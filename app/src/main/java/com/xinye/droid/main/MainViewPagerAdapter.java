package com.xinye.droid.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;


/**
 * 主界面ViewPagerAdapter
 *
 * @author wangheng
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {


    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        if (MainActivity.TAB_HOME == position) {
//            return HomeFragment.newInstance();
//        } else if (MainActivity.TAB_HEAD_OFFICE == position) {
//            return HeadOfficeFragment.newInstance();
//        } else if (MainActivity.TAB_PARTY_BRANCH == position) {
//            if(UserManager.getInstance().isPartyMember()) {
//                return PartyBranchFragment.newInstance();
//            }else{
//                return PartyBranchEmptyFragment.newInstance();
//            }
//        } else if (MainActivity.TAB_STUDY == position) {
//            return StudyFragment.newInstance();
//        } else if (MainActivity.TAB_MY == position) {
//            return MyFragment.newInstance();
//        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
//        if (object instanceof HomeFragment) {
//            return PagerAdapter.POSITION_UNCHANGED;
//        } else if (object instanceof HeadOfficeFragment) {
//            return PagerAdapter.POSITION_NONE;
//        } else if (object instanceof PartyBranchFragment) {
//            return PagerAdapter.POSITION_NONE;
//        } else if (object instanceof StudyFragment) {
//            return PagerAdapter.POSITION_NONE;
//        } else if (object instanceof MyFragment) {
//            return PagerAdapter.POSITION_NONE;
//        }
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return MainActivity.COUNT_TAB;
    }
}
