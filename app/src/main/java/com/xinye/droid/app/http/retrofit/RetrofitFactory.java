package com.xinye.droid.app.http.retrofit;



import com.droid.library.thirdplatform.gson.GsonManager;
import com.droid.library.utils.text.StringUtils;
import com.xinye.droid.app.http.Api;
import com.xinye.droid.user.UserManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit工厂
 *
 * @author wangheng
 */
public final class RetrofitFactory {

    // Api请求Retrofit
    private static Retrofit sApiRetrofit;

    // 文件上传Retrofit
    private static Retrofit sUploadRetrofit;

    private static String sToken = null;
    private static SimpleDateFormat sSimpleDateFormat;

    public static Retrofit createApiRetrofit(String token) {

        if (!needCreateRetrofit(sApiRetrofit)) {
            return sApiRetrofit;
        }

        sToken = token;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new RequestInterceptor())
                .addInterceptor(new LogInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        sApiRetrofit = new Retrofit.Builder()
                .baseUrl(Api.getApiUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(
                        GsonManager.getInstance().getGsonDefault()))
                .build();
        return sApiRetrofit;
    }

    public static Retrofit createUploadRetrofit(String token) {

        if (!needCreateRetrofit(sUploadRetrofit)) {
            return sUploadRetrofit;
        }

        sToken = token;

        OkHttpClient client = new OkHttpClient().newBuilder()
//                .addInterceptor(new RequestInterceptor())
                .addInterceptor(new LogInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        sUploadRetrofit = new Retrofit.Builder()
                .baseUrl(Api.getApiUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(
                        GsonManager.getInstance().getGsonDefault()))
                .build();
        return sUploadRetrofit;
    }

    private static boolean needCreateRetrofit(Retrofit retrofit) {
        if (retrofit == null) {
            return true;
        }

        if (sToken == null) {
            return true;
        }

        if (!sToken.equals(UserManager.getInstance().getToken())) {
            return true;
        }

        return false;
    }

    private static SimpleDateFormat getSimpleDateFormat() {
        if (null == sSimpleDateFormat) {
            synchronized (RetrofitFactory.class) {
                if (null == sSimpleDateFormat) {
                    sSimpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm");
                    sSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                }
            }
        }
        return sSimpleDateFormat;
    }

    public static void recycle() {
        sToken = null;
        sApiRetrofit = null;
        sUploadRetrofit = null;
    }

    /*********************************
     * 页面请求参数名和值
     ********************************/

    private static class RequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();


            String token = UserManager.getInstance().getToken();

            if (StringUtils.isNullOrEmpty(token)) {
                token = "";
            }

            HttpUrl url = request.url();
            HttpUrl.Builder urlBuilder = url.newBuilder();


//            String serviceName = url.queryParameter("service");
//            // 无论如何，都得让builder正常返回
//            try {
//                // 拼signature字符串
//                String datetime = getSimpleDateFormat().format(new Date());
//
//                String srcString = serviceName.toLowerCase()
//                        + datetime
//                        + SIGNATURE_FIXED_STRING
//                        + token;
//
//                String signature = MD5.getMD5(srcString);
////                Logger.i("wangheng","待加密串：" + srcString + ",加密之后：" + signature);
//                urlBuilder.addQueryParameter(PARAM_SIGNATURE, signature);
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            urlBuilder.addQueryParameter(PARAM_VERSION,VALUE_VERSION);
//            urlBuilder.addQueryParameter(PARAM_OS_TYPE,VALUE_OS_TYPE);
//
//            url = urlBuilder.build();
//
//            Request.Builder builder = request.newBuilder();
//            builder.url(url);
//
//
//
//            // imei
//            String deviceId = DeviceUtils.getImei(App.getInstance().getContext());
//            if(StringUtils.isNullOrEmpty(deviceId)){
//                deviceId = DeviceUtils.getAndroidId(App.getInstance().getContext());
//            }
//            builder.addHeader(PARAM_DEVICE_ID, deviceId);
//
//            if(!StringUtils.isNullOrEmpty(token)){
//                builder.addHeader(PARAM_TOKEN,token);
//            }else{
//                token = "";
//            }
//
//            builder.addHeader(PARAM_VERSION,VALUE_VERSION);
//            builder.addHeader(PARAM_OS_TYPE,VALUE_OS_TYPE);


//            request = builder.build();

            return chain.proceed(request);
        }
    }
}
