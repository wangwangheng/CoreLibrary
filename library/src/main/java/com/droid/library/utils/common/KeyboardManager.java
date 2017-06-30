package com.droid.library.utils.common;

import android.content.Context;

import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * 软键盘显示、隐藏管理器
 *
 * @author wangheng
 */
public class KeyboardManager {
    private KeyboardManager() {

    }

    /**
     * 显示软键盘
     *
     * @param view
     */
    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 判断软键盘是否显示
     *
     * @param context
     * @return
     */
    public static boolean isActive(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }
}
