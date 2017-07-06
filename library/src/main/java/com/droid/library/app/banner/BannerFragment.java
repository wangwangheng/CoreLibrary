package com.droid.library.app.banner;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.droid.library.app.App;
import com.droid.library.app.BaseFragment;
import com.droid.library.utils.app.DeviceUtils;
import com.droid.library.utils.app.PixelUtil;
import com.xinye.lib.R;

import java.util.ArrayList;


/**
 * @author wangheng
 *  BannerFragment.
 * <p/>
 * 调用Banner模块的时候需要给Banner模块设置宽度、高度、数据列表和Banner点击的回调，按照以下示例：<br/>
 * <pre>
 *
 * Bundle bundle = new Bundle();
 * bundle.putInt(BannerFragment.KEY_WIDTH, DeviceUtils.getScreenWidth(App.getInstance().getContext()));
 * bundle.putInt(BannerFragment.KEY_HEIGHT, DeviceUtils.getScreenWidth(App.getInstance().getContext()) / 2);
 * bundle.putSerializable(BannerFragment.KEY_BANNER_LIST, bannerList);
 *
 * BannerFragment fragment = new BannerFragment();
 * fragment.setCallback(new Callback() {
 * public void onBannerItemClick(View itemView, Banner item) {
 * getUI().onBannerItemClick(itemView,item);
 * }
 * });
 * fragment.setData(bundle);
 * </pre>
 */
public class BannerFragment extends BaseFragment {

    public static final String KEY_WIDTH = "keyWidth";
    public static final String KEY_HEIGHT = "keyHeight";
    public static final String KEY_BANNER_LIST = "keyBannerList";
    /**
     * 开始时的延迟时间 *
     */
    private static final int AUTO_SCROLL_INTERVAL = 3000;
    /**
     * 保存状态的Data的Key *
     */
    private static final String KEY_DATA = "keyBannerFragmentData";
    /**
     * Banner的容器 *
     */
    private BannerViewPager mViewPager;

    /**
     * 指示器View *
     */
    private LinearLayout mIndicatorContainer;

    /**
     * banner 列表 *
     */
    private ArrayList<Banner> mBannerList;


    /**
     * ViewPager的Adapter *
     */
    private BannerPagerAdapter mAdapter;

    private View mRootView = null;

    private int mWidth;
    private int mHeight;
    private static final float RATIO_DEFAULT = 0.5f;

    private Bundle mData;

    private Callback mCallback = null;

    private OnBannerPageChangeListener mBannerPageChangeListener;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public static BannerFragment create(ArrayList<Banner> bannerList){
        int width = DeviceUtils.getScreenWidth(App.getInstance().getContext());
        int height = (int)(width * RATIO_DEFAULT);

        return create(bannerList,width,height);
    }
    public static BannerFragment create(ArrayList<Banner> bannerList, int width, int height){
        Bundle bundle = new Bundle();
        bundle.putInt(BannerFragment.KEY_WIDTH, width);
        bundle.putInt(BannerFragment.KEY_HEIGHT, height);
        bundle.putSerializable(BannerFragment.KEY_BANNER_LIST, bannerList);

        BannerFragment fragment = new BannerFragment();
        fragment.setData(bundle);
        return fragment;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener){
        this.mOnPageChangeListener = listener;
        if(mBannerPageChangeListener != null){
            mBannerPageChangeListener.setOnPageChangeListener(listener);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle(KEY_DATA);
            if (bundle != null) {
                mData = bundle;
            }
        }
        if (mData != null) {
            mWidth = mData.getInt(KEY_WIDTH);
            mHeight = mData.getInt(KEY_HEIGHT);
            if (mWidth == 0 || mHeight == 0) {
                mWidth = DeviceUtils.getScreenWidth(App.getInstance().getContext());
                mHeight = (int)(mWidth * RATIO_DEFAULT);
            }
            mBannerList = (ArrayList<Banner>) mData.getSerializable(KEY_BANNER_LIST);
        } else {
            mWidth = DeviceUtils.getScreenWidth(App.getInstance().getContext());
            mHeight = (int)(mWidth * RATIO_DEFAULT);
        }
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_banner, container, false);
            mViewPager = (BannerViewPager) mRootView.findViewById(R.id.bannerViewPager);

            LayoutParams params = (LayoutParams) mViewPager.getLayoutParams();
            params.width = mWidth;
            params.height = mHeight;
            mViewPager.setLayoutParams(params);

            mIndicatorContainer = (LinearLayout) mRootView.findViewById(R.id.bannerIndicatorContainer);
            onBannerListRequestCompleted();
        }
        return mRootView;
    }

    /**
     * setCallback:设置Banner的事件Callback. <br/>
     *
     * @param callback callback
     */
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mData != null) {
            outState.putBundle(KEY_DATA, mData);
        }
    }

    /**
     * setData:设置启动这个Fragment必须的数据. <br/>
     *
     * @param bundle bundle
     */
    public void setData(Bundle bundle) {
        this.mData = bundle;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRootView != null && mRootView.getParent() != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewPager != null && mBannerList != null && mBannerList.size() > 1) {
            mViewPager.stopAutoScroll();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewPager != null && mBannerList != null && mBannerList.size() > 1) {
            mViewPager.startAutoScroll();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mViewPager != null && mBannerList != null && mBannerList.size() > 1) {
            if (!hidden) {
                mViewPager.startAutoScroll();
            } else {
                mViewPager.stopAutoScroll();
            }
        }
    }

    public void onUserVisibleHint(boolean isVisibleToUser) {
        if(BannerFragment.this.isDetached()){
            return;
        }
            if (mViewPager != null && mBannerList != null && mBannerList.size() > 1) {
            if (isVisibleToUser) {
                mViewPager.startAutoScroll();
            } else {
                mViewPager.stopAutoScroll();
            }
        }
    }

    /**
     * onBannerListRequestCompleted:BannerList请求完成的回调 <br/>
     *
     */
    public void onBannerListRequestCompleted() {

        if (mBannerList == null) {
            mViewPager.setVisibility(View.GONE);
        } else {
            if (mBannerList.size() == 0) {
                mViewPager.setVisibility(View.GONE);
                return;
            }
            // 得到View层中的ViewPager和Indicator容器
            mViewPager.setVisibility(View.VISIBLE);

            addFirstAndLast();

            // 得到指示器View的布局参数
            LinearLayout.LayoutParams indicatorParams = getIndicatorViewLayoutParams();

            int len = mBannerList.size();
            for (int i = 0; i < len; i++) {
                Banner banner = mBannerList.get(i);
                if (banner == null) {
                    continue;
                }

                if (len > 1 && i != 0 && i != len - 1) {
                    addIndicatorView(indicatorParams,i);
                }
            }

            setViewPagerAttributes();
        }
    }

    /**
     * setViewPagerAttributes:设置ViewPager的属性们. <br/>
     */
    private void setViewPagerAttributes() {
        try {
            mBannerPageChangeListener = new OnBannerPageChangeListener(mViewPager,
                    mIndicatorContainer,
                    mBannerList,mCallback);

            mViewPager.addOnPageChangeListener(mBannerPageChangeListener);

            if(mOnPageChangeListener != null){
                mBannerPageChangeListener.setOnPageChangeListener(mOnPageChangeListener);
            }
            mAdapter = new BannerPagerAdapter(getActivity(), mBannerList,mCallback);
            mViewPager.setAdapter(mAdapter);

            if (mBannerList.size() > 1) {
                mViewPager.setOffscreenPageLimit(mBannerList.size());
                mViewPager.setCurrentItem(1, false);
                mViewPager.setAutoScrollInterval(AUTO_SCROLL_INTERVAL);
                mViewPager.startAutoScroll();
            } else if (mBannerList.size() == 1) {
                mViewPager.setCurrentItem(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * addFirstAndLast:把第一个拷贝并添加到List结尾，并把最后一个拷贝并添加到List开头. <br/>
     * 如：原列表：0,1,2,3，则把3拷贝到0之前并把0拷贝到3之后变成：3,0,1,2,3,0
     */
    private void addFirstAndLast() {
        if (mBannerList == null || mBannerList.size() <= 1) {
            return;
        }

        Banner first = mBannerList.get(0);
        Banner last = mBannerList.get(mBannerList.size() - 1);

        if (mBannerList.size() > 2 && last == mBannerList.get(1)) {
            return;
        }

        mBannerList.add(0, last);
        mBannerList.add(first);
    }

    /**
     * getIndicatorViewLayoutParams:得到指示器的布局参数. <br/>
     *
     * @return LayoutParams
     */
    private LinearLayout.LayoutParams getIndicatorViewLayoutParams() {
        LinearLayout.LayoutParams indicatorParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        indicatorParams.gravity = Gravity.CENTER;
        indicatorParams.leftMargin = PixelUtil.dip2px(5);
        return indicatorParams;
    }

    /**
     * addIndicatorView:添加指示器View. <br/>
     *
     * @param indicatorParams params
     * @param position position
     */
    private void addIndicatorView(LinearLayout.LayoutParams indicatorParams, final int position) {
        ImageView indicatorView = new ImageView(getActivity());

        indicatorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager != null){
                    mViewPager.setCurrentItem(position);
                }
            }
        });

        indicatorView.setLayoutParams(indicatorParams);
        indicatorView.setScaleType(ScaleType.FIT_XY);
        indicatorView.setImageResource(R.drawable.icon_indicator_normal);
        mIndicatorContainer.addView(indicatorView);
    }

}

