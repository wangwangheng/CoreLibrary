package com.xinye.droid.main;


import com.droid.library.app.mvp.IUI;
import com.xinye.droid.app.bean.ResponseUpdate;

/**
 * 主界面UI层协议
 *
 * @author wangheng
 */
interface IMainUI extends IUI {

    /**
     * 显示更新对话框
     *
     * @param info 新版本信息
     */
    void showUpdateDialog(ResponseUpdate info);

    /**
     * 更新对话框是否正在显示
     *
     * @return 更新对话框是否正在显示
     */
    boolean isUpdateDialogShowing();
}
