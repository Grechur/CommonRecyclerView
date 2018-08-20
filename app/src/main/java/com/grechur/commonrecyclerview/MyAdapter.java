package com.grechur.commonrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.grechur.library.CommonRecycleAdapter;
import com.grechur.library.MultiTypeSupport;
import com.grechur.library.OnItemClickListener;
import com.grechur.library.ViewHolder;

import java.util.List;

/**
 * Created by zhouzhu on 2018/8/13.
 */

public class MyAdapter extends CommonRecycleAdapter<User>{
    private List<User> mList;
    private Context mContext;
    private OnItemClickListener onItemClickListen;

    public MyAdapter(Context context, List<User> data, int layoutId) {
        super(context, data, layoutId);
    }

    public MyAdapter(Context context, List<User> data, MultiTypeSupport multiTypeSupport) {
        super(context, data, multiTypeSupport);
    }


    @Override
    public void convert(ViewHolder holder, User item) {
        holder.setText(R.id.text,item.name);
    }





}
