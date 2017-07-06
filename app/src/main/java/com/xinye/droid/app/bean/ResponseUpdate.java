package com.xinye.droid.app.bean;

import java.io.Serializable;

/**
 * 服务器更新回调
 *
 * @author wangheng
 */

public class ResponseUpdate extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -3494739790099386395L;

    /**
     * state int 1:成功 2:最新版本
     * message string state为0时需返回的错误说明信息
     * title string 版本号
     * apk string 地址
     */

    public static final int STATE_ALREADY_NEWEST = 2;

    private String title;
    private String apk;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    /**
     * 是否已经时最新版本
     * @return 是否是最新版本
     */
    public boolean isLastVersion(){
        return STATE_ALREADY_NEWEST == getState();
    }
}
