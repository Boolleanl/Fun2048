package com.boollean.fun2048.Utils;

import com.boollean.fun2048.Entity.BaseModel;
import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.Entity.RankUserEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Json字段处理类。
 *
 * @author Boollean
 */
public class JsonUtils {
    private static Gson mGson;

    /**
     * 将Json转换为排行榜用户对象链表
     * @param jsonString 需要转换的Json对象字段
     * @return 转换后生成的对象链表
     */
    public static List<RankUserEntity> toRankUserList(String jsonString) {
        Type type = new TypeToken<BaseModel<RankUserEntity>>() {
        }.getType();
        mGson = new Gson();
        BaseModel<RankUserEntity> baseModel = mGson.fromJson(jsonString, type);
        return baseModel.getSubjects();
    }

    /**
     * 将Json转换为留言板对象链表
     * @param jsonString 需要转换的Json对象字段
     * @return 转换后生成的对象链表
     */
    public static List<MessageEntity> toMessageList(String jsonString) {
        Type type = new TypeToken<BaseModel<MessageEntity>>() {
        }.getType();
        mGson = new Gson();
        BaseModel<MessageEntity> baseModel = mGson.fromJson(jsonString, type);
        return baseModel.getSubjects();
    }
}
