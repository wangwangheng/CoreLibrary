package com.droid.library.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.droid.library.app.mvp.IUI;
import com.droid.library.dialog.DialogHelper;
import com.xinye.lib.R;

import butterknife.ButterKnife;


/**
 * 所有Activity的基类
 *
 * @author wangheng
 */
public abstract class BaseActivity extends AppCompatActivity implements
        View.OnClickListener, IUI {

    private boolean isActivityDestroyed = true;
    private boolean isPaused = true;
    private boolean isStopped = true;

    private ProgressDialog mWaitDialog;

    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isActivityDestroyed = false;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        onBeforeSetContentLayout();

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        // 通过注解绑定控件
        ButterKnife.inject(this);

        mHandler = new Handler();
    }

    /**
     * 在setContentView方法调用之前回调这个方法，
     * 如果需要在setContentView之前执行一些行为，在这个方法是非常合适的
     */
    protected void onBeforeSetContentLayout() {
    }

    /**
     * setContentView方法会回调这个方法得到布局id，如果返回的id=0,不会自动设置布局
     *
     * @return 布局id
     */
    @LayoutRes
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isStopped = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStopped = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
    }

    @Override
    protected void onPause() {
        isPaused = true;
        super.onPause();
    }

    @Override
    protected void onStop() {

        isStopped = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        isActivityDestroyed = true;
        ButterKnife.reset(this);
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
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        if (mHandler == null || isActivityDestroyed() || isFinishing()) {
            return;
        }
        // 隐藏对话框，一定不要放到runnable里面~
        hideWaitDialog();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !isActivityDestroyed()) {
                    mWaitDialog = DialogHelper.getWaitDialog(BaseActivity.this, message,listener);
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
                if (!isActivityDestroyed()
                        && mWaitDialog != null
                        && mWaitDialog.isShowing()) {
                    mWaitDialog.dismiss();
                    mWaitDialog = null;
                }
            }
        });
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public boolean isActivityDestroyed() {
        return isActivityDestroyed;
    }

    @Override
    public boolean isFragmentDetached() {
        return isActivityDestroyed;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    @Override
    public boolean isFragmentHidden() {
        return !isActivityDestroyed;
    }

    @Override
    public boolean isVisibleToUser() {
        return !isPaused;
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v != null && isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] location = {0, 0};
            v.getLocationInWindow(location);
            int left = location[0],
                    top = location[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
