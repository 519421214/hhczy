package com.king.hhczy.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author ningjinxiang
 * 20190701
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        //Response status code 4XX or 5XX to the client.
        //解决400、500等错误直接抛出异常问题
        restTemplate.setErrorHandler(new ThrowErrorHandler());
        return restTemplate;
    }
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);//ms   请求超时时间
        factory.setConnectTimeout(15000);//ms   连接超时时间
        return factory;
    }
    public class ThrowErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            // 返回false表示不管response的status是多少都返回没有错
            // 这里可以自己定义那些status code你认为是可以抛Error
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // 这里面可以实现你自己遇到了Error进行合理的处理
        }
    }
}