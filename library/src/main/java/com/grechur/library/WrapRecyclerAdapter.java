package com.grechur.library;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by zhouzhu on 2018/8/13.
 */

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //头部和底部的view的集合
    private SparseArray<View> mHeaderViews,mFooterViews;
    //本身的adapter
    private RecyclerView.Adapter mAdapter;

    private int HEADERTYPE =100000000;
    private int FOOTERTYPE = 20000000;

    public WrapRecyclerAdapter(RecyclerView.Adapter adapter){
        mAdapter = adapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mHeaderViews.indexOfKey(viewType)>=0){
            //是头部
            return creatHeaderFooterViewHolder(mHeaderViews.get(viewType));
        }
        if(mFooterViews.indexOfKey(viewType)>=0){
            //是底部
            return creatHeaderFooterViewHolder(mFooterViews.get(viewType));
        }
        //列表
        return mAdapter.createViewHolder(parent, viewType);
    }

    /**
     * 创建头部和底部viewholder
     * @param view
     */
    private RecyclerView.ViewHolder creatHeaderFooterViewHolder(View view) {

        return new RecyclerView.ViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position<mHeaderViews.size()){
            return;
        }
        final int adjPosition = position - mHeaderViews.size();
        int adapterCount = mAdapter.getItemCount();
        if(adjPosition < adapterCount){
            mAdapter.onBindViewHolder(holder,adjPosition);
        }

        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(adjPosition);
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(adjPosition);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position<mHeaderViews.size()){
            return mHeaderViews.keyAt(position);
        }

        final int adjPosition = position - mHeaderViews.size();
        int adapterCount = mAdapter.getItemCount();
        if(adjPosition < adapterCount){
            return super.getItemViewType(position);
        }

        return mFooterViews.keyAt(adjPosition-adapterCount);
    }

    @Override
    public int getItemCount() {
        return mHeaderViews.size()+mFooterViews.size()+mAdapter.getItemCount();
    }

    /**
     * 添加头部view
     * @param view
     */
    public void addHeaderView(View view){
        //不要重复加入
        if(mHeaderViews!=null&&mHeaderViews.indexOfValue(view)==-1){
            mHeaderViews.put(HEADERTYPE++,view);
            notifyDataSetChanged();
        }
    }

    /**
     * 删除头部view
     * @param view
     */
    public void removeHeaderView(View view){
        if(mHeaderViews!=null&&mHeaderViews.indexOfValue(view)>-1){
            mHeaderViews.removeAt(mHeaderViews.indexOfValue(view));
            notifyDataSetChanged();
        }
    }

    /**
     * 添加底部view
     * @param view
     */
    public void addFooterView(View view){
        //不要重复加入
        if(mFooterViews!=null&&mFooterViews.indexOfValue(view)==-1){
            mFooterViews.put(FOOTERTYPE++,view);
            notifyDataSetChanged();
        }
    }

    /**
     * 删除底部view
     * @param view
     */
    public void removeFooterView(View view){
        if(mFooterViews!=null&&mFooterViews.indexOfValue(view)>-1){
            mFooterViews.removeAt(mFooterViews.indexOfValue(view));
            notifyDataSetChanged();
        }
    }

    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     *
     * @param recycler
     * @version 1.0
     */
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter =
                            isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 是否是底部
     * @param position
     * @return
     */
    private boolean isFooterPosition(int position) {
        return position<mHeaderViews.size();
    }

    /**
     * 是否是头部
     * @param position
     * @return
     */
    private boolean isHeaderPosition(int position) {
        return position>=(mHeaderViews.size()+mAdapter.getItemCount());
    }

    /***************
     * 给条目设置点击和长按事件
     *********************/
    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }
}
