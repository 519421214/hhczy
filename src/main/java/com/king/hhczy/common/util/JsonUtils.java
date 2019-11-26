package com.king.hhczy.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtils {

    /**
     * null或空值也需要返回
     * @param obj 转化的类
     * @param writeMapNullValue 是否显示空值的字段
     * @return
     */
    public static String objectToString(Object obj,boolean writeMapNullValue){
        if (writeMapNullValue) {
            return JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        }
        return JSONObject.toJSONString(obj);
    }
    /**
     * 005.json字符串转换为map
     */
    public static <T> Map<String, Object> json2map(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue(jsonString, Map.class);
    }
}
