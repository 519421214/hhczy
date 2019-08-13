package task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目启动后缓存数据
 */
@Component//被spring容器管理
//@Order(1)//如果多个自定义ApplicationRunner，用来标明执行顺序
public class AutoRunner implements ApplicationRunner {
    //获取配置文件属性值的另一种方法，
    @Autowired
    private Environment env;
//    @Value("${upgrade.package.time}")
//    private static Integer minite=2;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //@value在此获取不到值
        Integer minite = env.getProperty("upgrade.package.time", Integer.class);
        //重启后执行
        this.myTimer(minite);
    }
//    定时任务测试(线程)
    public static void myTimer(Integer minite){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("------定时任务,0秒后开始执行，每1秒执行一次--------");
            }
        }, 10, 60000*minite);
    }
}
