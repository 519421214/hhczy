package com.king.hhczy.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.king.hhczy.entity.BichromaticSphere;
import com.king.hhczy.mapper.BichromaticSphereMapper;
import com.king.hhczy.service.IBichromaticSphereService;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 参考https://www.cnblogs.com/chy18883701161/p/10852203.html
 * https://blog.csdn.net/weixin_34195142/article/details/92612816?utm_source=distribute.pc_relevant.none-task
 * 双色球往期一键保存工具
 * @author ningjinxiang
 */
@Component
@Slf4j
public class ShuangSeQiuUtil {
    @Autowired
    private IBichromaticSphereService bichromaticSphereService;
    @Autowired
    private BichromaticSphereMapper bichromaticSphereMapper;

    public void bichromaticSphere() {
        log.info("开始同步双色球往期数据");
        //找出最后更新的数据
        QueryWrapper<BichromaticSphere> qwm = new QueryWrapper<>();
        qwm.orderByDesc("publish_time").last("limit 1");
        BichromaticSphere maxOne = bichromaticSphereMapper.selectOne(qwm);
        String maxNo = "00001";
        if (!ObjectUtils.isEmpty(maxOne)) {
            maxNo = maxOne.getNo();
        }
        //拿到网页源码
        String urlSource = HtmlRequest.getHttpsURLSource("https://datachart.500.com/ssq/history/newinc/history.php?start="+maxNo);
        if (!StringUtils.hasText(urlSource)) return;
        //采用jsoup解析
        Document doc = Jsoup.parse(urlSource);
        //定位到集合内容
        Elements trs = doc.select("#tdata tr");
        int sucNum = 0;
        for (Element tr : trs) {
            Elements tds = tr.select("td");
            BichromaticSphere bichromaticSphere = new BichromaticSphere();
            String no = tds.get(0).text();
            bichromaticSphere.setNo(no);
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

            //不存在就新增
            QueryWrapper<BichromaticSphere> qw = new QueryWrapper<>();
            qw.eq("no", no);
            int qc = bichromaticSphereService.count(qw);
            if (qc==0) {
                if (bichromaticSphereService.save(bichromaticSphere)) {
                    sucNum++;
                }
            }
        }
        log.info("同步结束，本次获取到数据 {}条，入库 {}条",trs.size(),sucNum);
    }
}
