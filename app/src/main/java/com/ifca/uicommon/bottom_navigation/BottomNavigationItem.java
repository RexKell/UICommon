package com.ifca.uicommon.bottom_navigation;

/**
 * Created by rex on 2017/4/21.
 * 功能：底部navigation
 */

public class BottomNavigationItem {
    /**
     * 内容标题*/
    private String title;
    /**
     * 被选中是的颜色*/
    private int color;
    /**
     * 内容图标*/
    private int imageResource;
    /**
     * 角标数目*/
    private String newsCount;

    public BottomNavigationItem(String title, int color, int imageResource,String newsCount) {
        this.title = title;
        this.color = color;
        this.imageResource = imageResource;
        this.newsCount=newsCount;
    }
    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(String newsCount) {
        this.newsCount = newsCount;
    }
}
