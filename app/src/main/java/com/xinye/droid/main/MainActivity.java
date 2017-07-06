package com.xinye.droid.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.droid.library.app.mvp.BaseMVPActivity;
import com.droid.library.app.mvp.IUI;
import com.droid.library.log.Logger;
import com.droid.library.toast.ToastUtils;
import com.droid.library.utils.BugFixedUtils;
import com.droid.library.widget.ScrollableViewPager;
import com.xinye.app.R;
import com.xinye.droid.app.bean.ResponseUpdate;
import com.xinye.droid.app.eventbus.MainEvent;
import com.xinye.droid.app.update.UpdateManager;
import com.xinye.droid.user.UserManager;
import com.xinye.droid.user.login.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.InjectView;

/**
 * 主界面
 *
 * @author wangheng
 */
public class MainActivity extends BaseMVPActivity<MainPresenter> implements IMainUI,
        RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";

    public static final int TAB_HOME = 0;
    public static final int TAB_HEAD_OFFICE = 1;
    public static final int TAB_PARTY_BRANCH = 2;
    public static final int TAB_STUDY = 3;
    public static final int TAB_MY = 4;
    public static final int COUNT_TAB = 5;
    private static final String KEY_VERSION_CHECKED = "keyVersionChecked";
    private static final int TIME_EXIT = 2000;
    private static final String KEY_ACTION = "keyAction";
    private static final int INVALID_ACTION = 0;
    private static final int ACTION_EXIT = 0x1001;
    private static final int ACTION_RETRY_LOGIN = 0x1002;
    private static final int ACTION_BACK_MAIN = 0x1003;
    @InjectView(R.id.vpMain)
    ScrollableViewPager mViewPager;
    @InjectView(R.id.rgMainTab)
    RadioGroup mTabRadioGroup;
    @InjectView(R.id.rbMainHome)
    RadioButton mTabHome;
    @InjectView(R.id.rbMainHeadOffice)
    RadioButton mTabHeadOffice;
    @InjectView(R.id.rbMainPartyBranch)
    RadioButton mTabPartyBranch;
    @InjectView(R.id.rbMainStudy)
    RadioButton mTabStudy;
    @InjectView(R.id.rbMainMy)
    RadioButton mTabMy;
    Handler mHandler = new Handler();
    private long mLastClickTime;
    // 升级对话框(不是真正的Dialog，所以需要手动处理)
    private Dialog mUpdateDialog;
    // 是否已经自动检查过更新
    private boolean isVersionChecked = false;

    /**
     * 退出Application
     *
     * @param activity activity
     */
    public static void exitApplication(Activity activity) {
        executeAction(activity, ACTION_EXIT);
    }

    /**
     * 重新尝试登录
     *
     * @param activity activity
     */
    public static void retryLogin(Activity activity) {
        executeAction(activity, ACTION_RETRY_LOGIN);
    }

    /**
     * 执行给定的操作
     *
     * @param activity 执行这个操作的activity
     * @param action   要执行的操作
     */
    private static void executeAction(Activity activity, int action) {
        if (null == activity) {
            return;
        }

        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(KEY_ACTION, action);
        activity.startActivity(intent);
        if (MainActivity.class != activity.getClass()) {
            activity.finish();
        }
    }
    public static void backMainClearTask(Activity activity) {
        if (null == activity) {
            return;
        }

        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_VERSION_CHECKED, isVersionChecked);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        int action = intent.getIntExtra(KEY_ACTION, INVALID_ACTION);
        if (ACTION_EXIT == action) {

            Logger.i("wangheng", "exit executed");

            finish();
//            UmengManager.getInstance().notifyUmengProcessKilled();
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
        } else if (ACTION_RETRY_LOGIN == action) {

            Logger.i("wangheng", "retry login action");
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else if (action == ACTION_BACK_MAIN) {
            Logger.i("wangheng", "back to main activity");
        } else if (!UserManager.getInstance().isLogin()) {
            Logger.i("wangheng", "retry login no action");
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

        } else {

            Logger.i("wangheng", "MainActivity default");
            if (!isVersionChecked) {
                // 检查新版本
                isVersionChecked = true;
                getPresenter().checkNewVersion();
            }
        }
    }

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isVersionChecked = savedInstanceState.getBoolean(KEY_VERSION_CHECKED, false);
        }
        handleIntent(getIntent());

        mTabRadioGroup.setOnCheckedChangeListener(MainActivity.this);

        mViewPager.addOnPageChangeListener(MainActivity.this);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));

        mViewPager.setCurrentItem(TAB_HOME, false);

        ActivityCompat.checkSelfPermission(MainActivity.this,"");

        EventBus.getDefault().register(MainActivity.this);
    }


    private static long previousLogoutMillis = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MainEvent event){
        if(isActivityDestroyed() || event == null){
            return;
        }
        if(event.getEvent() == MainEvent.EVENT_TOKEN_TIME_OUT){

            long current = System.currentTimeMillis();
            if(current - previousLogoutMillis < 2000){
                return;
            }
            previousLogoutMillis = current;

            ToastUtils.toastSystem(R.string.text_token_timeout,ToastUtils.LENGTH_SHORT);
            UserManager.getInstance().logout();
            retryLogin(MainActivity.this);
        }
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected IUI getUI() {
        return MainActivity.this;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbMainHome:
                mViewPager.setCurrentItem(TAB_HOME, false);
                break;
            case R.id.rbMainHeadOffice:
                mViewPager.setCurrentItem(TAB_HEAD_OFFICE, false);
                break;
            case R.id.rbMainPartyBranch:
                mViewPager.setCurrentItem(TAB_PARTY_BRANCH, false);
                break;
            case R.id.rbMainStudy:
                mViewPager.setCurrentItem(TAB_STUDY, false);
                break;
            case R.id.rbMainMy:
                mViewPager.setCurrentItem(TAB_MY, false);
                break;
            default:

                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (TAB_HOME == position) {
            if (!mTabHome.isChecked()) {
                mTabHome.setChecked(true);
            }
        } else if (TAB_HEAD_OFFICE == position) {
            if (!mTabHeadOffice.isChecked()) {
                mTabHeadOffice.setChecked(true);
            }
        } else if (TAB_PARTY_BRANCH == position) {
            if (!mTabPartyBranch.isChecked()) {
                mTabPartyBranch.setChecked(true);
            }
        } else if (TAB_STUDY == position) {
            if (!mTabStudy.isChecked()) {
                mTabStudy.setChecked(true);
            }
        } else if (TAB_MY == position) {
            if (!mTabMy.isChecked()) {
                mTabMy.setChecked(true);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(MainActivity.this);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
            mUpdateDialog.dismiss();
            mUpdateDialog = null;
        }

        if (null != mViewPager) {
            mViewPager.removeOnPageChangeListener(MainActivity.this);
        }

        BugFixedUtils.fixInputMethodManagerMemoryLeak(MainActivity.this);

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (System.currentTimeMillis() - mLastClickTime < TIME_EXIT) {

                finishActivity();
            } else {
                ToastUtils.toastInfo(MainActivity.this, R.string.text_exit_again);
                mLastClickTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showUpdateDialog(ResponseUpdate info) {
        if(info == null){
            return;
        }

        if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
            mUpdateDialog.dismiss();
            mUpdateDialog = null;
        }

        mUpdateDialog = UpdateManager.getInstance()
                .showUpdateDialog(MainActivity.this, info);
    }

    @Override
    public boolean isUpdateDialogShowing() {
        return mUpdateDialog != null && mUpdateDialog.isShowing();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
