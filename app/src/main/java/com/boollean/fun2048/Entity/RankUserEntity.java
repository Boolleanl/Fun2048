package com.boollean.fun2048.Entity;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class RankUserEntity {
    @SerializedName("position")
    private int mPosition;      //排名

    private Bitmap mAvatar;  //头像
    @SerializedName("avatar_path")
    private String mBitmapPath;    //头像的定位符
    @SerializedName("name")
    private String mName;   //用户名
    @SerializedName("gender")
    private int mGender; //性别
    @SerializedName("score")
    private int mScore;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public Bitmap getAvatar() {
        return mAvatar;
    }

    public void setAvatar(Bitmap avatar) {
        mAvatar = avatar;
    }

    public String getBitmapPath() {
        return mBitmapPath;
    }

    public void setBitmapPath(String bitmapPath) {
        mBitmapPath = bitmapPath;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getGender() {
        return mGender;
    }

    public void setGender(int gender) {
        mGender = gender;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }
}
