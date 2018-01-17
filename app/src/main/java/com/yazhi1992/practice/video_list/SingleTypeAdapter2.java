package com.yazhi1992.practice.video_list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chenliu on 17/9/27.
 *
 */

public class SingleTypeAdapter2<T> extends BaseViewAdapter<T>{

    private int mLayoutRes;

    public interface Presenter<T> extends BaseViewAdapter.Presenter {
        void onItemClick(View target, T itemViewModel);
    }

    public SingleTypeAdapter2(Context context, List<T> datas, @LayoutRes int layoutRes) {
        super(context);
        mCollection = datas;
        this.mLayoutRes = layoutRes;
    }

    @Override
    protected BindingViewHolder inflateViewHolder(ViewGroup parent, int viewType) {
        return new BindingViewHolder(DataBindingUtil.inflate(layoutInflater, mLayoutRes, parent, false));
    }

    public List<T> getData() {
        return mCollection;
    }
}
