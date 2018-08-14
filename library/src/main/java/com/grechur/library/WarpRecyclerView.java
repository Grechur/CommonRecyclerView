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
    private WrapRecyclerAdapter mWrapAdapter;

    // 增加一些通用功能
    // 空列表数据应该显示的空View
    // 正在加载数据页面，也就是正在获取后台接口页面
    private View mEmptyView, mLoadingView;
    private Adapter mAdapter;

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

        this.mAdapter = adapter;

        if(adapter instanceof WrapRecyclerAdapter){
            mWrapAdapter  = (WrapRecyclerAdapter) adapter;
        }else{
            mWrapAdapter = new WrapRecyclerAdapter(adapter);
        }
        super.setAdapter(mWrapAdapter);
        // 注册一个观察者
        mAdapter.registerAdapterDataObserver(mDataObserver);

        // 解决GridLayout添加头部和底部也要占据一行
        mWrapAdapter.adjustSpanSize(this);

        // 加载数据页面
        if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.setVisibility(View.GONE);
        }

        if (mItemClickListener != null) {
            mWrapAdapter.setOnItemClickListener(mItemClickListener);
        }

        if (mLongClickListener != null) {
            mWrapAdapter.setOnLongClickListener(mLongClickListener);
        }
    }

    /**
     * 添加头部view
     * @param view
     */
    public void addHeaderView(View view){
        //不要重复加入
        if(mWrapAdapter!=null) {
            mWrapAdapter.addHeaderView(view);
        }
    }

    /**
     * 删除头部view
     * @param view
     */
    public void removeHeaderView(View view){
        if(mWrapAdapter!=null) {
            mWrapAdapter.removeHeaderView(view);
        }
    }

    /**
     * 添加底部view
     * @param view
     */
    public void addFooterView(View view){
        //不要重复加入
        if(mWrapAdapter!=null) {
            mWrapAdapter.addFooterView(view);
        }
    }

    /**
     * 删除底部view
     * @param view
     */
    public void removeFooterView(View view){
        if(mWrapAdapter!=null) {
            mWrapAdapter.removeFooterView(view);
        }
    }

    /**
     * 得到头部view的个数
     * @return
     */
    public int getHeadersCount(){
        return mWrapAdapter.getHeadersCount();
    }

    /**
     * 得到底部view的个数
     * @return
     */
    public int getFootersCount(){
        return mWrapAdapter.getFootersCount();
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyDataSetChanged();

            dataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemRemoved(positionStart);
            dataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemChanged(positionStart);
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemChanged(positionStart, payload);
            dataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemInserted(positionStart);
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
        if (mWrapAdapter.getItemCount() == 0) {
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

        if (mWrapAdapter != null) {
            mWrapAdapter.setOnItemClickListener(mItemClickListener);
        }
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;

        if (mWrapAdapter != null) {
            mWrapAdapter.setOnLongClickListener(mLongClickListener);
        }
    }

}
