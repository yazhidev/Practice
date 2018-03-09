package com.yazhi1992.practice.video_list;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengyazhi on 2018/1/18.
 */

public class VideoPlayManager {

    private IVideoPlayer mVideoPlayer;
    private Map<String, Long> mCachePositionMaps = new HashMap<>();

    private VideoPlayManager() {

    }

    private static class VideoPlayManagerHolder {
        private static VideoPlayManager INSTANCE = new VideoPlayManager();
    }

    public static VideoPlayManager getInstance() {
        return VideoPlayManagerHolder.INSTANCE;
    }

    public void setCurrentVideoPlayer(IVideoPlayer textureVideoPlayer) {
        mVideoPlayer = textureVideoPlayer;
    }

    public long getPositionFormCache(String url) {
        if(!mCachePositionMaps.containsKey(url)) {
            mCachePositionMaps.put(url, 0L);
        }
        return mCachePositionMaps.get(url);
    }

    public void savePostion(String url, long position) {
        mCachePositionMaps.put(url, position);
    }

    public void release() {
        if(mVideoPlayer != null) {
            savePostion(mVideoPlayer.getDataSourceUrl(), mVideoPlayer.getCurrentPosition());
            mVideoPlayer.release();
        }
    }
}
