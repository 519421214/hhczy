package com.king.hhczy.base.task;

import com.king.hhczy.base.constant.HhczyConstant;
import com.king.hhczy.common.util.AliyunDDNS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ningjinxiang
 * @date 20190517
 * govideo一些参数定时同步到本地
 */
@Component
@Configurable
@EnableScheduling
@Slf4j
public class DDNSTasks {
    @Autowired
    private AliyunDDNS aliyunDDNS;
    /**
     * @Author:ningjinxiang
     * @Description:定时导出任务
     * @Date:2019/1/23_10:10
     * @Scheduled(fixedRate = 5000) ：上一次开始执行时间点之后5秒再执行
     * @Scheduled(fixedDelay = 5000) ：上一次执行完毕时间点之后5秒再执行
     * @Scheduled(initialDelay=1000, fixedRate=5000) ：第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次
     * @Scheduled(cron = "${task.cron}") 也可以通过cron表达式
     */
//    @Scheduled(cron = "0/10 * * * * ?")//每10秒执行，测试用
//    @Scheduled(cron = "${task.cron}")//每15分钟执行（）

    /**
     * 每天23点
     */
    @Scheduled(cron = "0 0 23 * * ? ")
//    @Scheduled(initialDelay=60000,fixedRate = 190000)
    public void tip1() {
        if (isOpen()) {
            System.out.println();
            System.out.print("--->");
            System.out.print(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            System.out.println(" ##开启阿里云解析任务##");
            System.out.println("--->任务进行中...");
        }
    }
    /**
     * 每天1点结束
     */
    @Scheduled(cron = "0 1 1 * * ?")
//    @Scheduled(initialDelay=180000,fixedRate = 180000)
    public void tip2() {
        if (isOpen()) {
            System.out.print(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            System.out.println(" ##任务完毕##");
        }
    }

    /**
     * 每天22点开始，每小时+5分钟一次，2点结束
     */
//    @Scheduled(cron = "0 5 0-2,22-23 * * ?")
    @Scheduled(cron = "0 0/5 0,23 * * ?")
//    @Scheduled(initialDelay=61000,fixedRate = 60000)
    public void ddnsTask() {
        if (isOpen()) {
            aliyunDDNS.ddns(false);
        }
    }

    private boolean isOpen() {
        LocalDateTime updateTime = HhczyConstant.UPDATE_TIME;
        if (updateTime != null && Duration.between(updateTime, LocalDateTime.now()).toHours() < 28) {
            return false;
        }
        return true;
    }

}
