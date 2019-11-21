package com.king.hhczy.entity.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
//入参(序列化)属性限定首字母大写
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class UserVO {
    //回参参(反序列化)属性限定首字母大写
    @JSONField(name = "Username")
    private String username;
}
