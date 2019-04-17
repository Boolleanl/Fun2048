package com.boollean.fun2048.Utils;

import com.boollean.fun2048.Entity.BaseModel;
import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.Entity.RankUserEntity;
import com.boollean.fun2048.Entity.User;
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

    public static String userToJson(User user) {
        mGson = new Gson();
        String result = mGson.toJson(user);
        return result;
    }
}
