package com.king.hhczy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author ningjinxiang
 */
//开启缓存策略
@EnableCaching
@SpringBootApplication
@MapperScan("com.king.hhczy.mapper")
public class MainApplication {
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}



