package com.droid.library.app.banner;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.droid.library.app.App;
import com.xinye.lib.R;

import java.util.ArrayList;


/**
 * Page改变事件.
 *
 * @author wangheng
 */
public class OnBannerPageChangeListener implements OnPageChangeListener {

    private static final String TAG = "Banner";
    private static final boolean DEBUG = App.getInstance().isDebug();

    private ArrayList<Banner> bannerList = null;
    private LinearLayout indicatorContainer = null;
    private BannerViewPager viewPager;
    private OnPageChangeListener mRealPageChangeListener;
    private Callback mCallback;

    /**
     * 设置页面改变监听
     * @param listener listener
     */
    public void setOnPageChangeListener(OnPageChangeListener listener){
        this.mRealPageChangeListener = listener;
    }

    public OnBannerPageChangeListener(BannerViewPager viewPager,
                                      LinearLayout indicatorContainer,
                                      ArrayList<Banner> bannerList, Callback callback) {

        this.viewPager = viewPager;
        this.indicatorContainer = indicatorContainer;
        this.bannerList = bannerList;
        this.mCallback = callback;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mRealPageChangeListener != null){
            mRealPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }
        if (bannerList != null && bannerList.size() > 1) {
            try {
                if (position == bannerList.size() - 1 && positionOffset == 0) {
                    viewPager.setCurrentItem(1, false);
                } else if (position == 0 && positionOffset == 0) {
                    viewPager.setCurrentItem(bannerList.size() - 2, false);
                }
            } catch (Exception e) {
                if(DEBUG) {
                    if(DEBUG) {
                        e.printStackTrace();
                        Log.e(TAG, "banner onPageScrolled failed:" + e);
                    }
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if(mRealPageChangeListener != null){
            mRealPageChangeListener.onPageSelected(position);
        }

        if (bannerList == null || bannerList.size() <= 1
                || bannerList == null || bannerList.size() <= 1) {
            return;
        }

       /* if(position == 0){
            viewPager.setCurrentItem(bannerViewList.size() - 2,false);
        }else if(position == bannerViewList.size() - 1){
            viewPager.setCurrentItem(1, false);
        }*/

        int count = indicatorContainer.getChildCount();
        int pageIndex ;
        if (position == 0) {
            pageIndex = count - 1;
        } else if (position == bannerList.size() - 1) {
            pageIndex = 0;
        } else {
            pageIndex = position - 1;
        }
        for (int i = 0; i < count; i++) {
            ImageView indicatorView = (ImageView) indicatorContainer.getChildAt(i);
            if (i == pageIndex) {
                indicatorView.setImageResource(R.drawable.icon_indicator_selected);
            } else {
                indicatorView.setImageResource(R.drawable.icon_indicator_normal);
            }
        }

        if(mCallback != null && position >= 1 && position <= bannerList.size() - 2){
            mCallback.onBannerSelected(bannerList.get(position),pageIndex);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(mRealPageChangeListener != null){
            mRealPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}