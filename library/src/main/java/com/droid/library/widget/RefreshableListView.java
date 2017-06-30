package com.droid.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;

import com.droid.library.log.Logger;


/**
 * 可刷新的自定义ListView
 *
 * @author wangheng
 */
public class RefreshableListView extends ListView implements AbsListView.OnScrollListener {

    private OnScrollListener mOuterOnScrollListener;

    private int firstVisibleItem;

    public RefreshableListView(Context context) {
        super(context);

        initSelf(context, null);
    }

    public RefreshableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSelf(context, attrs);
    }

    public RefreshableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSelf(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSelf(context, attrs);
    }

    private void initSelf(Context context, AttributeSet attrs) {
        super.setOnScrollListener(RefreshableListView.this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 解决系统事件分发 数组下标越界问题
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // 解决系统绘制 数组下标越界问题
        try {
            super.dispatchDraw(canvas);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void setOnScrollListener(OnScrollListener l) {
        if (l != RefreshableListView.this) {
            this.mOuterOnScrollListener = l;
        }
    }


    @Override
    public final void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mOuterOnScrollListener != null) {
            mOuterOnScrollListener.onScrollStateChanged(view, scrollState);
        }

        String state;
        if (scrollState == SCROLL_STATE_FLING) {
            state = "fling";
        } else if (scrollState == SCROLL_STATE_IDLE) {
            state = "idle";
            if (isLastItemVisible) {
//            if(isLastItemVisible && !loading){
                if (mOnPullUpRefreshListener != null) {
//                    loading = true;
                    mOnPullUpRefreshListener.onPullUpRefresh();
//                    Logger.i("wangheng","refresh =====222================");
                }
            }
        } else {
            state = "touch_scroll";
        }
        Logger.i("wangheng","scroll state :" + state);
    }

    private int previousFirstVisibleItem = -1;
    //    private boolean loading = false;
    boolean isLastItemVisible = false;

    @Override
    public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        this.firstVisibleItem = firstVisibleItem;

        if (mOuterOnScrollListener != null) {
            mOuterOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

//        Logger.i("wangheng"," first:" + firstVisibleItem
//                + ",visible:" + visibleItemCount
//                + ",total:" + totalItemCount
//                + ",last:" + getLastVisiblePosition());

        boolean isMotionUP = firstVisibleItem > previousFirstVisibleItem;

        boolean firstPlusVisibleEqualsTotal = firstVisibleItem + visibleItemCount == totalItemCount;

        isLastItemVisible = firstPlusVisibleEqualsTotal && getLastVisiblePosition() != -1;

//        if(isMotionUP && !loading && isLastItemVisible){
        if (isMotionUP && isLastItemVisible) {
            if (mOnPullUpRefreshListener != null) {
//                loading = true;
                mOnPullUpRefreshListener.onPullUpRefresh();
//                Logger.i("wangheng","refresh ==========111===========");
            }
        }

        previousFirstVisibleItem = firstVisibleItem;
    }

//    public void onPullUpRefreshListenerCompleted() {
//        this.loading = false;
//    }

    private OnPullUpRefreshListener mOnPullUpRefreshListener;

    public final void setOnPullUpRefreshListener(OnPullUpRefreshListener listener) {
        this.mOnPullUpRefreshListener = listener;
    }

    public interface OnPullUpRefreshListener {
        void onPullUpRefresh();
    }

    public int getFirstVisibleItem() {
        return firstVisibleItem;
    }
}
