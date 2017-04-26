package com.ifca.uicommon.xrecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rex on 2017/4/21.
 * 功能：基础设配器
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder>{
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mDatas = new ArrayList<>();
    public OnItemClickListener<T> mOnItemClickListener;

    public BaseRecyclerAdapter(Context context, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        if (datas != null && datas.size()>0) {
            mDatas = datas;
        }
    }

    protected abstract int getLayoutId();

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mDatas.size() > 0) {
            count = mDatas.size();
        }
        return count;
    }


    public void remove(int position) {
        mDatas.remove(position);
    }

    public void removeAll() {
        mDatas.clear();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    // 点击事件接口
    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T model);

        void onItemLongClick(View view, int position, T model);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        convert(holder,mDatas.get(position));
        holder.convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(v,position,mDatas.get(position));
                }
            }
        });
    }

    protected abstract void convert(BaseRecyclerViewHolder holder, T t);

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new QuickHolder(parent.getContext(),view);
    }


    public class QuickHolder extends BaseRecyclerViewHolder{

        protected QuickHolder(Context context, View view) {
            super(context, view);
        }
    }

}
