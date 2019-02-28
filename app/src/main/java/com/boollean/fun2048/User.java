package com.boollean.fun2048;

import android.widget.ImageView;

public class User {
    private static final String TAG = "User";

    private static volatile User instance = null;

    private String name;

    private int gender;

    private ImageView avatar;

    public static User getInstance() {
        synchronized (User.class) {
            if (instance == null) {
                instance = new User(null, 0, null);
            }
        }
        return instance;
    }

    public User(String name, int gender, ImageView avatar) {
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

    public ImageView getAvatar() {
        return avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }
}
