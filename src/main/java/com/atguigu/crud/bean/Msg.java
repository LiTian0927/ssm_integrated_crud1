package com.atguigu.crud.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的返回信息的类
 */
public class Msg {
    /**
     * 状态码 100 -成功; 200- 失败
     */
    private int code;
    //提示信息
    private String msg;
    //用户要返回给浏览器的数据
    private Map<String, Object> extension = new HashMap<>();

    public Msg() {
    }

    public Msg(int code, String msg, Map<String, Object> extension) {
        this.code = code;
        this.msg = msg;
        this.extension = extension;
    }

    public Msg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Msg success() {
        Msg result = new Msg();
        result.setCode(100);
        result.setMsg("处理成功!");
        return result;
    }
    public static Msg fail() {
        Msg result = new Msg();
        result.setCode(200);
        result.setMsg("处理失败!");
        return result;
    }

    public Msg add(String key, Object value) {
        this.getExtension().put(key, value);
        return this;
    }

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

    public Map<String, Object> getExtension() {
        return extension;
    }

    public void setExtension(Map<String, Object> extension) {
        this.extension = extension;
    }
}
