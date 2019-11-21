package com.king.hhczy.common.util;


import org.springframework.stereotype.Component;

import java.util.concurrent.ForkJoinPool;

/**
 * stream并行处理统一类
 */
@Component
public class ParallelStreamUtil {

    public boolean parallelWaitFinish(Runnable runnable) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(16);//分配16个并行任务
        forkJoinPool.submit(runnable);
        forkJoinPool.shutdown();//关闭释放线程池
        //等待并行线程流，避免main线程提前结束请求
        synchronized (forkJoinPool){
            try {
                forkJoinPool.wait();
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }


//    private void test() {
//        List<Integer> test = new ArrayList<>();
//        Runnable runnable = () -> test.parallelStream().forEach(x -> {
//        });
//        this.parallelWaitFinish(runnable);
//    }
}
