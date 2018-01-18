package com.yazhi1992.practice.video_list;

/**
 * Created by zengyazhi on 2018/1/17.
 */

public interface IVideoPlayer {
    void start();
    void pause();
    void seekTo(long position);
    boolean isPlaying();
    long getDuration();
    long getCurrentPosition();
    void release();
}
