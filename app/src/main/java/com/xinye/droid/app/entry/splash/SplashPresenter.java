package com.xinye.droid.app.entry.splash;


import com.droid.library.app.mvp.BasePresenter;
import com.xinye.droid.app.bean.ResponseList;
import com.xinye.droid.app.http.ServiceFactory;
import com.xinye.droid.app.http.callback.HttpCallback;

import java.util.List;

import retrofit2.Call;


/**
 * 闪屏页
 *
 * @author wangheng
 */
public class SplashPresenter extends BasePresenter<ISplashUI> {

    /**
     * 请求SplashImageUrl
     */
    public void requestImageUrl() {
        Call<ResponseList<String>> call = ServiceFactory.getAppService().getSplashImage();
        addCallToCache(call);
        call.enqueue(new HttpCallback<ResponseList<String>>() {
            @Override
            public void onResponse(ResponseList<String> body) {
                if(isUIDestroyed()){
                    return;
                }
                if(body == null || !body.isRequestSuccess()){
                    return;
                }
                List<String> list = body.getList();
                if(list != null && list.size() > 0) {
                    getUI().showSplashImage(list.get(0));
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
