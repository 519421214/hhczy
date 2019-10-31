package com.king.hhczy.base.constant;

public final class CacheConstants {

    /**
     * 缓存类型缓存关键值前缀
     */
    private final static String COMMON_CACHE_PREFIX = "hhczy:";//所有cache对象公用前缀
    /**
     * 重启系统需要初始化 前缀
     */
    public final static String INIT_CACHE_PREFIX = COMMON_CACHE_PREFIX+"init:";//服务重启，需初始化的key前缀
    /**
     * 不随系统重启影响 持久化前缀
     */
    public final static String LASTING_CACHE_PREFIX = COMMON_CACHE_PREFIX+"lasting:";
    /**
     * 微信系统
     */
    public final static String WECHAT = "wechat:";
    /**
     * 用户
     */
    public final static String USER = "user:";


}
