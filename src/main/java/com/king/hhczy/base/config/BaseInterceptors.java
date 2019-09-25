package com.king.hhczy.base.config;

import com.king.hhczy.common.util.Log;
import com.king.hhczy.common.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        @Value("${abcd}")
        private String abcd;
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            //请求信息
            String requestId = Optional.ofNullable(request.getHeader("request-id")).orElse(UUIDUtil.uuid());
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
            return false;
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