package com.king.hhczy.common.util;

import com.king.hhczy.base.constant.CacheConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * 参考https://www.cnblogs.com/chy18883701161/p/10852203.html
 * https://blog.csdn.net/weixin_34195142/article/details/92612816?utm_source=distribute.pc_relevant.none-task
 *
 * @author ningjinxiang
 */
@Component
public class PaChongUtil {
    @Autowired
    private RedissonClient redissonClient;
//    @Autowired
//    private MailUtil mailUtil;
    private String[] sendGovNewMails = {"519421214@qq.com","694268341@qq.com","1282550688@qq.com"};
    @Autowired
    private MailUtil mailUtil;
    //爬gwy新闻数据
    public void govNews() {
        RMapCache<Object, Object> mapCache = redissonClient.getMapCache(CacheConstants.PA_CHONG + "gov-news");

        //拿到网页源码
        String urlSource = HtmlRequest.getURLSource("http://www.gov.cn/xinwen/gundong.htm");
        if (!StringUtils.hasText(urlSource)) return;
        //采用jsoup解析
        Document doc = Jsoup.parse(urlSource);
        //定位到集合内容
        Elements liH4 = doc.select("li h4");
        for (Element ele : liH4) {
            //拿到跳转地址
            String href = ele.select("a").attr("href");
            //拿到唯一key
            String idKey = href.substring(href.lastIndexOf("/")+1, href.lastIndexOf("."));
            //拿到日期
            int span = ele.select("span").size();
            String date = ele.select("span[class='date']").text();
            //判断新闻是不是当天的
            if (LocalDate.parse(date).isEqual(LocalDate.now())) {
                //已经提醒过的新闻不再提醒
                if (mapCache.get(idKey)==null) {
                    //拿到主题
                    Elements a = ele.select("a");
                    String title = StringUtils.hasText(a.select("span").text())?a.select("span").text():a.text()+"。";
                    String details = "详情：http://www.gov.cn" + href;
//                    sendTo(title,details);
                    mapCache.put(idKey,1,3, TimeUnit.DAYS,3,TimeUnit.DAYS);
                    Log.info("推送了消息：{}{}", title,details);
                }
            }
        }
    }
    //爬交通新闻数据
    public void motNews() {
        RMapCache<Object, Object> mapCache = redissonClient.getMapCache(CacheConstants.PA_CHONG + "not-news");

        //拿到网页源码
        String urlSource = HtmlRequest.getURLSource("http://www.mot.gov.cn/jiaotongyaowen");
        if (!StringUtils.hasText(urlSource)) return;
        //采用jsoup解析
        Document doc = Jsoup.parse(urlSource);
        //定位到集合内容
        Elements liH4 = doc.select("div[class='list-group tab-content'] a");
        for (Element ele : liH4) {
            //拿到跳转地址
            String href = ele.attr("href");
            //拿到唯一key
            String idKey = href.substring(href.lastIndexOf("/"), href.lastIndexOf("."));
            //拿到日期
            String date = ele.select("span[class=badge]").text();
            //判断新闻是不是当天的
            if (LocalDate.parse(date).isEqual(LocalDate.now())) {
                //已经提醒过的新闻不再提醒
                if (mapCache.get(idKey)==null) {
                    //拿到主题
                    String title = ele.select("span").first().text()+"。";
                    String details = "详情：http://www.mot.gov.cn/jiaotongyaowen/" + href;
//                    sendTo(title,details);
                    mapCache.put(idKey,1,3, TimeUnit.DAYS,3,TimeUnit.DAYS);
                    Log.info("推送了消息：{}{}", title,details);
                }
            }
        }
    }
    //爬图片到本地
    public void test() {
        //推邮箱
        mailUtil.sendMail("这是一个标题","这是详细内容", new String[]{"519421214@qq.com"});
    }

    private void sendTo(String title,String details){
        //推邮箱
//        mailUtil.sendMail(title,details,sendGovNewMails);
        //推微信
//        KeyboardUtil.copyAndSend(title+details);
        //本地保存
//        KeyboardUtil.copyAndSend(title+details);
    }
}
