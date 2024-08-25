package com.nestplayer.lib.result;

import java.util.Map;

public class NestResult<T> {
    private int code;
    private String message;
    private long time;
    private T result;
    private Map<String, Object> extResult; //扩展参数

    public static <T> NestResult<T> success(T t) {
        NestResult<T> nestResult = new NestResult<>();
        nestResult.setCode(200);
        nestResult.setResult(t);
        nestResult.setMessage("success");
        return nestResult;
    }

    public static <T> NestResult<T> error(String message) {
        return error(400, message);
    }

    public static <T> NestResult<T> error(int code, String message) {
        NestResult<T> nestResult = new NestResult<>();
        nestResult.setCode(code);
        nestResult.setResult(null);
        nestResult.setMessage(message);
        return nestResult;
    }

    public boolean isSuccess() {
        return code == 200;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Map<String, Object> getExtResult() {
        return extResult;
    }

    public void setExtResult(Map<String, Object> extResult) {
        this.extResult = extResult;
    }

}
