package com.king.hhczy.base.config;

import com.baidu.aip.ocr.AipOcr;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Redisson配置
 * 项目名称:  myproj
 * 包:        com.gosun.myproj.framework.configuration.redisson
 * 类名称:    RedissonConfiguration.java
 * 创建人:
 * 创建时间:  2018/9/2
 */
@Configuration
@Slf4j
public class BaiduConfiguration {
    @Value("${baidu.server.appid}")
    private String appid;
    @Value("${baidu.server.apikey}")
    private String apikey;
    @Value("${baidu.server.secretkey}")
    private String secretkey;


    @Bean
    public AipOcr redissonClient() {
        //初始化百度接口
        log.info("初始化百度ocr服务");
        AipOcr aipOcr = new AipOcr(appid, apikey, secretkey);
        // 可选：设置网络连接参数
        aipOcr.setConnectionTimeoutInMillis(2000);//建立连接的超时时间（单位：毫秒）
        aipOcr.setSocketTimeoutInMillis(60000);//通过打开的连接传输数据的超时时间（单位：毫秒）

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        aipOcr.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        aipOcr.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        try {
            //=====以下代码其实没啥用，只是为了随便测试一张图片看是否能读取到,直接return aipOcr;就OK了=====//
            //=====以下代码其实没啥用，只是为了随便测试一张图片看是否能读取到,直接return aipOcr;就OK了=====//
            //=====以下代码其实没啥用，只是为了随便测试一张图片看是否能读取到,直接return aipOcr;就OK了=====//

            //读取resource下文件方法一：
            InputStream inputStream = this.getClass().getResourceAsStream("/init-test/ocr-test.jpg");
            //读取resource下文件方法二：
//            ClassPathResource classPathResource = new ClassPathResource("static/assets/test.txt");
//            InputStream inputStream = classPathResource.getInputStream();
            //读取resource下文件方法三：
//            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("excleTemplate/test.xlsx");

            //读取到字节buffer
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            HashMap<String, String> options = new HashMap();
            options.put("language_type", "CHN_ENG");    //中英语言
            JSONObject res = aipOcr.basicGeneral(bytes, options);
            int initTip = res.getJSONArray("words_result").getJSONObject(0).getInt("words");
            log.info("初始化百度ocr服务成功,标识码：{}",initTip);
        } catch (Exception e) {
            log.info("初始化百度ocr服务失败，请检查相关参数");
            log.info("服务如继续使用，相关ocr功能将无法使用");
        }
        return aipOcr;
    }

}
