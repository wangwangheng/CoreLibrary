package com.droid.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;

import com.xinye.lib.R;


/**
 * 2014年10月8日 下午8:41:58 <br/>
 * @author wangheng
 */
public class NestedLineGridView extends LinearLayout {

    /**
     * Item的水平间距 *
     */
    private int mRowSpacing = 0;

    private int mColumnSpacing = 0;

    private int mColumnCount = 1;

    /**
     * View持有的单击监听器 *
     */
    private OnItemClickListener onItemClickListener = null;

    public NestedLineGridView(Context context) {
        super(context);
        init(context, null);
    }

    public NestedLineGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NestedLineGridView);
        mRowSpacing = ta.getDimensionPixelSize(R.styleable.NestedLineGridView_rowSpacing, 0);
        mColumnSpacing = ta.getDimensionPixelSize(R.styleable.NestedLineGridView_columnSpacing, 0);
        mColumnCount = ta.getInteger(R.styleable.NestedLineGridView_columnCount, 1);
        ta.recycle();
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    /**
     * setAdapter:设置Adapter. <br/>
     *
     * @param adapter
     * @author wangheng
     */
    public void setAdapter(Adapter adapter) {
        removeAllViews();
        int count = adapter.getCount();

        int rowsCount;
        int modOfCountAndColumnCount = count % mColumnCount;
        if (modOfCountAndColumnCount != 0) {
            rowsCount = count / mColumnCount + 1;
        } else {
            rowsCount = count / mColumnCount;
        }

        LayoutParams rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rowParams.bottomMargin = mColumnSpacing;

        LayoutParams lastRowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        for (int row = 0; row < rowsCount; row++) {

            LinearLayout rowView = new LinearLayout(getContext());
            rowView.setOrientation(HORIZONTAL);
            if (row != rowsCount - 1) {
                rowView.setLayoutParams(rowParams);
            } else {
                rowView.setLayoutParams(lastRowParams);
            }

            for (int column = 0; column < mColumnCount; column++) {
                int position = row * mColumnCount + column;
                if (position >= adapter.getCount()) {
                    break;
                }
                View v = adapter.getView(position, rowView);
                v.setClickable(true);
                v.setOnClickListener(new MyOnClickListener(position, adapter.getItemId(position)));

                if (column == mColumnCount - 1) {
                    LayoutParams params = (LayoutParams) v.getLayoutParams();
                    if (params == null) {
                        params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
                    }
                    params.width = 0;
                    params.weight = 1;
                    rowView.addView(v, params);
                } else {
                    LayoutParams params = (LayoutParams) v.getLayoutParams();
                    if (params == null) {
                        params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
                    }
                    params.width = 0;
                    params.weight = 1;
                    params.rightMargin = mRowSpacing;
                    rowView.addView(v, params);
                }
            }

            // 处理最后一行不够mColumnCount的情况：添加两个空白View
            if(row == rowsCount - 1){
                int num = mColumnCount - rowView.getChildCount();
                for(int i = 0;i < num;i++){
                    Space space = new Space(getContext());

                    LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
                    params.weight = 1;

                    if (i == mColumnCount - 1) {
                        rowView.addView(space, params);
                        params.rightMargin = 0;
                    } else {
                        params.rightMargin = mRowSpacing;
                    }
                    rowView.addView(space, params);
                }
            }
            addView(rowView);
        }
    }

    /**
     * setOnItemClickListener:设置当View的Item被单击的时候的监听器<br/>
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getRowSpacing() {
        return mRowSpacing;
    }

    public void setRowSpacing(int mRowSpacing) {
        this.mRowSpacing = mRowSpacing;
    }

    public int getColumnSpacing() {
        return mColumnSpacing;
    }

    public void setColumnSpacing(int mColumnSpacing) {
        this.mColumnSpacing = mColumnSpacing;
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int mColumnCount) {
        this.mColumnCount = mColumnCount;
    }


    public interface OnItemClickListener {

        /**
         * onItemClick:当LineGridView的任一个Item被点击的时候将回调这个方法<br/>
         *
         * @param parent   就是这个LineGridView
         * @param view     被点击的具体的Item的View
         * @param position 被点击的View在数据集合中的位置
         * @param id       被点击的Item的Id
         */
        void onItemClick(View parent, View view, int position, long id);
    }

    private class MyOnClickListener implements OnClickListener {

        /**
         * 点击的position *
         */
        private int position = -1;

        /**
         * 点击的Item的id *
         */
        private long id = -1;

        private MyOnClickListener(int position, long id) {
            this.position = position;
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(
                        NestedLineGridView.this,
                        NestedLineGridView.this.getChildAt(position), position, id);
            }

        }

    }

    /**
     * LineGridView的适配器都要实现这个协议
     * @author wangheng
     */
    public interface Adapter {
        /**
         * 数据集合的个数<br/>
         */
        int getCount();

        /**
         * 具体的数据集中的position对应的数据 <br/>
         *
         * @param position 在数据集中的位置
         */
        Object getItem(int position);

        /**
         * getItemId:指定position的Item的id <br/>
         */
        long getItemId(int position);

        /**
         * getView:得到指定位置的View <br/>
         */
        View getView(int position, ViewGroup parent);
    }
}
