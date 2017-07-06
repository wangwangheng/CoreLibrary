package com.droid.library.app.mvp.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinye.lib.R;


/**
 * FooterView管理器.
 *
 * @author wangheng
 */
public class FooterManager {

    private LinearLayout mFooterView = null;
    private View mLoadingView;
    private ImageView mLoadingImage;
    private TextView mLoadingText;

    private View mNoMoreDataView;
    private TextView mNoMoreDataText;

    private Animation mLoadingImageAnimation;

    public FooterManager(Context context, ViewGroup viewParent) {
        init(context, viewParent);
    }

    private void init(Context context, ViewGroup viewParent) {
        mFooterView = new LinearLayout(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mLoadingView = inflater.inflate(R.layout.footer_loading, viewParent, false);
        mLoadingImage = (ImageView) mLoadingView.findViewById(R.id.footerLoadingImage);
        mLoadingText = (TextView) mLoadingView.findViewById(R.id.footerLoadingText);


        mNoMoreDataView = inflater.inflate(R.layout.footer_no_more_data, viewParent, false);
        mNoMoreDataText = (TextView) mNoMoreDataView.findViewById(R.id.footerNoMoreDataText);

        mLoadingImageAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_loading_image);

        mFooterView.addView(mLoadingView);
        mFooterView.addView(mNoMoreDataView);

        hideFooter();
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setLoadingText(String text) {
        mLoadingText.setText(text);
    }

    public void setLoadingTextSize(float textSize) {
        mLoadingText.setTextSize(textSize);
    }

    public void setLoadingTextColor(int color) {
        mLoadingText.setTextColor(color);
    }

    public void setLoadingImage(int resId) {
        mLoadingImage.setImageResource(resId);
    }

    public void setNoMoreDataText(String text) {
        mNoMoreDataText.setText(text);
    }

    public void setNoMoreDataTextSize(float textSize) {
        mNoMoreDataText.setTextSize(textSize);
    }

    public void setNoMoreDataTextColor(int color) {
        mNoMoreDataText.setTextColor(color);
    }

    public void showFooter(boolean isLastPage) {
        if (isLastPage) {
            mLoadingView.setVisibility(View.GONE);
            mNoMoreDataView.setVisibility(View.VISIBLE);
        } else {
            mLoadingView.setVisibility(View.VISIBLE);
            mLoadingImage.startAnimation(mLoadingImageAnimation);
            mNoMoreDataView.setVisibility(View.GONE);
        }
    }

    public void hideFooter() {
        mLoadingView.setVisibility(View.GONE);
        mNoMoreDataView.setVisibility(View.GONE);
    }
}
