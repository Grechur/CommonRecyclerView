package com.grechur.library;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhouzhu on 2018/8/13.
 */

public class WarpRecyclerView extends RecyclerView{
    //通用adapter
    private WrapRecyclerAdapter mAdapter;

    // 增加一些通用功能
    // 空列表数据应该显示的空View
    // 正在加载数据页面，也就是正在获取后台接口页面
    private View mEmptyView, mLoadingView;

    public WarpRecyclerView(Context context) {
        this(context,null);
    }

    public WarpRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WarpRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {

        // 为了防止多次设置Adapter
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }

        if(adapter instanceof WrapRecyclerAdapter){
            mAdapter  = (WrapRecyclerAdapter) adapter;
        }else{
            mAdapter = new WrapRecyclerAdapter(adapter);
        }
        super.setAdapter(mAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);

        // 解决GridLayout添加头部和底部也要占据一行
        mAdapter.adjustSpanSize(this);

        // 加载数据页面
        if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.setVisibility(View.GONE);
        }

        if (mItemClickListener != null) {
            mAdapter.setOnItemClickListener(mItemClickListener);
        }

        if (mLongClickListener != null) {
            mAdapter.setOnLongClickListener(mLongClickListener);
        }
    }

    /**
     * 添加头部view
     * @param view
     */
    public void addHeaderView(View view){
        //不要重复加入
        if(mAdapter!=null) {
            mAdapter.addHeaderView(view);
        }
    }

    /**
     * 删除头部view
     * @param view
     */
    public void removeHeaderView(View view){
        if(mAdapter!=null) {
            mAdapter.removeHeaderView(view);
        }
    }

    /**
     * 添加底部view
     * @param view
     */
    public void addFooterView(View view){
        //不要重复加入
        if(mAdapter!=null) {
            mAdapter.addFooterView(view);
        }
    }

    /**
     * 删除底部view
     * @param view
     */
    public void removeFooterView(View view){
        if(mAdapter!=null) {
            mAdapter.removeFooterView(view);
        }
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            mAdapter.notifyDataSetChanged();
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeChanged(positionStart, itemCount);
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            mAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
            dataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeInserted(positionStart, itemCount);
            dataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            dataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mAdapter.notifyItemMoved(fromPosition, toPosition);
            dataChanged();
        }
    };

    /**
     * 添加一个空列表数据页面
     */
    public void addEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 添加一个正在加载数据的页面
     */
    public void addLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
        mLoadingView.setVisibility(View.VISIBLE);
    }
    /**
     * Adapter数据改变的方法
     */
    private void dataChanged() {
        if (mAdapter.getItemCount() == 0) {
            // 没有数据
            if (mEmptyView != null) {
                mEmptyView.setVisibility(VISIBLE);
            } else {
                mEmptyView.setVisibility(GONE);
            }
        }
    }

    /***************
     * 给条目设置点击和长按事件
     *********************/
    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;

        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(mItemClickListener);
        }
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;

        if (mAdapter != null) {
            mAdapter.setOnLongClickListener(mLongClickListener);
        }
    }

}
