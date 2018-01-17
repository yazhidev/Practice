package com.yazhi1992.practice.video_list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yazhi1992.practice.R;
import com.yazhi1992.practice.databinding.ActivityVideoPlayBinding;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoPlayActivity extends AppCompatActivity {

    private ActivityVideoPlayBinding mBinding;
    private AbsVideoController mVideoController;
    private TextureVideoPlayer mVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_play);

        //加载native库
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            finish();
        }

        mVideoPlayer = mBinding.textureVideoPlayer;
        mVideoController = new MyVideoController(this);
        mBinding.textureVideoPlayer.setController(mVideoController);
        mBinding.textureVideoPlayer.setPath("http://7xr4ce.media1.z0.glb.clouddn.com/22379-201801171138804.m3u8");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoPlayer.pause();
        IjkMediaPlayer.native_profileEnd();
    }
}
