//
//package com.king.hhczy.base.config;
//
//import com.king.hhczy.base.constant.CacheConstants;
//import org.redisson.Redisson;
//import org.redisson.api.*;
//import org.redisson.config.Config;
//import org.redisson.config.SingleServerConfig;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.util.StringUtils;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * Redisson配置
// * 项目名称:  myproj
// * 包:        com.gosun.myproj.framework.configuration.redisson
// * 类名称:    RedissonConfiguration.java
// * 创建人:
// * 创建时间:  2018/9/2
// */
//@Configuration
//public class RedissonConfiguration {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Value("${redis.host}")
//    private String host;
//
//    @Value("${redis.port}")
//    private int port;
//
//    @Value("${redis.password:\"\"}")//如果没有配置spring.redis.password，则用:后面的值来代替
//    private String password;
//
//
//    @Bean(destroyMethod = "shutdown")
//    @Primary
//    public RedissonClient redissonClient() {
//
//        //创建配置
//        Config config = new Config();
//
//        //指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
//        //之前使用的spring-data-redis，用的客户端jedis，编码为org.springframework.data.redis.serializer.StringRedisSerializer
//        //改用redisson后为了之间数据能兼容，这里修改编码为org.redisson.client.codec.StringCodec
//        //config.setCodec(new org.redisson.client.codec.StringCodec());
//
//        //指定使用单节点部署方式
//        SingleServerConfig singleServerConfig = config.useSingleServer();
//        singleServerConfig.setAddress("redis://" + host + ":" + port);
//        if (StringUtils.hasText(password)) {
//            singleServerConfig.setPassword(password);//设置密码
//        }
//
//        //集群的配置
//        /*config.useClusterServers()
//                .setScanInterval(2000) // 集群状态扫描间隔时间，单位是毫秒
//                //可以用"rediss://"来启用SSL连接
//                .addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001", "redis://127.0.0.1:7002",
//                        "redis://127.0.0.1:7003", "redis://127.0.0.1:7004", "redis://127.0.0.1:7005");*/
//
//        return Redisson.create(config);
//    }
//
//    /**
//     * 通过注入RedissonClient来使用
//     *
//     * @param redissonClient Redisson客户端Bean
//     */
////    @Autowired
//    public void testRedisson(RedissonClient redissonClient) {
//        boolean infoEnabled = logger.isInfoEnabled();
//
//        RBucket<Integer> rBucket = redissonClient.getBucket("redission_test_key1_on_project_starting");
//        Integer integer = rBucket.get();
//
//        if (infoEnabled) {
//            logger.info("redission_test_key1_on_project_starting's value before increment : {}", integer);
//        }
//
//        if (integer == null) {
//            integer = 0;
//        }
//        rBucket.set(++integer);
//
//        if (infoEnabled) {
//            logger.info("redission_test_key1_on_project_starting's value after increment : {}", rBucket.get());
//        }
//
//        RAtomicLong rAtomicLong = redissonClient.getAtomicLong("redission_test_key2_on_project_starting");
//        long beforeIncrement = rAtomicLong.getAndIncrement();
//        long afterIncrement = rAtomicLong.get();
//
//        if (infoEnabled) {
//            logger.info("redission_test_key2_on_project_starting's value before increment : {}", beforeIncrement);
//            logger.info("redission_test_key2_on_project_starting's value after increment : {}", afterIncrement);
//        }
//
//        //redis hash的存储方式
//        RMap<String, Object> rMap = redissonClient.getMap("redission_test_Map_on_project_starting");
//        rMap.put("111", 111);
//        rMap.put("222", "222");
//        rMap.put("333", 333.3d);
//        rMap.put("444", new Object());
//        rMap.expire(30, TimeUnit.MINUTES);
//
//        RMapCache<String, String> tokenCache = redissonClient.getMapCache(CacheConstants.INIT_CACHE_PREFIX);
//        //token有效时间30分钟，最大闲置时间比接口端少一分钟（避免延迟导致误差）,前一个有效时间，后面是最大闲置时间
////        tokenCache.put(token, message.getString("Token"), 30, TimeUnit.MINUTES, govideoTokenLosetime, TimeUnit.MINUTES);
//
//        if (infoEnabled) {
//            logger.info("put values to redission_test_Map_on_project_starting success");
//            logger.info("redission_test_Map_on_project_starting's values : {}", rMap.toString());
//        }
//    }
//}
