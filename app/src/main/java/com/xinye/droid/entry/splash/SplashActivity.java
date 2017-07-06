package com.xinye.droid.entry.splash;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.library.app.mvp.BaseMVPActivity;
import com.droid.library.app.mvp.IUI;
import com.droid.library.app.storage.sp.SPManagerDefault;
import com.droid.library.thirdplatform.imageloader.CoreImageLoader;
import com.xinye.app.R;
import com.xinye.droid.entry.guide.GuideActivity;
import com.xinye.droid.app.storage.sp.SPKeys;
import com.xinye.droid.main.MainActivity;
import com.xinye.droid.user.UserManager;
import com.xinye.droid.user.login.LoginActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 闪屏页
 *
 * @author wangheng
 */
public class SplashActivity extends BaseMVPActivity<SplashPresenter>
            implements ISplashUI {

    private static final String TAG = "SplashActivity";

    private static final int SPLASH_TIME = 2;
    private static final int COUNT_DOWN_STEP = 1000;

    private int mCurrentCountDown = SPLASH_TIME;

    @InjectView(R.id.ivSplash)
    ImageView mImageView;

    @InjectView(R.id.tvSplashSkip)
    TextView mSkipTextView;

    private Handler mHandler = null;
//    private String mFormat;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        mHandler = new Handler(Looper.getMainLooper());

//        mFormat = App.getInstance().getString(R.string.splash_skip);
//        mSkipTextView.setText(String.format(mFormat,mCurrentCountDown));

        CoreImageLoader.getInstance().display(mImageView, R.drawable.splash);

//        getPresenter().requestImageUrl();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mHandler != null) {
            mHandler.postDelayed(mCountdownTask, COUNT_DOWN_STEP);
        }
    }

    @Override
    protected void onStop() {
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onStop();
    }

//    private Runnable mCountdownTask = new Runnable() {
//        @Override
//        public void run() {
//            if(--mCurrentCountDown <= 0) {
//                showNextPage();
//            }else{
//                mSkipTextView.setText(String.format(mFormat,mCurrentCountDown));
//                if(mHandler != null) {
//                    mHandler.postDelayed(mCountdownTask, COUNT_DOWN_STEP);
//                }
//            }
//        }
//    };
    private Runnable mCountdownTask = new Runnable() {
        @Override
        public void run() {
            if(--mCurrentCountDown <= 0) {
                showNextPage();
            }else{
                if(mHandler != null) {
                    mHandler.postDelayed(mCountdownTask, COUNT_DOWN_STEP);
                }
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
            }
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    protected IUI getUI() {
        return SplashActivity.this;
    }

    @OnClick({R.id.tvSplashSkip})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSplashSkip:
                showNextPage();
                break;
            default:

                break;
        }
    }

    private void showNextPage() {
        if(mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        boolean needShowGuide = SPManagerDefault.getInstance().getBoolean(SPKeys.KEY_NEED_SHOW_GUIDE, true);

        if (needShowGuide) {
            GuideActivity.launch(SplashActivity.this);
        } else {
            if(UserManager.getInstance().isLogin()) {
                MainActivity.launch(SplashActivity.this);
            }else{
                LoginActivity.launch(SplashActivity.this);
            }
        }
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }

    @Override
    public void showSplashImage(String imagePath) {
        if(isActivityDestroyed() || mImageView == null){
            return;
        }
        CoreImageLoader.getInstance().display(mImageView,imagePath);
    }
}
