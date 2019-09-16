package com.king.hhczy.base.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.king.hhczy.common.result.ReqtBody;
import com.king.hhczy.common.util.Log;
import com.king.hhczy.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 项目名称:  vdc
 * 包:       com.gosun.myproj.framework.aspect
 * 类名称:    WebLogAspect.java
 * 类描述:    记录WEB请求日志.AOP拦截
 * 创建人:    Mr.XiHui
 * 创建时间:  2018/9/2
 */
@Aspect
@Component
@Slf4j//需要设置允许注解处理
public class WebLogAspect {

    private static final ThreadLocal<Long> START_TIME_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> REQUEST_MAP_THREAD_LOCAL = new ThreadLocal<>();

    // 两个..代表所有子目录，最后括号里的两个..代表所有参数 模糊写法*Sync(..)
//    @Pointcut("execution(public * com.king.hhczy.controller.*Controller.*(..))"+"|| execution(public * com.king.hhczy.controller.*Controller.*(..))")
    @Pointcut("execution(public * com.king.hhczy.controller.*Controller.*(..))")
    public void logPointCut() {}

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {

        START_TIME_THREAD_LOCAL.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Signature signature = joinPoint.getSignature();

        Object requestId = request.getAttribute("requestId");
        if (requestId==null) {
            requestId = request.getHeader("request-id");
        }
        String url = request.getRequestURL().toString();
        String remoteAddr = request.getRemoteAddr();
        String httpMethod = request.getMethod();
        String classMethod = signature.getDeclaringTypeName() + "." + signature.getName();

        Map<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("requestId", Optional.ofNullable(requestId).orElse(UUIDUtil.uuid()));
        requestMap.put("remoteAddr", remoteAddr);
        requestMap.put("url", url);
        requestMap.put("httpMethod", httpMethod);
        requestMap.put("classMethod", classMethod);

        Object[] args = joinPoint.getArgs();
        if (ObjectUtils.isEmpty(args)) {
            args = new Object[]{"no parameter，无入参"};
        }

        Object firstArg = args[0];

        if (firstArg instanceof ReqtBody) {
            requestMap.put("requestBody", firstArg);
        } else {
            Object[] clone = args.clone();
            for (int i = 0; i < clone.length; i++) {
                Object o = clone[i];
                if (o instanceof ServletRequest
                        || o instanceof ServletResponse
                        || o instanceof HttpSession) {
                    clone[i] = o.toString();//防止JsonUtils.toJsonStr操作报错
                }
            }
            requestMap.put("requestBody", clone);
        }

        if (Log.isInfoEnabled()) {
            Log.info("request : {} ", JSONObject.toJSONString(requestMap, SerializerFeature.WriteMapNullValue));
        }

        REQUEST_MAP_THREAD_LOCAL.set(requestMap);
    }

    @AfterReturning(returning = "ret", pointcut = "logPointCut()")
    // returning的值和doAfterReturning的参数名一致
    public void doAfterReturning(Object ret) {

        Map<String, Object> responseParams = new HashMap<>();

        Map<String, Object> requestMap = REQUEST_MAP_THREAD_LOCAL.get();
        responseParams.put("request-id",requestMap.get("requestId"));

        long costTime = System.currentTimeMillis() - START_TIME_THREAD_LOCAL.get();
        responseParams.put("costTime", costTime);   //处理日志

        responseParams.put("responseBody", ret);
        // 处理完请求，返回内容
        if (Log.isInfoEnabled()) {
            Log.info("response : {} ", JSONObject.toJSONString(responseParams, SerializerFeature.WriteMapNullValue));
        }

        START_TIME_THREAD_LOCAL.remove();
        REQUEST_MAP_THREAD_LOCAL.remove();
    }

    /**
     * 先进入此异常，在走全局异常
     * @param ex
     */
    @AfterThrowing(value = "logPointCut()", throwing = "ex")
    public void doAfterThrowing(Exception ex) {

        Map<String, Object> responseParams = new HashMap<>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, Object> requestMap = REQUEST_MAP_THREAD_LOCAL.get();
        if (!CollectionUtils.isEmpty(requestMap)) {
            responseParams.put("request-id",requestMap.get("requestId"));
        }
        request.setAttribute("requestMap", responseParams);
        request.setAttribute("startTime", START_TIME_THREAD_LOCAL.get());

        START_TIME_THREAD_LOCAL.remove();
        REQUEST_MAP_THREAD_LOCAL.remove();
    }
}

