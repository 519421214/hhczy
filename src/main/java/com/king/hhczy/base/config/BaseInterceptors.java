package com.king.hhczy.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求拦截，给请求提供token;日志拦截器，记录访问接口地址
 * WebMvcConfigurationSupport会让spring boot的自动配置失效,访问不了classpath:/static/下面的资源
 * https://blog.csdn.net/thekey1314/article/details/81044999
 *
 * @author ningjinxiang
 */
@Configuration
@Slf4j
public class BaseInterceptors implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置拦截与不拦截的路径(根据设置顺序而执行先后)
        //静态资源不过滤
        //本地查询不需要北向接口登陆校验
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
    }

    /**
     * Token获取拦截器
     *
     * @author ningjinxiang
     */
    @Component
    private class TokenInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            String token = request.getHeader("Authorization");
//            String token = null;
//            if ("GET".equals(request.getMethod())) {
//                token = request.getParameter("Token");
//            }else {
//                token = request.getHeader("Authorization");
//            }
            //token认证，先写死 todo 可以从他们缓存到redis里面获取（参照sac）
//            if ("GosuncnToken".equals(token)) {
//            if (StringUtils.hasText(token)) {//先不做本地token校验 服务不公开20190330
//                //一些不需要走govideo认证的接口 begin
//                String requestURI = request.getRequestURI();
//                StringBuffer localInterface = new StringBuffer();
//                localInterface.append("/getAllArea").append("/getAreaDevice").append("/getBuildingIpcOfArea").append("/getIpcOfBuilding");
//                //一些不需要走govideo认证的接口 end
//                if (localInterface.toString().contains(requestURI.substring(requestURI.lastIndexOf("/")))|| govideoMapper.loginGovideo(token, false)) {
//                    return true;
//                }
////                throw new CustomizedException(ErrorCodeConstant.TOKEN_ERROR, "govide-token获取失败");
//            }
//            throw new CustomizedException(ErrorCodeConstant.TOKEN_ERROR, "token认证失败");
            return true;
        }
    }

}