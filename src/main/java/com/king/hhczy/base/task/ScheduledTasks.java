package com.king.hhczy.base.task;

import com.king.hhczy.common.util.GupiaoUtil;
import com.king.hhczy.common.util.PaChongUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author ningjinxiang
 * @date 20190517
 * govideo一些参数定时同步到本地
 */
@Component
@Configurable
@EnableScheduling
@Slf4j
public class ScheduledTasks {
//    @Autowired
//    private DataSourceService dataSourceService;
    @Autowired
    private PaChongUtil paChongUtil;
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
     * 定时同步通道状态
     */
//    @Scheduled(fixedDelay = 120000)//两分钟执行一次
    public void buildToLocal() {
//        dataSourceService.updateChannelStatus(this.getClass().getSimpleName(), UUIDUtil.uuid());
    }
    @Autowired
    private GupiaoUtil gupiaoUtil;
    @Scheduled(fixedDelay = 5000)//
    public void test() {
        gupiaoUtil.showDetail();
    }

    /**
     * 每天闲时同步按月查询数据到redis
     */
//    @Scheduled(fixedDelay = 10000)//两分钟执行一次
//    @Scheduled(cron = "0 0 2 * * ?")//每天凌晨2点同步一次
    public void syncWhithDayHasDataOfMonth() {
//        dataSourceService.syncWhithDayHasDataOfMonth(this.getClass().getSimpleName(), UUIDUtil.uuid());
    }
    /**
     * 每天闲时同步按月查询数据到redis
     */
//    @Scheduled(fixedDelay = 60000)//两分钟执行一次
//    @Scheduled(cron = "0 0 2 * * ?")//每天凌晨2点同步一次
    @Scheduled(cron = "0 0/1 6-23 * * ?")//6-23点每分钟执行一次
    public void paGovNews() {
        paChongUtil.govNews();
        paChongUtil.motNews();
    }

    private static int tip = 0;
//    @Scheduled(fixedDelay = 1000)//两分钟执行一次
//    @Scheduled(cron = "0 0 2 * * ?")//每天凌晨2点同步一次
    public void aaa() throws AWTException {
        if (tip==3) {
            return;
        }
        Robot robot = new Robot();
        BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(1746, 592, 1, 1));
        int rgb = screenCapture.getRGB(0, 0);
        if (rgb!=-240029) {
            tip++;
            Runtime runtime = Runtime.getRuntime();
            Process p = null;
//        String path = "C:\\Program Files (x86)\\Tencent\\WeChat\\WeChat.exe";
            try {
                for (int i = 0; i < 3; i++) {
                    p = runtime.exec("C:\\\\Program Files (x86)\\\\Tencent\\\\WeChat\\\\WeChat.exe");
                    Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable tText = new StringSelection("警告：你的电脑被人动，请查监控快速确认"); //自己定义就需要把这行注释，下行取消注释
//            tText = new StringSelection("爱你每一天");//如果爱得深，把这行取消注释，把内容更换掉你自己想说的
                    clip.setContents(tText, null);
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_CONTROL);//释放所有按键
                    robot.delay(100);
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_CONTROL);//释放所有按键
                    robot.delay(100);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
//    @Value("${local.clean.path}")
//    private String[] cleanPaths;
    /**
     * 每天0点清理大于30天的文件
     */
//    @Scheduled(cron = "0 0 0 1/1 * ?")
//    @Scheduled(initialDelay=1000,fixedDelay = 10000)
//    public void clean() {
//        System.out.println("-->开始执行定期清理任务（清理计划：每天零晨扫描清理一次30天后的文件）");
//        System.out.println("-->待扫描路径：");
//        Arrays.stream(cleanPaths).forEach(System.out::println);
//        Arrays.stream(cleanPaths).forEach(x->{
//            log.info("开始清理路径：{}",x);
//            Path first = Paths.get(x);
//            if (Files.exists(first)&&Files.isDirectory(first)) {
//                //遍历所有子文件，获取路径
//                List<Path> allFilesPaths = FilesUtils.getAllFilesPaths(x);
//                allFilesPaths.stream().parallel().forEach(y->{
//                    //拿到文件最后更新时间戳
//                    long fileCreateTime = FilesUtils.getFileTime(y, FilesUtils.FilesAttribute.LAST_MODIFIED_TIME);
//                    //获取30天前的时间戳
//                    long before30Time = LocalDateTime.now().minusDays(30).toInstant(ZoneOffset.of("+8")).toEpochMilli();
//                    if(fileCreateTime<before30Time){
//                        try {
//                            if (Files.deleteIfExists(y)) {
//                                log.info(y.getFileName()+" 过期删除成功");
//                            }
//                        } catch (IOException e) {
//
//                        }
//                    }
//                });
//            }else {
//                log.error("错误的文件目录：{}",x);
//            }
//        });
//        log.info("当次任务执行完毕\r\n");
//    }
}
