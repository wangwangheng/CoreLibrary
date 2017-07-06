package com.xinye.droid.app.http.service;


import com.xinye.droid.app.bean.BaseResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 上传服务
 *
 * @author wangheng
 */

public interface UploadService {

    @Multipart
    @POST("?s=/Home/Index/modify_data")
    Call<BaseResponse> requestUpdateUserInfo(@Part("id") RequestBody id,
                                             @Part("token") RequestBody token,
                                             @Part("name") RequestBody name,
                                             @Part("sex") RequestBody sex,
                                             @Part MultipartBody.Part file);

//    @Multipart
//    @POST("?s=/Home/Login/register")
//    Call<ResponseRegister> register(
//            @Part("phone") RequestBody phone,
//            @Part("pwd") RequestBody password,
//            @Part("name") RequestBody name,
//            @Part("identity") RequestBody identity,
//            @Part("sex") RequestBody sex,
//            @Part("birthtime") RequestBody birthday,
//            @Part("company_id") RequestBody companyId,
//            @Part("partybranch_id") RequestBody partyBranchId,
//            @Part MultipartBody.Part photoUrl);

}
