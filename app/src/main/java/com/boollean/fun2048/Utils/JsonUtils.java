package com.boollean.fun2048.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {
    private static Gson mGson;

    public static List<RankUserEntity> toRankUserList(String jsonString) {
        Type type = new TypeToken<BaseModel<RankUserEntity>>() {
        }.getType();
        mGson = new Gson();
        BaseModel<RankUserEntity> baseModel = mGson.fromJson(jsonString, type);
        List<RankUserEntity> userList = baseModel.getSubjects();
        return userList;
    }

    public static List<MessageEntity> toMessageList(String jsonString) {
        Type type = new TypeToken<BaseModel<MessageEntity>>() {
        }.getType();
        mGson = new Gson();
        BaseModel<MessageEntity> baseModel = mGson.fromJson(jsonString, type);
        List<MessageEntity> messageList = baseModel.getSubjects();
        return messageList;
    }
}
