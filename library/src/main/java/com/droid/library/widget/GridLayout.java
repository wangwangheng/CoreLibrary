package com.droid.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xinye.lib.R;


/**
 * View
 */
public class GridLayout extends LinearLayout {

    private static final int ORIENTATION_HORIZONTAL = 1;
    private static final int ORIENTATION_VERTICAL = 2;
    private static final int ORIENTATION_DEFAULT = ORIENTATION_HORIZONTAL;
    private int mOrientation = ORIENTATION_DEFAULT;
    private Drawable mDivider = null;
    private Adapter mAdapter = null;
    private OnItemClickListener mOnItemClickListener;

    public GridLayout(Context context) {
        super(context);
        init(context,null);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        int orientation = 1;
        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GridLayout);

            mOrientation = ta.getInt(R.styleable.GridLayout_gridLayoutOrientation,ORIENTATION_DEFAULT);

            if(mOrientation == ORIENTATION_HORIZONTAL){
                super.setOrientation(HORIZONTAL);
            }else{
                super.setOrientation(VERTICAL);
            }

            mDivider = ta.getDrawable(R.styleable.GridLayout_gridLayoutDivider);
            if(mDivider != null){
                mDivider.setBounds(0,0,mDivider.getIntrinsicWidth(),mDivider.getIntrinsicHeight());
            }

            ta.recycle();
        }
        setGravity(Gravity.CENTER);
    }

    @Override
    @Deprecated
    public final void setOrientation(int orientation) {

    }

    public void setAdapter(Adapter adapter){
        mAdapter = adapter;
        removeAllViews();
        if(adapter == null){
            return;
        }
        int count = mAdapter.getCount();
        for(int i = 0;i < count;i++){
            // itemView
            final View view = mAdapter.getView(GridLayout.this,i);
            view.setLayoutParams(getItemLayoutParams(view,count));

            addView(view);
            // divider
            if(mDivider != null && i < count - 1){
                View divider = getDividerView();
                if(divider != null) {
                    addView(divider);
                }
            }
            // clickListener
            final int position = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onItemClickListener(GridLayout.this,view,position);
                    }
                }
            });
        }
    }

    private View getDividerView(){
        if(mDivider == null){
            return null;
        }
        ImageView divider = new ImageView(getContext());
        LayoutParams lp = null;
        if(mOrientation == ORIENTATION_HORIZONTAL){
            lp = new LayoutParams(mDivider.getIntrinsicWidth(),LayoutParams.MATCH_PARENT);
        }else{
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, mDivider.getIntrinsicHeight());
        }
        divider.setLayoutParams(lp);
        divider.setScaleType(ImageView.ScaleType.FIT_XY);
        divider.setImageDrawable(mDivider);
        return divider;
    }

    private LayoutParams getItemLayoutParams(View view,int count){
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if(lp == null){
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if(mOrientation == ORIENTATION_HORIZONTAL){
            lp.width = 0;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }else{
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = 0;
        }
        lp.weight = 1;
        return lp;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public interface Adapter{
        int getCount();
        View getView(GridLayout parent, int position);
    }

    public interface OnItemClickListener{
        void onItemClickListener(GridLayout parent, View itemView, int position);
    }
}
