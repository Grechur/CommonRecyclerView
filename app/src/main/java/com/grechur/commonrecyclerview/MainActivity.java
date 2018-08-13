package com.grechur.commonrecyclerview;

import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        for (int i = 0; i < 10; i++) {
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
        recycler_view.setOnLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onLongClick(int position) {
                Toast.makeText(MainActivity.this,"删除"+mData.get(position),Toast.LENGTH_SHORT).show();
                mData.remove(position);
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }
    public void onClick(View view){

    }
}
