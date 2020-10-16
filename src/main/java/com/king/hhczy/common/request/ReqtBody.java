package com.king.hhczy.common.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 请求参数对象
 * 项目名称:  myproj
 * 包:        com.gosun.myproj.framework.message
 * 类名称:    ReqtBody.java
 * 创建人:
 * 创建时间:  2018/9/2
 */
//入参属性限定首字母大写
//@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class ReqtBody<T> implements Serializable {

    private static final long serialVersionUID = 4170534046017913803L;

    /**
     * 当前页码
     */
    private int pageIndex;

    /**
     * 每页行数
     */
    private int pageSize;
    private int count;
    /**
     * 除去以上通用参数外的具体请求参数
     */
    @NotNull(message = "请求体参数parameter不能为空")
    @Valid
    private T parameter;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReqtBody{");
        sb.append(", pageIndex=").append(pageIndex);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", count=").append(count);
        sb.append(", parameter=").append(parameter);
        sb.append('}');
        return sb.toString();
    }
}
