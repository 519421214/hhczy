package com.king.hhczy.common.util;

import com.king.hhczy.base.constant.CacheConstants;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 摘要认证工具
 *
 * @author ningjinxiang
 */
@Component
public class AbstractUtils {
    private final static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AbstractUtils.class);
    //认证nonce的key
    private final String ABSTRACT_NONCE_KEY = CacheConstants.INIT_CACHE_PREFIX + "abstract:nonce:key";
    @Autowired
    private RedissonClient redissonClient;

    private String realm = "REALM";
    private String qop = "auth";
    //nonce最大有效时间
    private final int nonceValidTime = 30;

    public boolean authentication(HttpServletRequest request, HttpServletResponse response, String password) {

        String authorization = request.getHeader("Authorization");
        RMapCache<String, String> nonces = redissonClient.getMapCache(ABSTRACT_NONCE_KEY);

        if (authorization != null && !"".equals(authorization)) {
            Map<String, String> map = this.stringToMap(authorization.replaceFirst("Digest\\s+", ""));
            String nonce = map.get("nonce");
            if (StringUtils.isEmpty(nonce) || nonces.get(nonce) == null) {
                responseAbstractHeader(response);
                return false;
            }
            Function<String, String> strVal = x -> Optional.ofNullable(map.get(x)).orElse("");
            String method = request.getMethod();
            String username = map.get("username");
//            String realm = strVal.apply("realm");
            String uri = strVal.apply("uri");
            String nc = strVal.apply("nc");
            String cnonce = strVal.apply("cnonce");
//            String qop = strVal.apply("qop");
            //客户端传过来的摘要
            String responseFromClient = map.get("response");
            //MD5计算
            String a1 = username + ":" + realm + ":" + password;
            String a2 = DigestUtils.md5DigestAsHex(a1.getBytes());
            String a3 = DigestUtils.md5DigestAsHex((method + ":" + uri).getBytes());
            //服务器计算出的摘要
            String a4 = a2 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + a3;
            String a5 = DigestUtils.md5DigestAsHex(a4.getBytes());
            //两者摘要相同，即验证成功
            if (a5 != null && a5.equals(responseFromClient)) {
//                业务逻辑。。。
                //两者摘要不相同，验证失败
                LOGGER.info("摘要认证成功");
                return true;
            } else {
//                业务逻辑。。。
                LOGGER.info("摘要认证失败");
                return false;
            }
        } else {
            responseAbstractHeader(response);
            return false;
        }
    }

    /**
     * 响应摘要头
     * @param response
     * @return
     */
    public void responseAbstractHeader(HttpServletResponse response) {
        Log.warn("摘要认证失败，响应摘要头");
        RMapCache<String, String> nonces = redissonClient.getMapCache(ABSTRACT_NONCE_KEY);
        //设置最大有效时间
        String nonce = UUID.randomUUID().toString().replace("-", "");
        nonces.put(nonce, "", nonceValidTime, TimeUnit.SECONDS);

        StringBuilder sb = new StringBuilder();
        sb.append("Digest ");
        sb.append("realm").append("=\"" + realm + "\",");
        sb.append("qop").append("=\"" + qop + "\",");
        sb.append("nonce").append("=\"").append(nonce).append("\",");
//            sb.append("opaque").append("=\"").append(UUIDUtil.uuid()).append("\"");
        response.setHeader("WWW-authenticate", sb.toString());
        Log.info("authorization:" + sb);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//指定返回状态
        Log.warn("摘要头通过header响应完毕");
    }

    /**
     * 等号类string转Map
     *
     * @param equalsStr
     * @return
     */
    private Map stringToMap(String equalsStr) {
        if (StringUtils.isEmpty(equalsStr.trim())) {
            return null;
        }
        Map result = new HashMap<>();
        Arrays.stream(equalsStr.replaceAll("[\"'\\s+]", "").split(",")).forEach(x -> {
            String[] splitVal = x.replaceFirst("=", "￥@￥").split("￥@￥");
            String mapVal = "";
            if (splitVal.length > 1) {
                mapVal = splitVal[1];
            }
            result.put(splitVal[0], mapVal);
        });
        return result;
    }
    /**
     * 等号类string通过key找value
     * @param equalsStr
     * @return
     */
    private static String stringFindValueByKey(String equalsStr,String key){
        if (StringUtils.isEmpty(equalsStr)||StringUtils.isEmpty(key)) {
            return null;
        }
        String result = Arrays.stream(equalsStr.replaceAll("[\"'\\s+]", "").split(",")).
                map(y -> y.replaceFirst("=", "￥@￥").split("￥@￥")).filter(z -> key.equals(z[0]) && z.length > 1).findAny().map(i -> i[1]).orElse(null);
        return result;
    }
    /**
     * 获取username
     * @return
     */
    public String getUsername(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        Log.info("Authorization:{}", authorization);
        if (StringUtils.isEmpty(authorization)) {
            Log.warn("Authorization 为空值");
            return null;
        }
        String noDigestStr = authorization.replaceFirst("Digest\\s+", "");
        String username = this.stringFindValueByKey(noDigestStr, "username");
        return username;
    }
}
