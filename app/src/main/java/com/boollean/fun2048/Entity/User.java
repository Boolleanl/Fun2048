package com.boollean.fun2048.Entity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

/**
 * 用户类。
 *
 * @author Boollean
 */
public class User {

    private static volatile User instance = null;

    private String mName;    //用户名
    private String mPassword;    //密码
    private int mGender; //性别
    private Bitmap mAvatar;  //头像
    private Uri mBitmapPath;    //头像的定位符
    private int bestScore4;     //4*4模式最高分
    private int bestScore5;     //5*5模式最高分
    private int bestScore6;     //6*6模式最高分

    public User(String name, String password, int gender, Bitmap avatar) {
        mName = name;
        mPassword = password;
        mGender = gender;
        mAvatar = avatar;
    }

    /**
     * 获取唯一User对象
     *
     * @return User对象
     */
    public static User getInstance() {
        if (instance == null) {
            synchronized (User.class) {
                if (instance == null) {
                    instance = new User(null, null, 0, null);
                }
            }
        }
        return instance;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }

    public static void deleteThisUser() {
        User.instance = null;
        Log.i("aboutF", "name" + instance.getName());
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public int getGender() {
        return mGender;
    }

    public void setGender(int gender) {
        this.mGender = gender;
    }

    public Bitmap getAvatar() {
        return mAvatar;
    }

    public void setAvatar(Bitmap avatar) {
        mAvatar = avatar;
    }

    public Uri getBitmapPath() {
        return mBitmapPath;
    }

    public void setBitmapPath(Uri bitmapPath) {
        mBitmapPath = bitmapPath;
    }

    public int getBestScore4() {
        return bestScore4;
    }

    public void setBestScore4(int bestScore4) {
        this.bestScore4 = bestScore4;
    }

    public int getBestScore5() {
        return bestScore5;
    }

    public void setBestScore5(int bestScore5) {
        this.bestScore5 = bestScore5;
    }

    public int getBestScore6() {
        return bestScore6;
    }

    public void setBestScore6(int bestScore6) {
        this.bestScore6 = bestScore6;
    }
}
