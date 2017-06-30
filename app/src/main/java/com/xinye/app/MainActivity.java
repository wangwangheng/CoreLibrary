package com.xinye.app;
import android.app.Activity;
import android.os.Bundle;

import com.droid.library.toast.ToastUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToastUtils.toastInfo(MainActivity.this, R.string.app_name);
    }
}
