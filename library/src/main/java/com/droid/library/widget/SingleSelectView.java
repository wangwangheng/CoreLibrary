package com.droid.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinye.lib.R;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;


/**
 * 单选View
 *
 * @author wangheng
 */
public class SingleSelectView extends LinearLayout implements OnWheelScrollListener {

    private WheelView mMonthView;
    private OnDateChangedListener mOnDateChangedListener;

    public SingleSelectView(Context context) {
        super(context);
        init(context,null);
    }

    public SingleSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SingleSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SingleSelectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
    }

    public void setItems(String[] list){
        removeAllViews();
        mMonthView = new WheelView(getContext());
        LayoutParams monthParams = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
        monthParams.weight = 1;
        mMonthView.setLayoutParams(monthParams);

        // mMonthView
        mMonthView.setViewAdapter(new DateArrayAdapter(getContext(), list));
        mMonthView.setCurrentItem(0);

        mMonthView.addScrollingListener(this);

        mMonthView.setWheelBackground(R.drawable.transparent);
        mMonthView.setWheelForeground(R.drawable.date_view_foreground);

        int startColor = Color.parseColor("#AFFFFFFF");
        int endColor = Color.parseColor("#3FFFFFFF");

        mMonthView.setShadowColor(startColor,startColor,endColor);

        addView(mMonthView);
    }

    public int getCurrentItem(){
        if(mMonthView != null){
            return mMonthView.getCurrentItem();
        }
        return -1;
    }



    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        if(mOnDateChangedListener != null){
            mOnDateChangedListener.onDateChanged(wheel.getCurrentItem());
        }

    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items) {
            super(context, items);
            setTextSize(18);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
//            if (currentItem == currentValue) {
//                view.setTextColor(0xFF0000F0);
//            }else{
//                view.setTextColor(Color.RED);
//            }

            view.setTextColor(Color.BLACK);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            View view = super.getItem(index, cachedView, parent);
            view.setPadding(view.getPaddingLeft(),5,view.getPaddingRight(),5);
            return view;
        }
    }



    public void setOnDateChangedListener(OnDateChangedListener listener){
        this.mOnDateChangedListener = listener;
    }


    public interface OnDateChangedListener{
        void onDateChanged(int position);
    }

}
