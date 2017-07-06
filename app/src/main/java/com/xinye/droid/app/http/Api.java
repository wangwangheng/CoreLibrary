package com.xinye.droid.app.http;


import com.droid.library.app.App;
import com.xinye.droid.user.UserManager;

/**
 * 接口获取
 */
public class Api {


    /***********************************************************************************/
    /********************************* API请求接口地址 **********************************/
    /***********************************************************************************/
    // api接口地址线上
    private final static String RELEASE_API_URL = "http://5b.bloveapp.com/";
    // api接口地址测试
    private final static String DEBUG_API_URL = "http://5b.bloveapp.com/";

    // 注册协议地址
    public static final String URL_REGISTER_PROTOCOL = "http://5b.bloveapp.com/?s=/Home/Login/registration_protocol";


    public static final String URL_DETAIL = "http://5b.bloveapp.com/?s=/Home/Index/commentdetails";

    // 关于我们
    public static String getAboutUsUrl(){
        String id = UserManager.getInstance().getLoginUid();
        String token = UserManager.getInstance().getToken();
        return "http://5b.bloveapp.com/?s=/Home/Index/about_us" +
                "&id=" + id +
                "&token=" + token;
    }

    // 音频文稿
    public static String getAudioDraftUrl(int audioId){
        String id = UserManager.getInstance().getLoginUid();
        String token = UserManager.getInstance().getToken();
        return "http://5b.bloveapp.com/?s=/Home/Index/presentation" +
                "&id=" + id +
                "&token=" + token +
                "&audio_id=" + audioId;
    }

    // 支部详情
    public static String getBranchDetailUrl(String branchId){
        String id = UserManager.getInstance().getLoginUid();
        String token = UserManager.getInstance().getToken();

        return "http://5b.bloveapp.com/?s=/Home/Index/branchdetails" +
                "&id=" + id +
                "&token=" + token +
                "&branch_id=" + branchId;
    }


    public static String getPersonUrl(int personId){
        String id = UserManager.getInstance().getLoginUid();
        String token = UserManager.getInstance().getToken();
        return "http://5b.bloveapp.com/?s=/Home/Index/exampledetails" +
                "&id=" + id +
                "&token=" + token +
                "&example_id=" + personId;
    }


    public static String getBookReadUrl(int page, String url){
        return "http://5b.bloveapp.com/?s=/Home/Index/read" +
                "&page=" + page +
                "&url=" + url;
    }

    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_UID = "id";

    /**
     * 得到API接口地址
     *
     * @return API接口地址
     */
    public static String getApiUrl() {
        if (App.getInstance().isDebug()) {
            return DEBUG_API_URL;
        } else {
            return RELEASE_API_URL;
        }
    }
}
