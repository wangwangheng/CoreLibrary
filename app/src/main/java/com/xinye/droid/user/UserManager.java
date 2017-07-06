package com.xinye.droid.user;


import com.droid.library.app.storage.sp.SPManager;
import com.droid.library.thirdplatform.gson.GsonManager;
import com.droid.library.utils.text.StringUtils;
import com.xinye.droid.app.bean.User;

/**
 * 用户管理
 *
 * @author wangheng
 */
public class UserManager {


    private static final String SP_NAME = "sp_user";
    /**当前登录的用户的Key*/
    private static final String KEY_USER = "keyUser";

    private User mUser;

    private UserManager() {
    }

    public static UserManager getInstance() {
        return Generator.INSTANCE;
    }

    public String getLoginUid() {
        return getUser().getId();
    }

    public boolean isLogin() {
        return !StringUtils.isNullOrEmpty(getToken());
    }

    public String getToken() {
        return getUser().getToken();
    }

    private static final class Generator {
        private static final UserManager INSTANCE = new UserManager();
    }

    public void saveUser(User user){
        if(user == null){
            mUser = new User();
            SPManager.get(SP_NAME).remove(KEY_USER);
        }else{
            mUser = user;
            String json = GsonManager.getInstance().getGsonDefault().toJson(user);
            SPManager.get(SP_NAME).putString(KEY_USER,json);
        }
    }

    public User getUser(){
        if(mUser == null) {
            String str = SPManager.get(SP_NAME).getString(KEY_USER, null);
            if (StringUtils.isNullOrEmpty(str)) {
                mUser = new User();
            }else {
                mUser = GsonManager.getInstance().getGsonDefault().fromJson(str, User.class);
            }

            if (mUser == null) {
                mUser = new User();
            }
            return mUser;
        }
        return mUser;
    }

    public void logout(){
        User user = getUser();

        User outUser = new User();
        outUser.setPhone(user.getPhone());
        outUser.setPassword(user.getPassword());
        outUser.setRememberPassword(user.isRememberPassword());
        saveUser(outUser);
    }
    public void clearUser(){
        saveUser(null);
    }
}
