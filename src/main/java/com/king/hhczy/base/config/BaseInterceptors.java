package com.king.hhczy.base.config;

import com.king.hhczy.common.util.JwtUtils;
import com.king.hhczy.common.util.Log;
import com.king.hhczy.common.util.RandomUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * 请求拦截，给请求提供token;日志拦截器，记录访问接口地址
 * WebMvcConfigurationSupport会让spring boot的自动配置失效,访问不了classpath:/static/下面的资源
 * https://blog.csdn.net/thekey1314/article/details/81044999
 *
 * @author ningjinxiang
 */
@Configuration
public class BaseInterceptors implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置拦截与不拦截的路径(根据设置顺序而执行先后)
        //静态资源不过滤
        //本地查询不需要北向接口登陆校验
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**")
                //排除在线文档的拦截
                .excludePathPatterns("/doc.html","/error")
                //排除测试接口
                .excludePathPatterns("/test-*")
                //去除对swagger api doc的拦截
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html", "/csrf/**");
    }

    /**
     * Token获取拦截器
     *
     * @author ningjinxiang
     */
    @Component
    private class TokenInterceptor implements HandlerInterceptor {
        private boolean OFF = true;// true关闭jwt令牌验证功能
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException, ServletException {
            //请求信息
            String requestId = Optional.ofNullable(request.getHeader("request-id")).orElse(RandomUtil.uuid());
            request.setAttribute("requestId",requestId);
            Log.info("request-id={}", requestId);
            Log.info("Method={}", request.getMethod());
            Log.info("RequestURL={}", request.getRequestURL());
            Log.info("Protocol={}", request.getProtocol());
            Log.info("CharacterEncoding={}", request.getCharacterEncoding());
            Log.info("srcHost:Port={}", request.getRemoteHost() + ":" + request.getRemotePort());
            //请求参数
            Log.info("QueryString= {}", request.getQueryString());
            String token = request.getHeader("Authorization");
            Log.info("preHandle:token="+token);

            if (OFF) {// 登陆直接放行
                return true;
            }
            // 从客户端请求头中获得令牌并验证
            String jwt = request.getHeader(JwtUtils.JWT_HEADER_KEY);
            Claims claims = this.validateJwtToken(jwt);
            if (null == claims) {
                // resp.setCharacterEncoding("UTF-8");
                response.sendError(403, "JWT令牌已过期或已失效");
                return false;
            } else {
                String newJwt = JwtUtils.copyJwt(jwt, JwtUtils.JWT_WEB_TTL);
                response.setHeader(JwtUtils.JWT_HEADER_KEY, newJwt);
            }
            return true;
        }
        /**
         * 验证jwt令牌，验证通过返回声明(包括公有和私有)，返回null则表示验证失败
         */
        private Claims validateJwtToken(String jwt) {
            Claims claims = null;
            try {
                if (null != jwt) {
                    claims = JwtUtils.parseJwt(jwt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return claims;
        }
    }

    /**
     * 跨域问题处理
     */
//    @Component
//    public class CommonIntercepter implements HandlerInterceptor {
//        @Override
//        public boolean preHandle(HttpServletRequest request,
//                                 HttpServletResponse response, Object handler) throws Exception {
//            //允许跨域,不能放在postHandle内
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            if (request.getMethod().equals("OPTIONS")) {
//                response.addHeader("Access-Control-Allow-Methods", "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH");
//                response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
//            }
//            return true;
//        }
//    }
}