package com.king.hhczy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * springboot开发三板斧：加依赖、写注解、写配置
 * @author ningjinxiang
 */
//开启缓存策略
@EnableCaching
@SpringBootApplication
@MapperScan("com.king.hhczy.mapper")
public class MainApplication {
	//启动类
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}



