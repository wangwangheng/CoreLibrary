package com.droid.library.app.emptyview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.droid.library.app.App;
import com.xinye.lib.R;


/**
 * 空View管理基类
 *
 * @param <T>
 */
public abstract class BaseEmptyViewManager<T extends BaseCallback> implements OnClickListener {

    protected T callback;
    private FrameLayout mEmptyView = null;
    private View mNoDataEmptyView = null;
    //    private View mServerErrorEmptyView;
    private View mNetworkErrorEmptyView = null;
    //    private View mDataFormatErrorEmptyView;
    private View mOtherErrorEmptyView;

    public BaseEmptyViewManager(T callback) {
        this.callback = callback;
    }

    public T getCallback() {
        return callback;
    }

    protected LayoutInflater getLayoutInflater() {
        return (LayoutInflater) App.getInstance().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public final ViewGroup getEmptyView(Activity context) {
        if (mEmptyView == null) {
            mEmptyView = new FrameLayout(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mEmptyView.setLayoutParams(params);
        }
        return mEmptyView;
    }

    public void addEmptyView(Activity activity, ViewGroup parent) {
        if (parent == null) {
            return;
        }
        parent.addView(getEmptyView(activity));
    }

    public void addEmptyViewToFirst(Activity activity, ViewGroup parent) {
        if (parent == null) {
            return;
        }
        parent.addView(getEmptyView(activity), 0);
    }

    /**
     * showNoDataEmptyView:显示无数据View. <br/>
     *
     * @param context wangheng
     */
    public void showNoDataEmptyView(Activity context) {
        hideAllEmptyView();
        if (mNoDataEmptyView == null) {
            mNoDataEmptyView = createNoDataEmptyView(context);
            if (mNoDataEmptyView != null) {
                mEmptyView.addView(mNoDataEmptyView);
            }
        }
        if (mNoDataEmptyView != null) {
            mNoDataEmptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * createNoDataEmptyView:创建无数据EmptyView. <br/>
     *
     * @param context context
     * @return view
     */
    protected abstract View createNoDataEmptyView(Activity context);

    /**
     * showServerErrorEmptyView:显示服务器错误View. <br/>
     *
     * @param context context
     */
    public void showServerErrorEmptyView(Activity context) {
        // TODO 待实现
    }

    /**
     * showNetworkErrorEmptyView:显示网络错误EmptyView. <br/>
     *
     * @param context context
     */
    public void showNetworkErrorEmptyView(Activity context) {
        hideAllEmptyView();

        if (mNetworkErrorEmptyView == null) {
            mNetworkErrorEmptyView = createNetWorkErrorEmptyView(context);
            if (mNetworkErrorEmptyView != null) {
                mEmptyView.addView(mNetworkErrorEmptyView);
            }
        }
        if (mNetworkErrorEmptyView != null) {
            mNetworkErrorEmptyView.setVisibility(View.VISIBLE);
        }
    }

    protected abstract View createNetWorkErrorEmptyView(Activity context);


    /**
     * showDataFormatErrorEmptyView:显示数据格式错误View. <br/>
     *
     * @param context context
     */
    public void showDataFormatErrorEmptyView(Activity context) {
        // TODO 待实现
    }

    /**
     * 显示其他错误EmptyView. <br/>
     *
     * @param context context
     */
    public void showOtherErrorEmptyView(Activity context) {
        // TODO 待实现
        hideAllEmptyView();
        if (mOtherErrorEmptyView == null) {
            mOtherErrorEmptyView = createOtherErrorEmptyView(context);
            if (mOtherErrorEmptyView != null) {
                mEmptyView.addView(mOtherErrorEmptyView);
            }
        }
        if (mOtherErrorEmptyView != null) {
            mOtherErrorEmptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 其他错误EmptyView
     *
     * @param context context
     * @return
     */
    protected View createOtherErrorEmptyView(Activity context) {
        View view = getLayoutInflater().inflate(R.layout.empty_white, getEmptyView(context), false);
        return view;
    }

    /**
     * hideAllEmptyView:隐藏所有的EmptyView. <br/>
     *
     * @author wangheng
     */
    public void hideAllEmptyView() {
        if (null == mEmptyView) {
            return;
        }
        int count = mEmptyView.getChildCount();
        for (int i = 0; i < count; i++) {
            mEmptyView.getChildAt(i).setVisibility(View.GONE);
        }
    }
}
