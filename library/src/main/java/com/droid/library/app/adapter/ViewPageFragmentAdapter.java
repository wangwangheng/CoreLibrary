package com.droid.library.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

@SuppressLint("Recycle")
public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    private final ArrayList<ViewPageInfo> mTabs = new ArrayList<ViewPageInfo>();

    public ViewPageFragmentAdapter(FragmentManager fm, ViewPager pager) {
        super(fm);
        mContext = pager.getContext();
    }

    public void addTab(String title, String tag, Class<?> clz, Bundle args) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, clz, args);
        mTabs.add(viewPageInfo);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {

        ViewPageInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }
}