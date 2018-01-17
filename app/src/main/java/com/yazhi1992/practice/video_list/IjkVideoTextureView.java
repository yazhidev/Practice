package com.yazhi1992.practice.video_list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by zengyazhi on 2018/1/17.
 */

public class IjkVideoTextureView extends FrameLayout implements TextureView.SurfaceTextureListener {

    private IMediaPlayer mMediaPlayer = null;
    private Context mContext;
    private TextureView mTextureView;
    private String mPath;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private YazhiController mController;
    private FrameLayout mContainer;

    public IjkVideoTextureView(Context context) {
        this(context, null);
    }

    public IjkVideoTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IjkVideoTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        setFocusable(true);
        mMediaPlayer = new IjkMediaPlayer();
        initContainer();
        initTextureView();
//        mPath = "http://7xr4ce.media1.z0.glb.clouddn.com/22379-201801171138804.m3u8";
    }

    private void initContainer() {
        mContainer = new FrameLayout(mContext);
        mContainer.setBackgroundColor(Color.BLACK);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mContainer, params);
    }

    private void initTextureView() {
        mTextureView = new TextureView(mContext);
        mTextureView.setSurfaceTextureListener(this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT);
        mContainer.addView(mTextureView, layoutParams);
    }

    public void setPath(String path) {
        //如果是第一次播放视频，那就创建一个新的surfaceView
        mPath = path;
    }

    private void startPlay() {
    }

    private void openMediaPlayer() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.stop();
//            mMediaPlayer.setDisplay(null);
//            mMediaPlayer.release();
//        }
//      开启硬解码
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        if (mSurface == null) {
            mSurface = new Surface(mSurfaceTexture);
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setSurface(mSurface);
        if (mPath != null && !mPath.isEmpty()) {
            try {
                mMediaPlayer.setDataSource(mPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //给mediaPlayer设置视图
            mMediaPlayer.prepareAsync();
        }

        mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                mMediaPlayer.seekTo(4000);
            }
        });
    }


    /**
     * 封装控制视频的方法
     */

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }


    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }


    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }


    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }


    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //待 SurfaceTexture 准备就绪
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surface;
            //mContainer移除重新添加后，mContainer及其内部的mTextureView和mController都会重绘，mTextureView重绘后，
            // 会重新new一个SurfaceTexture，并重新回调onSurfaceTextureAvailable方法
            openMediaPlayer();
        } else {
            mTextureView.setSurfaceTexture(mSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return mSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
