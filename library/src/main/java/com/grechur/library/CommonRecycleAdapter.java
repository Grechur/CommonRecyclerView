package com.grechur.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zz on 2018/8/17.
 */

public abstract class CommonRecycleAdapter<T> extends RecyclerView.Adapter<ViewHolder>{
    //数据
    private List<T> mData;
    //上下文
    private Context mContext;
    //页面加载flater
    private LayoutInflater mLayoutInflater;
    //页面id
    private int mLayoutId;

    private MultiTypeSupport mMultiTypeSupport;

    public CommonRecycleAdapter(Context context,List<T> data,int layoutId){
        this.mContext = context;
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mLayoutId = layoutId;
    }


    public CommonRecycleAdapter(Context context,List<T> data,MultiTypeSupport multiTypeSupport){
        this(context,data,-1);
        this.mMultiTypeSupport = multiTypeSupport;
    }


    /**
     * 根据当前位置获取不同的viewType
     */
    @Override
    public int getItemViewType(int position) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getLayoutId(mData.get(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType;
        }
        // 先inflate数据
        View itemView = mLayoutInflater.inflate(mLayoutId, parent, false);
        // 返回ViewHolder
        ViewHolder holder = new ViewHolder(itemView);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(position);
                }
            });
        }

        // 绑定怎么办？回传出去
        convert(holder, mData.get(position));
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     *
     * @param item 当前的数据
     */
    public abstract void convert(ViewHolder holder, T item);

    @Override
    public int getItemCount() {
        return mData.size();
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
