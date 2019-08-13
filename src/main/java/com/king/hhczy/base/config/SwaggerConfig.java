package com.king.hhczy.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collection;

/**
 * Swagger2 配置
 * 访问方式说明：
 * 1,启动gmvcs-cas。
 * 2,浏览器访问 =>
 * 升级管理
 * http://ip:port/project-name/doc.html
 * http://ip:port/project-name/swagger-ui.html#/
 * <p>
 * Created with IntelliJ IDEA
 * Created by zhaohuan
 * DATE : 2017/8/22.
 * TIME : 16:09
 * EMAIL : 791934554@qq.com
 * 步骤：
 * 1、引包：
 *     //swagger 在线API文档
 *     implementation 'io.springfox:springfox-swagger2:2.9.2'
 *     //swagger 在线api 文档
 *     implementation 'io.springfox:springfox-swagger-ui:2.9.2'
 *      //swagger-doc 在线API文档，支持导出doc
 *     implementation 'com.github.xiaoymin:swagger-bootstrap-ui:1.8.8'
 * 2、引该配置文件
 * 3、controller顶头标签：@Api(value = "数据层接口", description = "数据层接口")
 * 4、方法标签： @ApiOperation(value = "注销认证")
 * 5、请求体标签：@ApiModelProperty(value = "开始时间",required=true)
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)              //文档类型
                .genericModelSubstitutes(Collection.class)              //返回值为Collection<T>时,将其“类型化”
                .genericModelSubstitutes(ResponseEntity.class)
                .apiInfo(apiInfo())                                     //文档信息
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.king.hhczy.controller"))    //注解扫描路径
                .build();
//                .pathMapping("/gmvcs/uap");                       //项目接口前缀 springfox2.7.0以上无须配置
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("《hhczy》RESTful APIs手册")
//            .description(
//                new StringBuffer(">>更多接口相关信息请联系平台研发部！<br/><br/>")
//                        .append("<strong>注意：接口访问前缀为 /gmvcs/uap 。</strong><br/><br/>")
//                        .append("<strong>示例：</strong>")
//                        .append("GET    /cas/loginsuccess   登录成功接口<br/>")
//                        .append("http://10.10.20.120:8089/gmvcs/uap/cas/loginsuccess")
//                        .toString())
                .description("swagger2")//简介
                .termsOfServiceUrl("http://www.baidu.com")//服务url
                .version("1.0")
                .build();
    }

}
