package com.droid.library.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droid.library.app.mvp.IUI;
import com.droid.library.dialog.DialogHelper;
import com.xinye.lib.R;


/**
 * Fragment基类
 *
 * @author wangheng
 */
public abstract class BaseFragment extends Fragment implements
        View.OnClickListener, IUI {

    private boolean isPaused = true;
    private boolean isStopped = true;
    private boolean isDestroyed = true;
    private boolean isFragmentDetached = true;
    private boolean isHidden = true;
    private boolean isVisibleToUser = false;

    private ProgressDialog mWaitDialog;
    private Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isFragmentDetached = false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isFragmentDetached = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyed = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        isStopped = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
    }

    @Override
    public void onPause() {
        isPaused = true;
        super.onPause();
    }

    @Override
    public void onStop() {
        isStopped = true;
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        hideWaitDialog();
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mHandler != null) {
                        mHandler.removeCallbacksAndMessages(null);
                        mHandler = null;
                    }
                }
            });
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        isFragmentDetached = true;
        hideWaitDialog();
        super.onDetach();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        isHidden = hidden;
        super.onHiddenChanged(hidden);
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    @Override
    public boolean isActivityDestroyed() {
        return isDestroyed;
    }

    @Override
    public boolean isFragmentDetached() {
        return isFragmentDetached;
    }


    @Override
    public boolean isFragmentHidden() {
        return isHidden;
    }

    @Override
    public boolean isVisibleToUser() {
        return isVisibleToUser;
    }

    /**
     * @param isVisibleToUser 是否对用户可见
     * @deprecated 绝对不要调用这个方法，否则可能导致卡死
     */
    @Override
    public final void setUserVisibleHint(final boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isActivityDestroyed()) {
                        return;
                    }
                    onUserVisibleHint(isVisibleToUser);
                }
            });
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * setUserVisibleHint 通过Handler#post的方式调用
     *
     * @param isVisibleToUser 是否对用户可见
     */
    protected void onUserVisibleHint(boolean isVisibleToUser) {

    }

    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void showWaitDialog(DialogInterface.OnDismissListener listener) {
        showWaitDialog(R.string.text_loading,listener);
    }

    @Override
    public void showWaitDialog(int resid,DialogInterface.OnDismissListener listener) {
        showWaitDialog(getString(resid),listener);
    }

    @Override
    public void showWaitDialog(final String message, final DialogInterface.OnDismissListener listener) {
        if (mHandler == null) {
            return;
        }
        // 隐藏对话框，一定不要放到runnable里面~
        hideWaitDialog();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isDetached() && !isActivityDestroyed() && !isHidden() && getActivity() != null) {
                    mWaitDialog = DialogHelper.getWaitDialog(getActivity(), message,listener);
                    mWaitDialog.setMessage(message);
                    mWaitDialog.show();
                }
            }
        });
    }

    @Override
    public void hideWaitDialog() {
        if (mHandler == null) {
            return;
        }
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (!isDetached() && !isActivityDestroyed()
                        && mWaitDialog != null && mWaitDialog.isShowing()) {
                    mWaitDialog.dismiss();
                    mWaitDialog = null;
                }
            }
        });
    }

    /**
     * 初始化View
     *
     * @param view
     */
    protected void initView(View view) {

    }

    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void finishActivity() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
