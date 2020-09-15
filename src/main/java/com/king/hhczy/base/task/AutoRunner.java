package com.king.hhczy.base.task;

import com.king.hhczy.common.util.ShuangSeQiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动后缓存数据
 */
@Component//被spring容器管理
//@Order(1)//如果多个自定义ApplicationRunner，用来标明执行顺序
public class AutoRunner implements ApplicationRunner {
//    @Autowired
//    private RedissonClient redissonClient;
    @Autowired
    private ShuangSeQiuUtil shuangSeQiuUtil;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //重启时删除缓存
//        redissonClient.getMap(CacheConstants.INIT_CACHE_PREFIX).clear();
//        dataSourceService.getAllArea(null);
//        dataSourceService.syncWhithDayHasDataOfMonth(this.getClass().getSimpleName(), UUIDUtil.uuid());
        shuangSeQiuUtil.bichromaticSphere();
    }
    //定时任务测试(线程)
//    public static void myTimer(){
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("------定时任务,0秒后开始执行，每1秒执行一次--------");
//            }
//        }, 0, 1000);
//    }
}
