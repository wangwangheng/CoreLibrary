package com.droid.library.app.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.droid.library.thirdplatform.imageloader.CoreImageLoader;

import java.util.ArrayList;

/**
 * Banner的ViewPager的适配器.
 *
 * @author wangheng
 */
public class BannerPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Banner> mBannerList = null;
    private Callback mCallback;

    public BannerPagerAdapter(Context context, ArrayList<Banner> bannerList, Callback callback) {
        this.mContext = context;
        this.mBannerList = bannerList;
        this.mCallback = callback;
    }

    @Override
    public int getCount() {
        if (mBannerList != null) {
            return mBannerList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mBannerList != null && mBannerList.size() != 0) {
            int pageIndex;
            int len = mBannerList.size();
            if (position == 0) {
                pageIndex = len - 1;
            } else if (position == len - 1) {
                pageIndex = 0;
            } else {
                pageIndex = position - 1;
            }
            try {
                ImageView bannerView = getBannerView(container.getContext(),mBannerList.get(position),pageIndex);
                container.addView(bannerView, 0);
                CoreImageLoader.getInstance().display(bannerView,mBannerList.get(position).getSrc());
                return bannerView;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private ImageView getBannerView(Context context, Banner banner, int reaalPosition){
        ImageView view = new ImageView(context);
        ViewGroup.LayoutParams bannerParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(bannerParams);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setOnClickListener(new BannerOnClickListener(mCallback, banner,reaalPosition));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}