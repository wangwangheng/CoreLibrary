package com.xinye.droid.app.bean;

import java.io.Serializable;

/**
 * 用户
 *
 * @author wangheng
 */

public class User implements Serializable {
    private static final long serialVersionUID = -7701683343443782017L;


    /** 男 **/
    public static int SEX_FEMALE = 2;
    /** 女 **/
    public static int SEX_MALE = 1;


    private String id;
    private String name;
    private String phone;
    private String password;
    private boolean rememberPassword;
    private int sex;                // 1 男，2 女
    private String token;           // token
    private String birthtime;       // 生日
    private String logo;            // 头像


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRememberPassword() {
        return rememberPassword;
    }

    public void setRememberPassword(boolean rememberPassword) {
        this.rememberPassword = rememberPassword;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


    public String getBirthtime() {
        return birthtime;
    }

    public void setBirthtime(String birthtime) {
        this.birthtime = birthtime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }



    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", rememberPassword=" + rememberPassword +
                ", sex=" + sex +
                ", token='" + token + '\'' +
                ", birthtime='" + birthtime + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
