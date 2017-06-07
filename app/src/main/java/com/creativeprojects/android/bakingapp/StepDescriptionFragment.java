package com.creativeprojects.android.bakingapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creativeprojects.android.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.greenrobot.eventbus.EventBus;

public class StepDescriptionFragment extends Fragment
{
    private static final String EXOPLAYER_POSITION = "exoplayer_position";
    private static final String STEP_POSITION = "step_position";
    private static final String STEP_LIST_SIZE = "step_list_size";

    private long mLastExoPlayerPosition;

    private Step mStep;
    private int mStepPosition;
    private int mStepListSize;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    // The step buttons
    private AppCompatButton mBackStepButton;
    private AppCompatButton mNextStepButton;

    private OneStepBackButtonListener mOneStepBackButtonListener;
    private OneStepForwardButtonListener mOneStepForwardButtonListener;

    private boolean mTwoPane;

    public StepDescriptionFragment()
    {
        // Required empty public constructor
    }

    public static StepDescriptionFragment newInstance(int stepPosition, int stepListSize, Step step)
    {
        StepDescriptionFragment fragment = new StepDescriptionFragment();

        fragment.mStep = step;
        fragment.mStepPosition = stepPosition;
        fragment.mStepListSize = stepListSize;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_step_description, container, false);

        if(mStep == null)
        {
            Step step = EventBus.getDefault().getStickyEvent(Step.class);
            if(step != null)
                mStep = step;
        }

        // Check if the description is available (landscape mode)
        if(view.findViewById(R.id.description_textview) != null)
        {
            TextView descriptionTextview = (TextView) view.findViewById(R.id.description_textview);

            descriptionTextview.setText(mStep.getDescription());
        }

        if(savedInstanceState != null)
        {
            mLastExoPlayerPosition = savedInstanceState.getLong(EXOPLAYER_POSITION);
            mStepPosition = savedInstanceState.getInt(STEP_POSITION);
            mStepListSize = savedInstanceState.getInt(STEP_LIST_SIZE);
            mTwoPane = savedInstanceState.getBoolean("two_pane");
        }

        // Find step buttons id's
        mBackStepButton = (AppCompatButton) view.findViewById(R.id.back_step_button);
        mNextStepButton = (AppCompatButton) view.findViewById(R.id.next_step_button);

        // Set the click listeners and visibility
        if(mBackStepButton != null)
        {
            mBackStepButton.setOnClickListener(new BackButtonClickListener());
            mNextStepButton.setOnClickListener(new NextButtonClickListener());

            setStepButtonsVisibility();
        }

        mPlayerView = new SimpleExoPlayerView(getActivity());
        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);

        // Get the video url and test if is valid
        String videoURL = mStep.getVideoURL();
        if(videoURL == null || videoURL.isEmpty())
        {
            mPlayerView.setVisibility(View.GONE);

            if(view.findViewById(R.id.no_video_textview) != null)
            {
                TextView noVideoMessage = (TextView) view.findViewById(R.id.no_video_textview);
                noVideoMessage.setVisibility(View.VISIBLE);
            }

            return view;
        }

        initializePlayer(Uri.parse(videoURL));
        return view;
    }

    /**
     * Click listener for the back step button
     */
    private class BackButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            mOneStepBackButtonListener.goOneStepBack(mStepPosition);
        }
    }

    /**
     * Click listener for the next step button
     */
    private class NextButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            mOneStepForwardButtonListener.goOneStepForward(mStepPosition);
        }
    }

    /**
     * Based on the step position selected and the step
     * list size we set to GONE visibility to the step buttons
     */
    private void setStepButtonsVisibility()
    {
        if(mStepPosition == 0)
        {
            mBackStepButton.setVisibility(View.INVISIBLE);
        }

        if(mStepPosition == (mStepListSize - 1))
        {
            mNextStepButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if(mTwoPane)
            return;

        if (context instanceof OneStepBackButtonListener)
        {
            mOneStepBackButtonListener = (OneStepBackButtonListener) context;
        }
        else
        {
            //throw new RuntimeException(context.toString() + " must implement OneStepBackButtonListener");
        }

        if (context instanceof OneStepForwardButtonListener)
        {
            mOneStepForwardButtonListener = (OneStepForwardButtonListener) context;
        }
        else
        {
            //throw new RuntimeException(context.toString() + " must implement OneStepForwardButtonListener");
        }
    }

    public void setTwoPane(boolean twoPane)
    {
        mTwoPane = twoPane;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mOneStepBackButtonListener = null;
        mOneStepForwardButtonListener = null;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            //TrackSelector trackSelector = new DefaultTrackSelector();

            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(new DefaultBandwidthMeter());
            TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

            mPlayerView.setUseController(true);
            mPlayerView.requestFocus();
            mPlayerView.setPlayer(mExoPlayer);

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp2");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                                                                                bandwidthMeter,
                                                                                new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                                                               dataSourceFactory,
                                                               new DefaultExtractorsFactory(),
                                                               new Handler(),
                                                               null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

            mExoPlayer.seekTo(mLastExoPlayerPosition);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mExoPlayer == null)
            return;

        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if(mExoPlayer != null)
            outState.putLong( EXOPLAYER_POSITION , mExoPlayer.getCurrentPosition());

        outState.putInt( STEP_POSITION, mStepPosition);
        outState.putInt( STEP_LIST_SIZE, mStepListSize);
        outState.putBoolean("two_pane", mTwoPane);

        EventBus.getDefault().postSticky(mStep);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        releasePlayer();
    }

    public interface OneStepBackButtonListener
    {
        void goOneStepBack(int actualStepPosition);
    }

    public interface OneStepForwardButtonListener
    {
        void goOneStepForward(int actualStepPosition);
    }
}
