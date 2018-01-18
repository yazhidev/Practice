package com.yazhi1992.practice.video_list;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;

/**
 * Created by zengyazhi on 2018/1/17.
 */

public class VideoDataViewModel {

    public static final String PLAY = "start"; //播放中
    public static final String PAUSE = "pause"; //暂停
    public static final String DESTROY = "destroy"; //销毁

    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> playStatus = new ObservableField<>(PAUSE);
    public ObservableField<String> url = new ObservableField<>();

    @BindingAdapter("initItem")
    public static void videoPath(ItemVideoView view, VideoDataViewModel item) {
        view.mBinding.setItem(item);
        view.setPath(item.url.get());
    }

    @BindingAdapter("initPresenter")
    public static void videoPath(ItemVideoView view, SingleTypeAdapter2.Presenter presenter) {
        view.mBinding.setPresenter(presenter);
    }

    @BindingAdapter("initPath")
    public static void videoPath(ItemVideoView view, String url) {
    }
}
