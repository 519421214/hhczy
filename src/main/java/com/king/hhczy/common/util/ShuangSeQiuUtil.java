package com.king.hhczy.common.util;

import com.king.hhczy.entity.BichromaticSphere;
import com.king.hhczy.service.IBichromaticSphereService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 参考https://www.cnblogs.com/chy18883701161/p/10852203.html
 * https://blog.csdn.net/weixin_34195142/article/details/92612816?utm_source=distribute.pc_relevant.none-task
 * 双色球往期一键保存工具
 * @author ningjinxiang
 */
@Component
public class ShuangSeQiuUtil {
    @Autowired
    private IBichromaticSphereService bichromaticSphereService;

    //爬gwy新闻数据
    public void bichromaticSphere() {
        List<BichromaticSphere> bichromaticSpheres = new ArrayList<>();
        //拿到网页源码
        String urlSource = HtmlRequest.getHttpsURLSource("https://datachart.500.com/ssq/history/history.shtml?start=00001&end=20089");
        if (!StringUtils.hasText(urlSource)) return;
        //采用jsoup解析
        Document doc = Jsoup.parse(urlSource);
        //定位到集合内容
        Elements trs = doc.select("#tdata tr");
        for (Element tr : trs) {
            Elements tds = tr.select("td");
            BichromaticSphere bichromaticSphere = new BichromaticSphere();
            bichromaticSphere.setId(Integer.parseInt("8"+tds.get(0).text()));
            bichromaticSphere.setNo(tds.get(0).text());
            bichromaticSphere.setOne(tds.get(1).text());
            bichromaticSphere.setTwo(tds.get(2).text());
            bichromaticSphere.setThree(tds.get(3).text());
            bichromaticSphere.setFour(tds.get(4).text());
            bichromaticSphere.setFive(tds.get(5).text());
            bichromaticSphere.setSix(tds.get(6).text());
            bichromaticSphere.setSeven(tds.get(7).text());
            bichromaticSphere.setJackpot(tds.get(9).text());
            bichromaticSphere.setFirstPrizeSum(Integer.parseInt(tds.get(10).text()));
            bichromaticSphere.setFirstPrizeBonus(tds.get(11).text());
            bichromaticSphere.setSecondPrizeSum(Integer.parseInt(tds.get(12).text()));
            bichromaticSphere.setSecondPrizeBonus(tds.get(13).text());
            bichromaticSphere.setBettingAmount(tds.get(14).text());
            bichromaticSphere.setPublishTime(LocalDate.parse(tds.get(15).text()));
            bichromaticSphere.setInsertTime(LocalDateTime.now());
            bichromaticSphere.setUpdateTime(LocalDateTime.now());

            bichromaticSpheres.add(bichromaticSphere);
        }
        if (!ObjectUtils.isEmpty(bichromaticSpheres)) {
            bichromaticSphereService.saveOrUpdateBatch(bichromaticSpheres, 10000);
        }
    }
}
