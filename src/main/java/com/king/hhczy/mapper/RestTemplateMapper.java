package com.king.hhczy.mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.king.hhczy.common.util.UUIDUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 整合数据传送HttpEntity，简化代码步骤
 * 提供各种多态方法调用，满足不同的需求
 *
 * @author by ningjinxiang 20180913
 */
@Component
public class RestTemplateMapper {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;
    //响应码 key
    private String[] resultCodeKey = {"code","retcode"};
    //响应数据体 key
    private String[] resultDataKey = {"data"};
    //响应信息 key
    private String[] resultMsgKey = {"msg","retmsg"};
    //响应成功码
    private int successCode = 0;

    /**
     * post 统一请求
     *
     * @param requestUrl
     * @return
     */
    public JSONObject postResult(String requestUrl, Object params, Object... msg) {

        MultiValueMap headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Request-Id", UUIDUtil.uuid());

        JSONObject data = null;
        try {
            data = restTemplate.postForObject(requestUrl, new HttpEntity(params, headers), JSONObject.class);
            //返回结果码
            return resultDeal(data, requestUrl, msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("远程接口调用异常" + e.getMessage());
        }
    }

    /**
     * post 统一请求
     *
     * @param requestUrl
     * @return
     */
    public JSONObject postFormResult(String requestUrl, Map<String, Object> params, Object... msg) {

        try {
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();  //表单数据结构
            if (params != null) {
                params.forEach((k, v)->{
                    if (v instanceof List) {
                        for (Object val : ((List) v)) {
                            form.add(k, val);
                        }
                    }else {
                        form.add(k, v);
                    }
                });
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Request-Id", UUIDUtil.uuid());
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            JSONObject data = null;

            data = restTemplate.postForObject(requestUrl, new HttpEntity(form, headers), JSONObject.class);
            //返回结果码
            return resultDeal(data, requestUrl, msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("远程接口调用异常" + e.getMessage());
        }
    }

    /**
     * get 统一请求
     *
     * @param requestUrl
     * @return
     */
    public JSONObject getResult(String requestUrl, String[] params) {
        String paramsStr = "";
        if (!ObjectUtils.isEmpty(params)) {
            paramsStr = Arrays.stream(params).reduce("", (x, y) -> x + "&" + y);
        }

        JSONObject data = null;
        try {
            //先以String形式返回在转JSON，（直接转的话）避免回参编码问题转JSON报错
            data = JSON.parseObject(restTemplate.getForObject(requestUrl + "?{1}", String.class, paramsStr));
            //返回结果码
            return resultDeal(data, requestUrl, params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("远程接口调用异常" + e.getMessage());
        }
    }

    private JSONObject resultDeal(JSONObject data, Object... params) {

        Function<String[],String> getKey = x->Arrays.stream(x).filter(y -> data.get(y) != null).findAny().orElse(UUIDUtil.uuid());

        int resultCode = data.getIntValue(getKey.apply(resultCodeKey));
        if (resultCode == successCode) {
            return Optional.ofNullable(data.getJSONObject(getKey.apply(resultDataKey))).orElse(new JSONObject());
        } else {
            logger.error("远程接口{}回参失败：{} {}", params[0], params.length > 1 ? params[1] : "", data.getString(getKey.apply(resultMsgKey)));
//            logger.error("远程接口调用回参失败：{}[{}]",data.getString(resultMsgKey),JSONObject.toJSONString(params));
//            throw new RuntimeException("远程接口调用回参失败：" + data.getString(resultMsgKey));
            return null;
        }
    }
}
