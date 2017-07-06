package com.xinye.droid.app.http.service;


import com.xinye.droid.app.bean.ResponseList;
import com.xinye.droid.app.bean.ResponseUpdate;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 全局相关Service
 *
 * @author wangheng
 */
public interface AppService {

    @POST("?s=/Home/Login/start_page")
    Call<ResponseList<String>> getSplashImage();

    /**
     * state int 1:成功 2:最新版本
     * message string state为0时需返回的错误说明信息
     * title string 版本号
     * apk string 地址
     *
     */
    @FormUrlEncoded
    @POST("?s=/Home/Index/version_update")
    Call<ResponseUpdate> checkUpdate(@Field("title") String versionCode);
}
