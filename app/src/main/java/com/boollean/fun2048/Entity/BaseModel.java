package com.boollean.fun2048.Entity;

import java.util.List;

/**
 * 解析Json数据依托的基类
 *
 * @param <T> 泛型类，由基类下的数据决定
 * @author Boollean
 */
public class BaseModel<T> {
    private int code;   //Json中包含的状态标记
    private String msg;     //Json中包含的信息
    private List<T> subjects;   //Json中包含的主要对象链表
    private T data;     //对象所属的类

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<T> subjects) {
        this.subjects = subjects;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
