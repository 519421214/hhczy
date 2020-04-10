package com.king.hhczy.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * https://www.jianshu.com/p/108b8110a98c
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
    private String[] codes = {"sz300098,sz000100,sz000625,sh600699,sz000505,sz002352,sh600127,sz002370,sz002770"};

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

}
