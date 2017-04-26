package com.ifca.uicommon.tab_fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rex on 2017/2/16.
 * 功能：Tab类的fragment
 */

public class TabFragment extends TabBaseFragment {
    private static FragmentManager fm;
     private static List<String> titles=new ArrayList<>();
    private static List<Fragment> fragments=new ArrayList<>();

    public static TabFragment getInstance(FragmentManager fragmentManager,List<String> titleList,List<Fragment> fragmentList) {
        titles=titleList;
        fragments=fragmentList;
        fm = fragmentManager;
        return new TabFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected List<Fragment> setFragmentList() {
        return fragments;
    }

    @Override
    protected List<String> setTabTitle() {
        return titles;
    }

    @Override
    protected FragmentManager setFragmentManager() {
        return fm;
    }
}
