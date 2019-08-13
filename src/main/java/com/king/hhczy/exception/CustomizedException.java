package com.king.hhczy.exception;

/**
 * 自定义系统错误 customized:定制的
 * by ningjinxiang
 * 2019/03/25
 */
public class CustomizedException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;
    private String requestId;

    public CustomizedException(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }
    public CustomizedException(int code,String msg,String requestId) {
        this.msg = msg;
        this.code = code;
        this.requestId = requestId;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
