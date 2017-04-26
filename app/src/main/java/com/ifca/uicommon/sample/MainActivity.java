package com.ifca.uicommon.sample;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ifca.uicommon.R;
import com.ifca.uicommon.bottom_navigation.BottomNavigationItem;
import com.ifca.uicommon.bottom_navigation.BottomNavigationView;
import com.ifca.uicommon.xrecycler.BaseRecyclerAdapter;
import com.ifca.uicommon.xrecycler.BaseRecyclerViewHolder;
import com.ifca.uicommon.xrecycler.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseRecyclerAdapter.OnItemClickListener,XRecyclerView.LoadingListener {
    BottomNavigationView bottomNavigationView;
    XRecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        sampleXRecyclerView();
        sampleNavigationView();
    }
    private void initView(){
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.main_bottom_navigationview);
        mRecycler=(XRecyclerView)findViewById(R.id.main_recycle);
    }

    /**
     * XRecycler  使用示例*/
    private void sampleXRecyclerView(){
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        mRecycler.setArrowImageView(R.drawable.icon_arrow);
        mRecycler.setLoadingListener(this);


        List<String> data=new ArrayList<>();
        for (int i=0;i<20;i++){
            data.add(i+"");
        }
        /**
         *传入类型
         *  */
       BaseRecyclerAdapter<String> mAdapter=new BaseRecyclerAdapter<String>(this,data) {
           @Override
           protected int getLayoutId() {
               return R.layout.item_sample_recycler;
           }

           @Override
           protected void convert(BaseRecyclerViewHolder holder, String s) {
               holder.setText(R.id.tv_sample_recycler,s);

           }
       };
       mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);


    }

    /**
     * Bottom Navigation 使用示例*/
    private void sampleNavigationView(){
        int color=0xff738ffe;
        if(bottomNavigationView!=null){
            //设置是否显示文字
            bottomNavigationView.isWithText(true);
            //是否设置背景色
            bottomNavigationView.isColoredBackground(false);
            //设置文字的颜色
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(color);
        }
        BottomNavigationItem item1=new BottomNavigationItem("标题一",color,R.drawable.icon_bottom_todo,"10");
        BottomNavigationItem item2=new BottomNavigationItem("标题二",color,R.drawable.icon_bottom_message,"0");
        BottomNavigationItem item3=new BottomNavigationItem("标题三",color,R.drawable.icon_bottom_function,"10");
        BottomNavigationItem item4=new BottomNavigationItem("标题四",color,R.drawable.icon_bottom_setting,"0");
        bottomNavigationView.addTab(item1);
        bottomNavigationView.addTab(item2);
        bottomNavigationView.addTab(item3);
        bottomNavigationView.addTab(item4);
        bottomNavigationView.setOnBottomNavigationItemClickListener(new BottomNavigationView.OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                Toast.makeText(MainActivity.this,index+"",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position, Object model) {

    }

    @Override
    public void onItemLongClick(View view, int position, Object model) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
