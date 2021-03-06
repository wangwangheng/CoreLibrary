package com.droid.library.app.mvp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.droid.library.app.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;

/**
 * Presenter的基类.
 *
 * @author wangheng
 */
public abstract class BasePresenter<U extends IUI> implements IPresenter {

    protected Handler mHandler = new Handler(Looper.getMainLooper());
    private U mUI;
    private BaseActivity mActivity;
    private ArrayList<WeakReference<Call>> mCallList;


    public BasePresenter() {
        mCallList = new ArrayList<>();
    }

    /**
     * 添加call
     *
     * @param call call
     */
    protected void addCallToCache(Call call) {
        if (isUIDestroyed()) {
            if (call != null) {
                call.cancel();
            }
            return;
        }
        if (mCallList != null) {
            mCallList.add(new WeakReference<>(call));
        }
    }

    private void clearAndCancelCallList() {
        if (mCallList != null) {
            Iterator<WeakReference<Call>> iterator = mCallList.iterator();
            if (iterator.hasNext()) {
                WeakReference<Call> wf = iterator.next();
                if (wf != null) {
                    Call call = wf.get();
                    if (call != null) {
                        call.cancel();
                    }
                }
            }
            mCallList.clear();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IUI> void init(BaseActivity activity, T ui) {
        this.mActivity = activity;
        this.mUI = (U) ui;
    }

    protected final U getUI() {
        return mUI;
    }

    protected final BaseActivity getActivity() {
        return mActivity;
    }

    @Override
    public void onUICreate(Bundle savedInstanceState) {
    }

    @Override
    public void onUIStart() {

    }

    @Override
    public void onUIResume() {

    }

    @Override
    public void onUIPause() {

    }

    @Override
    public void onUIStop() {

    }

    @Override
    public void onUIDestroy() {
        // 清空Call列表，并放弃Call请求
        clearAndCancelCallList();

        // clear handler
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        mUI = null;
        mActivity = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    /**
     * 耗时操作执行完之后，UI是否destroy的条件判断
     *
     * @return getUI()返回空，getActivity返回空或者isActivityDestroyed()
     */
    protected boolean isUIDestroyed() {
        return getUI() == null || getActivity() == null || getUI().isActivityDestroyed();
    }
}
