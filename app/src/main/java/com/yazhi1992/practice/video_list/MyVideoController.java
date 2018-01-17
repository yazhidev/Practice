package com.yazhi1992.practice.video_list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.yazhi1992.practice.R;
import com.yazhi1992.practice.databinding.LayoutVideoControllerBinding;

/**
 * Created by zengyazhi on 2018/1/17.
 */

public class MyVideoController extends AbsVideoController {

    private LayoutVideoControllerBinding mBinding;

    public MyVideoController(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_video_controller, this, true);

        mBinding.btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoPlayer.isPlaying()) {
                    mVideoPlayer.pause();
                } else {
                    mVideoPlayer.start();
                }
            }
        });

        mBinding.seekBar.setMax(1000);
        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                long duration = mVideoPlayer.getDuration();
                long position = duration * seekBar.getProgress() / 1000;
                mVideoPlayer.seekTo(position);
            }
        });
    }
}
