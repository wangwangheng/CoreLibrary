package com.xinye.droid.app.entry.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.droid.library.app.BaseActivity;
import com.droid.library.app.storage.sp.SPManagerDefault;
import com.droid.library.thirdplatform.imageloader.CoreImageLoader;
import com.xinye.app.R;
import com.xinye.droid.app.storage.sp.SPKeys;
import com.xinye.droid.main.MainActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 引导页
 *
 * @author wangheng
 */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "GuideActivity";
    private static final int[] GUIDE_ARRAY = {
            R.drawable.guide_01,
            R.drawable.guide_02,
            R.drawable.guide_03,
            R.drawable.guide_04};

    @InjectView(R.id.vpGuide)
    ViewPager mViewPager;
//    @InjectView(R.id.llGuide)
//    LinearLayout mIndicatorLayout;
//    @InjectView(R.id.btnGuide)
//    Button mEnterButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 得到指示器View的布局参数
//        LinearLayout.LayoutParams indicatorParams = getIndicatorViewLayoutParams();
//
//        for (int i = 0; i < GUIDE_ARRAY.length; i++) {
//            addIndicatorView(indicatorParams, i);
//        }

        mViewPager.setAdapter(new Adapter());
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(GuideActivity.this);

    }

//    private LinearLayout.LayoutParams getIndicatorViewLayoutParams() {
//        LinearLayout.LayoutParams indicatorParams =
//                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);
//        indicatorParams.gravity = Gravity.CENTER;
//        indicatorParams.leftMargin = PixelUtil.dip2px(5);
//        return indicatorParams;
//    }
//
//    private void addIndicatorView(LinearLayout.LayoutParams indicatorParams, int position) {
//        ImageView indicatorView = new ImageView(GuideActivity.this);
//        indicatorView.setLayoutParams(indicatorParams);
//        indicatorView.setScaleType(ImageView.ScaleType.FIT_XY);
//        if (position == 0) {
//            indicatorView.setImageResource(R.drawable.icon_indicator_selected);
//        } else {
//            indicatorView.setImageResource(R.drawable.icon_indicator_normal);
//        }
//        mIndicatorLayout.addView(indicatorView);
//    }

    @OnClick({/**R.id.btnGuide,**/com.xinye.app.R.id.btnIgnore})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btnGuide:
//                Logger.i(TAG, "guide button click");
//
//                // 设置不需要再次显示引导页
//                SPDefaultManager.getInstance().putBoolean(SPKeys.KEY_NEED_SHOW_GUIDE, false);
//
//                startActivity(new Intent(GuideActivity.this, MainActivity.class));
//                finishActivity();
//                break;
            case com.xinye.app.R.id.btnIgnore:
                enterNextPage();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//
//        if (position == GUIDE_ARRAY.length - 1) {
//            mEnterButton.setVisibility(View.VISIBLE);
//        } else {
//            mEnterButton.setVisibility(View.GONE);
//        }

//        int count = mIndicatorLayout.getChildCount();
//        for (int i = 0; i < count; i++) {
//            ImageView indicatorView = (ImageView) mIndicatorLayout.getChildAt(i);
//            if (i == position) {
//                indicatorView.setImageResource(R.drawable.icon_indicator_selected);
//            } else {
//                indicatorView.setImageResource(R.drawable.icon_indicator_normal);
//            }
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return GUIDE_ARRAY.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                ImageView imageView = createImageView();
                container.addView(imageView, 0);
                CoreImageLoader.getInstance().display(imageView, GUIDE_ARRAY[position]);
                if(position == GUIDE_ARRAY.length - 1){
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enterNextPage();

                        }
                    });
                }else{
                    imageView.setOnClickListener(null);
                }
                return imageView;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private ImageView createImageView() {
            ImageView view = new ImageView(GuideActivity.this);
            ViewGroup.LayoutParams bannerParams =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(bannerParams);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void enterNextPage() {
        // 设置不需要再次显示引导页
        SPManagerDefault.getInstance().putBoolean(SPKeys.KEY_NEED_SHOW_GUIDE, false);

        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finishActivity();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, GuideActivity.class);
        activity.startActivity(intent);
    }
}
