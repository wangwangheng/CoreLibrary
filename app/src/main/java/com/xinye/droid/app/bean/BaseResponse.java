package com.xinye.droid.app.bean;

import java.io.Serializable;

/**
 * BaseData
 *
 * @author wangheng
 */
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -7083881313754553830L;

    // 请求成功的state值
    private static final int STATE_SUCCESS = 1;
    private static final int STATE_FAILED = 0;
    private static final int STATE_TOKEN_TIMEOUT = -1;

    private int state;
    private String message;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * 是否请求成功
     *
     * @return 是否请求成功
     */
    public boolean isRequestSuccess() {
        return STATE_SUCCESS == state;
    }

    public boolean isTokenTimeout(){
        return state == STATE_TOKEN_TIMEOUT;
    }

    /**
     * 是否还有更多数据
     * @return 是否还有更多数据
     */
    public boolean hasMoreData(){
        return false;
    }
}
