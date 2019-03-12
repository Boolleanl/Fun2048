package com.boollean.fun2048.User;

import android.graphics.Bitmap;

/**
 * 用户类。
 * Created by Boollean on 2019/2/28.
 */
public class User {
    private static final String TAG = "User";

    private static volatile User instance = null;

    private String name;    //用户名
    private int gender; //性别
    private Bitmap avatar;  //头像

    public User(String name, int gender, Bitmap avatar) {
        this.name = name;
        this.gender = gender;
        this.avatar = avatar;
    }

    public static User getInstance() {
        synchronized (User.class) {
            if (instance == null) {
                instance = new User(null, 0, null);
            }
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
