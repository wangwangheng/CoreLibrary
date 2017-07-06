package com.xinye.droid.app.http.callback;

/**
 * IHttpCallback
 *
 * @author wangheng
 */
interface IHttpCallback<T> {

    void onResponse(T body);

    void onFailure(Throwable t);

    void recycle();
}
