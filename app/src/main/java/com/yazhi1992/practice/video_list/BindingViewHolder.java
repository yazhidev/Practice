package com.yazhi1992.practice.video_list;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;


/**
 * Created by ck on 2017/2/23.
 */

public class BindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {


    protected T mBinding;


    public BindingViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public T getBinding() {
        return mBinding;
    }
}
