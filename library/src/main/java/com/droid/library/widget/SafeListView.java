package com.droid.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 安全的ListView
 * <p>
 * fix bug:ListView random IndexOutOfBoundsException
 * <p>
 * <a href="http://stackoverflow.com/questions/8431342/listview-random-indexoutofboundsexception-on-froyo">StackOverflow</a>
 *
 * @author wangheng
 */
public class SafeListView extends ListView {
    public SafeListView(Context context) {
        super(context);
    }

    public SafeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SafeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SafeListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
