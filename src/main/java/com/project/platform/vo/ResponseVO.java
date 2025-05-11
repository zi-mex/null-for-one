package com.project.platform.vo;

public class ResponseVO<T> {
    private int code;
    private String msg;
    private T data;

    public ResponseVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseVO ok() {
        return new ResponseVO(200, "操作成功", null);
    }

    public static ResponseVO ok(Object data) {
        return new ResponseVO(200, "操作成功", data);
    }

    public static ResponseVO fail(int code, Object data) {
        return new ResponseVO(code, "操作失败", data);
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

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
