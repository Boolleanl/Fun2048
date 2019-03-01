package com.boollean.fun2048;

import android.graphics.Bitmap;

public class User {
    private static final String TAG = "User";

    private static volatile User instance = null;

    private String name;

    private int gender;

    private Bitmap avatar;

    public static User getInstance() {
        synchronized (User.class) {
            if (instance == null) {
                instance = new User(null, 0, null);
            }
        }
        return instance;
    }

    public User(String name, int gender, Bitmap avatar) {
        this.name = name;
        this.gender = gender;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
