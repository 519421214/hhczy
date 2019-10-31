package com.king.hhczy.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求响应对象
 * 项目名称:  myproj
 * 包:        com.gosun.myproj.framework.message
 * 类名称:    RespBody.java
 * 创建人:
 * 创建时间:  2018/9/2
 */
@Data
public class RespBody<T> implements Serializable {

    private static final long serialVersionUID = 7996354349506576057L;

    private String requestId;
    /**
     * 接口执行结果
     */
    private int code;

    /**
     * 响应给前台的消息
     */
    private String msg;

    /**
     * 响应给前台的数据
     */
    private T data;

//    public RespBody() {
//        this.code = BaseResultCode.SUCCESS.getCode();
//        this.msg = BaseResultCode.SUCCESS.getMessage();
//    }

    public void result(BaseResultCode baseResultCode) {
        this.code = baseResultCode.getCode();
        this.msg = baseResultCode.getMessage();
    }
}
