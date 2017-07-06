package com.xinye.droid.app.http.callback;



import com.xinye.droid.app.bean.BaseResponse;
import com.xinye.droid.app.eventbus.MainEvent;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 回调适配器
 *
 * @author wangheng
 */
public abstract class HttpCallback<T> implements Callback<T>, IHttpCallback<T> {

//    private HttpRequest<T> httpRequest;
//    private HttpResponse<T> httpResponse;

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
//        httpRequest = new HttpRequest<>();
//        httpResponse = new HttpResponse<>();
        T body = response.body();
        onResponse(body);
        if(body != null && body instanceof BaseResponse){
            boolean isTimeout = ((BaseResponse)body).isTokenTimeout();
            if(isTimeout){
                EventBus.getDefault().post(MainEvent.create(MainEvent.EVENT_TOKEN_TIME_OUT));
            }
        }

    }


    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        onFailure(t);
    }

    public void recycle() {

    }

//    public HttpRequest<T> getHttpRequest() {
//        return httpRequest;
//    }
//
//    public HttpResponse<T> getHttpResponse() {
//        return httpResponse;
//    }
}
