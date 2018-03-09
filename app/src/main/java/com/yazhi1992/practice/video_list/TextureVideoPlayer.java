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
// TODO: 2018/1/17 显示首帧
// TODO: 2018/1/18 缓冲时很卡
// TODO: 2018/1/18 重复点击播放崩溃
public class TextureVideoPlayer extends FrameLayout implements TextureView.SurfaceTextureListener, IVideoPlayer {

    private IMediaPlayer mMediaPlayer = null;
    private Context mContext;
    private TextureView mTextureView;
    private String mPath;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private AbsVideoController mController;
    private FrameLayout mContainer;

    public TextureVideoPlayer(Context context) {
        this(context, null);
    }

    public TextureVideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextureVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        setFocusable(true);
        mMediaPlayer = new IjkMediaPlayer();
        initContainer();
        initTextureView();
    }

    public void setController(AbsVideoController controller) {
        controller.setVideoPlayer(this);
        mContainer.addView(controller);
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

    /**
     * 设置地址
     * @param path
     */
    public void setPath(String path) {
        //如果是第一次播放视频，那就创建一个新的surfaceView
        mPath = path;
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
            mMediaPlayer.prepareAsync();
        }

        mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
//                mMediaPlayer.seekTo(0);
//                mMediaPlayer.pause();
            }
        });
    }

    @Override
    public void start() {
        VideoPlayManager.getInstance().setCurrentVideoPlayer(this);
        openMediaPlayer();
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void seekTo(long position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public boolean isPlaying() {
        if(mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            // TODO: 2018/1/18 保存播放位置
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
    }

    @Override
    public String getDataSourceUrl() {
        return mPath;
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

    @Override
    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //待 SurfaceTexture 准备就绪
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surface;
            //mContainer移除重新添加后，mContainer及其内部的mTextureView和mController都会重绘，mTextureView重绘后，
            // 会重新new一个SurfaceTexture，并重新回调onSurfaceTextureAvailable方法
//            openMediaPlayer();
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
