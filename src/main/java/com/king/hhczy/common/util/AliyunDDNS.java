package com.king.hhczy.common.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.king.hhczy.base.constant.HhczyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 阿里云动态域名解析
 *
 * @author ningjinxiang
 */
@Component
@Slf4j
public class AliyunDDNS {
    @Value("${dns-access-key-id}")
    private String accessKeyId;
    @Value("${dns-access-key-secret}")
    private String accessKeySecret;
    @Value("${dns-domain-name}")
    private String domainName;
    @Value("${dns-region-id}")
    private String regionId;
    @Value("${dns-rr-Key-kord}")
    private String rrKeyWord;
    @Value("${dns-type}")
    private String type;

    /**
     * 获取主域名的所有解析记录列表
     */
    private DescribeDomainRecordsResponse describeDomainRecords(DescribeDomainRecordsRequest request, IAcsClient client) {
        try {
            // 调用SDK发送请求
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            // 发生调用错误，抛出运行时异常
            System.out.println("-->获取主域名解析记录列表错误，请检查配置，修改后重新启动服务<--");
        }
        return null;
    }

    /**
     * 获取当前主机公网IP
     */
    private String getCurrentHostIP() {
        // 这里使用jsonip.com第三方接口获取本地IP
        String jsonip = "https://jsonip.com/";
        // 接口返回结果
        String result = "";
        BufferedReader in = null;
        try {
            // 使用HttpURLConnection网络请求第三方接口
            URL url = new URL(jsonip);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }
        // 正则表达式，提取xxx.xxx.xxx.xxx，将IP地址从接口返回结果中提取出来
        String rexp = "(\\d{1,3}\\.){3}\\d{1,3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(result);
        String res = "";
        while (mat.find()) {
            res = mat.group();
            break;
        }
        return res;
    }

    /**
     * 修改解析记录
     */
    private UpdateDomainRecordResponse updateDomainRecord(UpdateDomainRecordRequest request, IAcsClient client) {
        try {
            // 调用SDK发送请求
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            // 发生调用错误，抛出运行时异常
            System.out.println("-->修改解析记录错误，请检查配置，修改后重新启动服务<--");
        }
        return null;
    }

    public void ddns(boolean firstTime) {
        // 设置鉴权参数，初始化客户端
        DefaultProfile profile = DefaultProfile.getProfile(
                regionId,// 地域ID
                accessKeyId,// 您的AccessKey ID
                accessKeySecret);// 您的AccessKey Secret
        IAcsClient client = new DefaultAcsClient(profile);
        AliyunDDNS ddns = new AliyunDDNS();
        // 当前主机公网IP
        String currentHostIP = ddns.getCurrentHostIP();
        //公网IP无变化，不往下处理
        if (HhczyConstant.FORWARD_IP!=null&&HhczyConstant.FORWARD_IP.equals(currentHostIP)) {
            return;
        }
        //公网IP发生变化，更新标记值
        HhczyConstant.FORWARD_IP = currentHostIP;
        // 查询指定二级域名的最新解析记录
        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest();
        // 主域名
        describeDomainRecordsRequest.setDomainName(domainName);
        // 主机记录
        describeDomainRecordsRequest.setRRKeyWord(rrKeyWord);
        // 解析记录类型
        describeDomainRecordsRequest.setType(type);
        DescribeDomainRecordsResponse describeDomainRecordsResponse = ddns.describeDomainRecords(describeDomainRecordsRequest, client);
        if (describeDomainRecordsResponse==null) {
            return;
        }
//		log_print("describeDomainRecords",describeDomainRecordsResponse);

        List<DescribeDomainRecordsResponse.Record> domainRecords = describeDomainRecordsResponse.getDomainRecords();
        // 最新的一条解析记录
        if (domainRecords.size() != 0) {
            DescribeDomainRecordsResponse.Record record = domainRecords.get(0);
            // 记录ID
            String recordId = record.getRecordId();
            // 记录值
            String recordsValue = record.getValue();

            if (firstTime) {
                System.out.println("--->当前主机公网IP为：" + currentHostIP);
                System.out.println("--->阿里云解析IP为：" + recordsValue);
            }
            if (!currentHostIP.equals(recordsValue)) {
                // 修改解析记录
                UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
                // 主机记录
                updateDomainRecordRequest.setRR(rrKeyWord);
                // 记录ID
                updateDomainRecordRequest.setRecordId(recordId);
                // 将主机记录值改为当前主机IP
                updateDomainRecordRequest.setValue(currentHostIP);
                // 解析记录类型
                updateDomainRecordRequest.setType(type);
                UpdateDomainRecordResponse updateDomainRecordResponse = ddns.updateDomainRecord(updateDomainRecordRequest, client);
//				log_print("updateDomainRecord",updateDomainRecordResponse);
                if (!firstTime) {
                    System.out.println("--->当前主机公网IP为：" + currentHostIP);
                    System.out.println("--->阿里云解析IP为：" + recordsValue);
                }
                System.out.println("--->当前主机公网IP发生变化，已与阿里云域名解析同步完成");
                System.out.println("--->任务将于两天后凌晨再次开启");
                HhczyConstant.UPDATE_TIME = LocalDateTime.now();
            } else {
                if (firstTime) {
                    System.out.println("--->公网IP没有变更，任务将于凌晨再次开启");
                }
            }
        }else {
            System.out.println("-->找不到域名解析数据，请检查配置，修改后重新启动服务<--");
        }
    }
}
