package com.king.hhczy.base.constant;

public class ErrorCodeConstant {
    //成功
    public static final int SUCCESS_CODE = 0;
    //成功
    public static final String SUCCESS_MSG = "请求成功";
    // token有误
    public static final int TOKEN_ERROR = 5150100;
    //入参绑定异常
    public static final int PARAMS_ERROR = 5150101;
    //入参校验错误
    public static final int PARAMS_VALID_ERROR = 5150102;
    //请求数据库异常
    public static final int DATAACCESS_ERROR = 8150101;
    //请求服务异常
    public static final int SYSTEM_ERROR = 7150101;
    //请求服务异常（接口调用异常）
    public static final int GOVIDEO_CONNET_OUT_ERROR = 7150201;
    //请求服务异常（接口回参异常）
    public static final int GOVIDEO_PARAMS_ERROR = 7150202;
    // GET/POST方法请求错误
    public static final int NOT_SUPPORTED_ERROR = 6150101;
    //自定义异常
    public static final int CUSTOMIZED_ERROR = 9150101;
}
