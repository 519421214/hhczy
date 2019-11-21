package com.king.hhczy.base.task;

import com.king.hhczy.common.util.AliyunDDNS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 项目启动后缓存数据
 */
@Component//被spring容器管理
//@Order(1)//如果多个自定义ApplicationRunner，用来标明执行顺序
public class AutoRunner implements ApplicationRunner {
//    @Autowired
//    private RedissonClient redissonClient;
    @Autowired
    private AliyunDDNS aliyunDDNS;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println();
        System.out.println("===By 吾爱破解-作者：cocxth [优秀伸手党]");
        System.out.print("--->");
        System.out.print(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        System.out.println(" ##开启阿里云解析任务##");
        System.out.println("--->由于IP变换时间未知，客服说48小时变一次，也有人说晚上10点、0点或2点");
        System.out.println("--->因此建议晚上10点至凌晨2点选一个整点重启一次光猫（之后无需再重启），以确保本程序的正确执行");
        System.out.println("--->程序第一次启动扫描一次，之后每天晚上10点至凌晨2点每整点+5分钟扫描一次");
        System.out.println("--->成功修改一次后48小时内不再重复扫描");
        System.out.println("--->任务执行中...");
        aliyunDDNS.ddns(true);
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
