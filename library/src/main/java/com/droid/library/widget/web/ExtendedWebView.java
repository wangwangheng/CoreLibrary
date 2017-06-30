package com.droid.library.widget.web;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import com.droid.library.log.Logger;


/**
 * see http://stackoverflow.com/a/9925980. <br/>
 *
 * @author see http://stackoverflow.com/a/9925980. <br/>
 */
public class ExtendedWebView extends WebView {

    public ExtendedWebView(Context context) {
        super(context);
    }

    public ExtendedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param direction -1 页面从上往下走。
     * @return 能否上下滚动
     */
    public boolean canScrollVertical(int direction) {
        final int offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if(range == 0) {
            return false;
        } else {
            return (direction < 0) ? (offset > 0) : (offset < range - 1);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    float mLastMotionY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    mLastMotionY = event.getY();
                    getParent().getParent().requestDisallowInterceptTouchEvent(true);
                } catch(Throwable e) {
                    e.printStackTrace();
                    Logger.e(getClass().getSimpleName(),"ExtendedWebView Exception:", e);
                }
                break;

            case MotionEvent.ACTION_MOVE: {

                try {
                    float direction = mLastMotionY - event.getY();
                    mLastMotionY = event.getY();

                    Logger.e(getClass().getSimpleName(),"scroll" + getScrollY() + "   direction:" + direction);

                    if((getScrollY() == 0 && direction < - 5)) {
                        // 获得VerticalViewPager的实例
                        getParent().getParent().requestDisallowInterceptTouchEvent(false);
                        ((View)getParent().getParent().getParent()).onTouchEvent(event);
                    } else {
                        getParent().getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } catch(Throwable e) {
                    e.printStackTrace();
                    Logger.e(getClass().getSimpleName(),"ExtendedWebView Exception:", e);
                }
            }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                try {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                } catch(Throwable e) {
                    e.printStackTrace();
                    Logger.e(getClass().getSimpleName(),"ExtendedWebView Exception:", e);
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }
}
