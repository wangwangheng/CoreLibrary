package com.droid.library.app.emptyview;

import android.app.Activity;
import android.view.View;

import com.xinye.lib.R;


/**
 * 通用空View管理器
 *
 * @author wangheng
 */
public class CommonEmptyViewManager extends BaseEmptyViewManager<CallbackAdapter> {
    public CommonEmptyViewManager(CallbackAdapter callback) {
        super(callback);
    }

    @Override
    protected View createNoDataEmptyView(Activity context) {
        View view = getLayoutInflater().inflate(R.layout.empty_no_data, getEmptyView(context), false);
        return view;
    }

    @Override
    protected View createNetWorkErrorEmptyView(Activity context) {
        View view = getLayoutInflater().inflate(R.layout.empty_no_network, getEmptyView(context), false);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
