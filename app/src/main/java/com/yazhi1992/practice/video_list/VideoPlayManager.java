package com.yazhi1992.practice.video_list;

/**
 * Created by zengyazhi on 2018/1/18.
 */

public class VideoPlayManager {

    private IVideoPlayer mTextureVideoPlayer;

    private VideoPlayManager() {

    }

    private static class VideoPlayManagerHolder {
        private static VideoPlayManager INSTANCE = new VideoPlayManager();
    }

    public static VideoPlayManager getInstance() {
        return VideoPlayManagerHolder.INSTANCE;
    }

    public void setCurrentVideoPlayer(IVideoPlayer textureVideoPlayer) {
        mTextureVideoPlayer = textureVideoPlayer;
    }

    public void release() {
        if(mTextureVideoPlayer != null) {
            mTextureVideoPlayer.release();;
        }
    }
}
