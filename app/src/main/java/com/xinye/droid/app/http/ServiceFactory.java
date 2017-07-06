package com.xinye.droid.app.http;



import com.xinye.droid.app.http.retrofit.RetrofitFactory;
import com.xinye.droid.app.http.service.AppService;
import com.xinye.droid.app.http.service.MainService;
import com.xinye.droid.app.http.service.UploadService;
import com.xinye.droid.app.http.service.UserService;
import com.xinye.droid.user.UserManager;


/**
 * 接口工厂
 *
 * @author wangheng
 */
public final class ServiceFactory {

    private static String sToken = null;

    private static UploadService sUploadService;
    private static AppService sAppService;
    private static UserService sUserService;
    private static MainService sMainService;

    public static void recycle() {
        sUploadService = null;
        sAppService = null;
        sUserService = null;
        sMainService = null;
        sToken = null;
    }


    public static UploadService getUploadService() {
        if (!needCreateService(sUploadService)) {
            return sUploadService;
        }
        sToken = UserManager.getInstance().getToken();
        sUploadService = RetrofitFactory.createUploadRetrofit(sToken).create(UploadService.class);
        return sUploadService;
    }
    public static MainService getMainService() {
        if (!needCreateService(sMainService)) {
            return sMainService;
        }
        sToken = UserManager.getInstance().getToken();
        sMainService = RetrofitFactory.createApiRetrofit(sToken).create(MainService.class);
        return sMainService;
    }
    public static AppService getAppService() {
        if (!needCreateService(sAppService)) {
            return sAppService;
        }
        sToken = UserManager.getInstance().getToken();
        sAppService = RetrofitFactory.createApiRetrofit(sToken).create(AppService.class);
        return sAppService;
    }
    public static UserService getUserService() {
        if (!needCreateService(sUserService)) {
            return sUserService;
        }
        sToken = UserManager.getInstance().getToken();
        sUserService = RetrofitFactory.createApiRetrofit(sToken).create(UserService.class);
        return sUserService;
    }


    private static boolean needCreateService(Object service) {

        if (service == null || sToken == null) {
            return true;
        }
        String token = UserManager.getInstance().getToken();

        return !sToken.equals(token);

    }

}
