package com.king.hhczy.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

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
}
