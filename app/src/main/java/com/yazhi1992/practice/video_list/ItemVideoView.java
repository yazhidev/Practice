package com.yazhi1992.practice.video_list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.yazhi1992.practice.R;
import com.yazhi1992.practice.databinding.ItemVideoBinding;


/**
 * Created by zengyazhi on 2018/1/16.
 */

public class ItemVideoView extends RelativeLayout {

    public ItemVideoBinding mBinding;

    public ItemVideoView(Context context) {
        this(context, null);
    }

    public ItemVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_video, this, true);
    }

    public void init() {
//        mBinding.videoPlayView.setSeekBar(findViewById(R.id.seekBar));
//        mBinding.videoPlayView.setTvCurrent(findViewById(R.id.tvCurrentInItem));
//        mBinding.videoPlayView.setTvDuration(findViewById(R.id.tvDuration));
//        mBinding.videoPlayView.setCompletionListener(new VideoPlayView.OnCompletionListener() {
//            @Override
//            public void OnCompletion(PLMediaPlayer plMediaPlayer) {
//                mBinding.getItem().playStatus.set(CourseVideoViewModel.PAUSE);
//            }
//        });
//        mBinding.videoPlayView.setPath(mBinding.getItem().fileUrl.get());
    }

    public ItemVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
