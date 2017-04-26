package com.ifca.uicommon.tab_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifca.uicommon.R;

import java.util.List;

/**
 * Created by rex on 2017/4/24.
 * 功能：Tab fragment 基类
 */

public abstract class TabBaseFragment extends Fragment {
    public View mRootView;
    public ViewPager mViewPager;
    public TabLayout mTab;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected abstract void initData();

    protected abstract List<Fragment> setFragmentList();

    protected abstract List<String> setTabTitle();
    protected abstract FragmentManager setFragmentManager();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fg_tab_viewpager, null);
        }
        initView(mRootView);
        initData();

        initTitleAndFragment();
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.fg_viewpager);
        mTab = (TabLayout) view.findViewById(R.id.fg_tab);
    }

    private void initTitleAndFragment() {
        mViewPager.setOffscreenPageLimit(setTabTitle().size());

        mViewPager.setAdapter(new ViewPagerAdapter(setFragmentManager()));
        mTab.setupWithViewPager(mViewPager);

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return setFragmentList().get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return setTabTitle().get(position);
        }

        @Override
        public int getCount() {
            return setTabTitle().size();
        }
    }
}
