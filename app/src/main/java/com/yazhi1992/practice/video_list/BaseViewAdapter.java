package com.yazhi1992.practice.video_list;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;


/**
 * Created by ck on 2017/2/23.
 */

public abstract class BaseViewAdapter<T> extends RecyclerView.Adapter<BindingViewHolder> {

    public interface ItemDecorator<T extends ViewDataBinding> {
        void decorate(T itemBinding);
    }

    public interface Presenter {

    }

    protected List<T> mCollection;
    protected Presenter mPresenter;
    protected ItemDecorator decorator;

    protected LayoutInflater layoutInflater;


    public BaseViewAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BindingViewHolder holder = inflateViewHolder(parent,viewType);
        if (decorator != null) {
            decorator.decorate(holder.getBinding());
        }
        return holder;
    }


    protected abstract BindingViewHolder inflateViewHolder(ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        T item = mCollection.get(position);
        holder.getBinding().setVariable(BR.item, item);
        if (mPresenter == null) return;
        holder.getBinding().setVariable(BR.presenter, mPresenter);
    }

    public void clear() {
        mCollection.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mCollection.size();
    }


    public void setPresenter(Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void setItemDecorator(ItemDecorator decorator) {
        this.decorator = decorator;
    }

}
