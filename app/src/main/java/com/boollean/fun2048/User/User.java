package com.boollean.fun2048.User;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * 用户类。
 * Created by Boollean on 2019/2/28.
 */
public class User {
    private static final String TAG = "User";

    private static volatile User instance = null;

    private String mName;    //用户名
    private String mPassword;    //密码
    private int mGender; //性别
    private Bitmap mAvatar;  //头像
    private Uri mBitmapPath;    //头像的定位符

    public User(String name, String password, int gender, Bitmap avatar) {
        mName = name;
        mPassword = password;
        mGender = gender;
        mAvatar = avatar;
    }

    public static User getInstance() {
        synchronized (User.class) {
            if (instance == null) {
                instance = new User(null, null, 0, null);
            }
        }
        return instance;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
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
}
