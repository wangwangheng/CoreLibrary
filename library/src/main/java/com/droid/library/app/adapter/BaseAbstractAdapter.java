package com.droid.library.app.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.droid.library.app.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter基类
 *
 * @author wangheng
 */
public abstract class BaseAbstractAdapter<T> extends BaseSwipListAdapter {

    protected List<T> mList;
    protected LayoutInflater mLayoutInflater = null;
    private Holder holder;

    public BaseAbstractAdapter() {
        mLayoutInflater = App.getInstance().getLayoutInflater();
    }

    /**
     * 重设数据集
     *
     * @param list list
     */
    public synchronized void resetDataList(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public synchronized void removeItem(T item) {
        if (mList != null) {
            mList.remove(item);
            notifyDataSetChanged();
        }
    }

    public synchronized void removeItemAt(int position) {
        if (mList != null && position < mList.size() && position >= 0) {
            mList.remove(position);
            notifyDataSetChanged();
        }
    }

    public synchronized void replaceItem(int position, T item) {
        if (mList == null) {
            return;
        }
        if (position >= mList.size() || position < 0) {
            return;
        }
        mList.set(position, item);
    }

    public synchronized void clear() {
        if (null != mList && 0 != mList.size()) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public synchronized void addItem(T item) {
        if (mList != null) {
            mList.add(item);
        } else {
            mList = new ArrayList<>();
            mList.add(item);
        }

        notifyDataSetChanged();
    }

    public synchronized void addItemAt(int position, T item) {
        if (mList != null) {
            if (mList.size() == 0) {
                mList.add(item);
            } else {
                if (position >= getCount()) {
                    mList.add(item);
                } else if (position >= 0) {
                    mList.add(position, item);
                }
            }
        } else {
            mList = new ArrayList<>();
            mList.add(item);
        }

        notifyDataSetChanged();
    }

    /**
     * 追加列表
     *
     * @param list
     */
    public synchronized void appendList(List<T> list) {
        if (mList != null) {
            mList.addAll(list);
        } else {
            mList = list;
        }
        notifyDataSetChanged();
    }

    public  synchronized List<T> getItemList() {
        return mList;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public final T getItem(int position) {
        if (mList != null) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(getLayoutId(), parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        inflate(convertView, mList.get(position), position);
        return convertView;
    }

    /**
     * 通过ID得到控件
     *
     * @param resId view id
     * @return View
     */
    protected View findViewById(int resId) {
        return holder.findViewById(resId);
    }

    /**
     * 得到Item的布局的ID
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 完成布局的填充和返回
     *
     * @param convertView convertView
     * @param position    当前条目位置
     */
    protected abstract void inflate(View convertView, T t, int position);


    private static class Holder {

        private SparseArray<View> views = new SparseArray<>();

        private View convertView = null;

        private Holder(View convertView) {
            this.convertView = convertView;
        }

        public View findViewById(int resId) {
            View v = views.get(resId);
            if (v == null) {
                v = convertView.findViewById(resId);
                views.put(resId, v);
            }
            return v;
        }
    }
}
