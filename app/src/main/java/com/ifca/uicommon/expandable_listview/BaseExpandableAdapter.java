package com.ifca.uicommon.expandable_listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifca.uicommon.R;

/**
 * Created by rex on 2017/5/8.
 * 功能：可展开列表树
 */

public abstract class BaseExpandableAdapter<G, C> extends BaseExpandableListAdapter {
    public SparseArray<G> groupList;
    public SparseArray<SparseArray<C>> childList;
    public Context mContext;

    public BaseExpandableAdapter(SparseArray<G> groupList, SparseArray<SparseArray<C>> childList, Context mContext) {
        this.groupList = groupList;
        this.childList = childList;
        this.mContext = mContext;
    }

    public abstract int getGroupKey(int groupPosition);

    public abstract String getGroupName(int groupPosotion);

    public abstract String getChildName(int groupPosition, int childPosition);


    @Override
    public int getGroupCount() {
        if (groupList != null) {
            return groupList.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (childList != null) {
            return childList.get(getGroupKey(groupPosition)).size();
        }
        return 0;
    }

    @Override
    public G getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public C getChild(int groupPosition, int childPosition) {
        return childList.get(getGroupKey(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_expandable_group, null);
            viewHolder.Img_Group = (ImageView) convertView.findViewById(R.id.iv_expand_arrow);
            viewHolder.Tv_title = (TextView) convertView.findViewById(R.id.tv_expand_group);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.Tv_title.setText(getGroupName(groupPosition));
        if (isExpanded) {
            viewHolder.Img_Group.setBackgroundResource(R.drawable.ic_keyboard_down);
        } else {
            viewHolder.Img_Group.setBackgroundResource(R.drawable.ic_keyboard_right);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_expandablelist_child, null);
            viewHolder.Tv_title = (TextView) convertView.findViewById(R.id.tv_expand_child);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.Tv_title.setText(getChildName(groupPosition, childPosition));
        return convertView;
    }

    private static class ViewHolder {
        ImageView Img_Group;
        TextView Tv_title;
    }
}
