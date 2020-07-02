package com.king.hhczy.entity.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
//入参(序列化)属性限定首字母大写
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class UserVO {
    //回参参(反序列化)属性限定首字母大写
    //用于转Json
    @JSONField(name = "Username")
    private String username;
    //用于接参
    @JsonProperty("Password")
    private String password;
}
