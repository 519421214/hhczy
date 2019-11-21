package com.king.hhczy.common.result;


/**
 *     4       04      1010
 *  错误类型 服务资源 细分错误码
 * ```
 *
 * 具体定义如下:
 *
 * #### 错误类型
 *
 * 错误类型定义该请求错误责权, 其中:
 *
 * - 1~3 为保留码, 一般不使用
 * - 4 用户权限错误, 使用于非法访问, 用户鉴权失败, 用户访问权限受限等
 * - 5 参数错误, 适用于参数错误, 参数缺省等
 * - 6 资源错误, 适用于访问资源不存在, 资源无法访问, 资源修改操作错误等
 * - 7 服务器错误, 适用于服务器内部错误等
 * - 8~9 占位码, 留待业务扩展使用
 */
public enum BaseResultCode {


    SUCCESS(0, "成功"),
    FAILED(-1, "失败"),

    UNKNOWN_ERROR(7040000, "未知错误"),

    INVALID_ACCESS(4040000, "非法访问"),

    INVALID_ACCESS_NOT_SUPPORTED_ERROR(4040001, "GET/POST请求错误"),

    NON_PARAMTER(5040000, "参数错误"),
    NON_PARAMTER_JSON_FORMAT(5040001, "入参JSON格式错误"),
    NON_PARAMTER_VALID_ERROR(5040002, "入参校验失败"),

    DATA_IS_NULL(6040000, "数据不存在"),

    SOCKET_ERROR(8040000, "远程接口请求错误"),
    SOCKET_ERROR_CONNET_OUT(8040001, "远程接口请求超时"),


    //业务错误码

      ;


    private int code;
    private String message;

    BaseResultCode(int code, String message) {
        this.code = code;
        this.message = message;

    }

    public BaseResultCode fillArgs(Object... args){
        this.message = String.format(this.message+":%s", args);
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
