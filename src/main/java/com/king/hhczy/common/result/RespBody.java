package com.king.hhczy.common.result;

import com.king.hhczy.base.constant.ErrorCodeConstant;
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

    /**
     * 请求识别唯一ID,
     * <p>
     * 透传前台传递过来的requestId
     */
    private String requestId;

    /**
     * 接口执行结果
     */
    private int code= ErrorCodeConstant.SUCCESS_CODE;

    /**
     * 响应给前台的消息
     */
    private String msg=ErrorCodeConstant.SUCCESS_MSG;

    /**
     * 响应给前台的数据
     */
    private T data;

}
