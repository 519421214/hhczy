package com.king.hhczy.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * https://www.jianshu.com/p/108b8110a98c
 *
 * @author ningjinxiang
 */
@Component
@Slf4j
public class GupiaoUtil {
    @Autowired
    private RestTemplate restTemplate;
    //数据来源地址
    private String impUrl = "http://hq.sinajs.cn/list=";
    //    private String[] codes = {"sz300098,sz000100,sh600105,sz000625,sz300050,sz002446,sh600936,sh600699,sh600050"};002584,300366,300796,300706
    private String[] codes = {"sz002984,sz000100,sh600584,sh600031,sz000977,sz300123,sh600048,sz002555,sh000001"};

    //GP信息输出到控制台
    public void showDetail() {
        String codesStr = Arrays.stream(codes).reduce((s1, s2) -> s1 + "," + s2).orElse(null);
        DecimalFormat df = new DecimalFormat("0.00");

        String forObject = restTemplate.getForObject(impUrl + codesStr, String.class);
        System.out.println("---------------------------------------------------------------------------");
        Arrays.stream(forObject.split(";")).map(x -> x.substring(x.indexOf("\"") + 1).split(",")).forEach(y -> {
            if (y.length < 5) return;
//            System.out.println(y[0]+"\t[开]"+y[1]+"\t[低]"+y[5]+"\t[高]"+y[4]+"\t[当前]"+y[3]+"\t["+(Double.valueOf(y[3])>Double.valueOf(y[2])?"红":"绿")+"]"+
//                    df.format(((Double.valueOf(y[3])/Double.valueOf(y[2]))-1)*100)+"%");
            //获取盈亏占比
            double ratio = (Double.valueOf(y[3]) / Double.valueOf(y[2])) - 1;

            String logPrint = String.format("%s\t" + "[开]%s\t" + "[低]%s\t" + "[高]%s\t" + "[NOW]%s\t" + "[%s]%s",
                    y[0], y[1], y[5], y[4], y[3],
                    ratio > 0 ? "红" : "绿",
                    df.format(ratio * 100) + "%");
            System.out.println(logPrint);
        });
    }

    /**
     * 定时播报
     * codes:逗号分开
     */
    public void autoRead(String inputCodes) {
        Function<String, String> changeCode = x -> x.startsWith("6") ? ("sh" + x) : ("sz" + x);
        List<String> collect = Arrays.stream(inputCodes.split(",")).map(changeCode::apply).collect(Collectors.toList());

        String codesStr = collect.stream().reduce((s1, s2) -> s1 + "," + s2).orElse(null);
        DecimalFormat df = new DecimalFormat("0.00");
        //缓存
        Map<String, Double> cacheHis = new HashMap<>();
        //大盘当前
        //开启定时任务,执行定时播报
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String forObject = restTemplate.getForObject(impUrl + "sh000001," + codesStr, String.class);
                Arrays.stream(forObject.split(";")).map(x -> x.substring(x.indexOf("\"") + 1).split(",")).forEach(y -> {
                    if (y.length < 5) return;
//            System.out.println(y[0]+"\t[开]"+y[1]+"\t[低]"+y[5]+"\t[高]"+y[4]+"\t[当前]"+y[3]+"\t["+(Double.valueOf(y[3])>Double.valueOf(y[2])?"红":"绿")+"]"+
//                    df.format(((Double.valueOf(y[3])/Double.valueOf(y[2]))-1)*100)+"%");
                    //获取盈亏占比
                    double ratio = (Double.valueOf(y[3]) / Double.valueOf(y[2])) - 1;
                    //缓存点数
                    if (cacheHis.get(y[0]) == null) {
                        cacheHis.put(y[0], ratio);
                    }
                    StringBuffer sayWords = new StringBuffer();
                    if ("上证指数".equals(y[0])) {
                        if (cacheHis.get("aCur") == null) {
                            cacheHis.put("aCur", Double.parseDouble(y[3]));
                        }
                        Double szzs = cacheHis.get(y[0]);
                        if ((ratio - szzs) > 0.002) {
                            sayWords.append("拉升了，拉升了，大盘拉升了。");
                            cacheHis.put(y[0], ratio);
                        } else if ((ratio - szzs) < -0.002) {
                            cacheHis.put(y[0], ratio);
                            sayWords.append("跳水啦，崩盘啦，快跑。");
                        }
                        if (ratio > 0) {
                            if (szzs < 0) sayWords.append("牛逼，大盘翻红了。");
                        } else if (ratio < 0) {
                            if (szzs > 0) sayWords.append("卧槽，大盘绿了。");
                        }
                        //突破提示
                        int curA = (int) Math.floor(Double.parseDouble(y[3]) / 100);
                        int hisA = (int) Math.floor(cacheHis.get("aCur") / 100);
                        int re = curA - hisA;
                        if (re != 0) {
                            if (re > 0) {
                                sayWords.append("大盘突破").append(curA * 100).append("点啦啦啦。");
                            } else {
                                sayWords.append("大盘跌破").append(hisA * 100).append("点，靠靠靠。");
                            }
                            cacheHis.put("aCur", Double.parseDouble(y[3]));
                        }
                    }else {
                        //--------------------其他----------------------
                        //缓存点数
                        Double szzs = cacheHis.get(y[0]);
                        String ds = df.format(ratio * 100);
                        if ((ratio - szzs) > 0.01) {
                            cacheHis.put(y[0], ratio);
                            sayWords.append(y[0]+" 拉升了。当前涨跌"+ (ratio>0?("百分之"+ds):("负百分之"+ds)) +"，当前价格为"+y[3]+"元");
                        } else if ((ratio - szzs) < -0.01) {
                            cacheHis.put(y[0], ratio);
                            sayWords.append("请注意，"+y[0]+"跳水啦。当前涨跌"+ (ratio>0?("百分之"+ds):("负百分之"+ds)) +"，价格为"+y[3]+"元");
                        }
                        if (cacheHis.get("aCur")!=ratio) {
                            if (ratio>9.7) {
                                sayWords.append(y[0]+"涨停，涨停，涨停偶也");
                                cacheHis.put(y[0], ratio);
                            }else if (ratio<-9.7){
                                sayWords.append("妈的，"+y[0]+" 跌停！！");
                                cacheHis.put(y[0], ratio);
                            }
                        }
                        if (ratio > 0) {
                            if (szzs < 0) sayWords.append("牛逼，"+y[0]+"翻红了。");
                        } else if (ratio < 0) {
                            if (szzs > 0) sayWords.append("卧槽，"+y[0]+"竟然绿了。");
                        }
                    }
                    if (sayWords.length() > 0) {
                        WordsReading.speak(sayWords.toString());
                    }
//            String logPrint = String.format("%s\t" + "[开]%s\t" + "[低]%s\t" + "[高]%s\t" + "[NOW]%s\t" + "[%s]%s",
//                    y[0], y[1], y[5], y[4], y[3],
//                    ratio > 0 ? "红" : "绿",
//                    df.format(ratio * 100) + "%");
//            System.out.println(logPrint);
                });
            }
        }, 0, 10000);
    }

}
