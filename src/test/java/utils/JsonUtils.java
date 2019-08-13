package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonUtils {

	private final static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private final static ThreadLocal<SimpleDateFormat> THREAD_LOCAL_SIMPLE_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_PATTERN);
        }
    };

	public static String toString(Object obj) {
		return toString(obj, "");
	}

	public static String toString(Object obj, String nullStr) {
		if (obj == null) {
			return nullStr;
		}

		if (obj instanceof Timestamp || obj instanceof Date) {
			return THREAD_LOCAL_SIMPLE_DATE_FORMAT.get().format((Date) obj);
		}
		return obj.toString();
	}

	public static Map<String, Object> jsonToMap(String json) {
		return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
		});
	}
	
	public static List<Map<String, Object>> jsonToMapList(String json) {
		return JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	/**
	 * 显示空值字段
	 * @param obj
	 * @return
	 */
	public static String objectToString(Object obj) {
		return JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
	}
	
	public static <T> T stringToObject(String jsonString, Class<T> clz){
		return JSONObject.parseObject(jsonString, clz);
	}
}
