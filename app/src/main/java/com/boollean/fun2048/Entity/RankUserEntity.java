package com.boollean.fun2048.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * 排行榜用户基类
 *
 * @author Boollean
 */
public class RankUserEntity {
    @SerializedName("position")
    private int mPosition;      //排名
    @SerializedName("name")
    private String mName;   //用户名
    @SerializedName("gender")
    private int mGender; //性别
    @SerializedName("score")
    private int mScore; //分数

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
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
