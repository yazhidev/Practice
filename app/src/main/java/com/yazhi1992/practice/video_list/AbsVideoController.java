package com.yazhi1992.practice.video_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by zengyazhi on 2018/1/17.
 */

public class AbsVideoController extends FrameLayout {
    IVideoPlayer mVideoPlayer;

    public AbsVideoController(@NonNull Context context) {
        this(context, null);
    }

    public AbsVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoPlayer(IVideoPlayer player) {
        mVideoPlayer = player;
    }

}
