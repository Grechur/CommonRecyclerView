package com.grechur.commonrecyclerview;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.grechur.library.OnItemClickListener;
import com.grechur.library.OnItemLongClickListener;
import com.grechur.library.WarpRecyclerView;
import com.grechur.library.WrapRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    WarpRecyclerView recycler_view;
    private List<String> mData;
    private MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler_view = findViewById(R.id.recycler_view);
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add(i+"");
        }
        mAdapter = new MyAdapter(this,mData);


        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(mAdapter);

        final View view = LayoutInflater.from(this).inflate(R.layout.layout_wrap_item,recycler_view,false);
        recycler_view.addHeaderView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_view.removeHeaderView(view);
            }
        });
        recycler_view.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,mData.get(position),Toast.LENGTH_SHORT).show();

            }
        });
//        recycler_view.setOnLongClickListener(new OnItemLongClickListener() {
//            @Override
//            public boolean onLongClick(int position) {
//                Toast.makeText(MainActivity.this,"删除"+mData.get(position),Toast.LENGTH_SHORT).show();
//                mData.remove(position);
//                mAdapter.notifyDataSetChanged();
//                return false;
//            }
//        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                Log.e("TAG","getMovementFlags");
                int currentSwapPosition = viewHolder.getAdapterPosition();
                if(currentSwapPosition<recycler_view.getHeadersCount()){
                    return 0;
                }
                int swapFlag = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;

                int dragFlag = 0;
                if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
                    dragFlag = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                }else{
                    dragFlag = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                }

                return makeMovementFlags(dragFlag,swapFlag);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Log.e("TAG","onMove");
                //原来的位置
                int fromPosition = viewHolder.getAdapterPosition();
                int fDic = viewHolder.getAdapterPosition()-recycler_view.getHeadersCount();
                //目标位置
                int targetPosition = target.getAdapterPosition();
                int tDic = target.getAdapterPosition()-recycler_view.getHeadersCount();
//                Log.e("TAG","fromPosition-->"+fromPosition+"targetPosition-->"+targetPosition);

                //有头部，操作的mdata，要先去掉头部占的位置
                if(fDic>tDic){
                    for (int i = fDic; i > tDic&&tDic>=0; i--) {
                        Collections.swap(mData,i,i-1);
                    }
                }else{
                    for (int i = fDic; i < tDic; i++) {
                        Collections.swap(mData,i,i+1);
                    }
                }
                mAdapter.notifyItemMoved(fromPosition,targetPosition);

                return false;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                Log.e("TAG","onSelectedChanged");
                //不在正常状态时，改变背景颜色
                if(actionState!=ItemTouchHelper.ACTION_STATE_IDLE){
                    viewHolder.itemView.setBackgroundColor(Color.GRAY);
                }


            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                Log.e("TAG","clearView");
                //动画执行完毕
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
                //侧滑删除有些条目出不来
                viewHolder.itemView.setTranslationX(0);
                mAdapter.notifyDataSetChanged();
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.e("TAG","onSwiped");
                //侧滑执行完毕，做操作（例如清除侧滑的数据）
                int currentSwapPosition = viewHolder.getAdapterPosition();
                if(currentSwapPosition>=recycler_view.getHeadersCount()) {
                    int position = currentSwapPosition-recycler_view.getHeadersCount();
                    mAdapter.notifyItemRemoved(currentSwapPosition);
                    mData.remove(position);

                }
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                Log.e("TAG","onMoved");
            }
        });
        itemTouchHelper.attachToRecyclerView(recycler_view);

    }
    public void onClick(View view){

    }
}
