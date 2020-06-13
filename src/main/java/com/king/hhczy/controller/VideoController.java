package com.king.hhczy.controller;

import com.king.hhczy.common.util.FilesUtils;
import com.king.hhczy.common.util.NonStaticResourceHttpRequestHandler;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过接口直接获取视频流播放
 * @author ningjinxiang
 * @ClassName: ApiController
 * @Description: 对外服务提供controller
 * @date 2020年05月15日
 * todo 有报错问题待处理
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/v")
public class VideoController {
    @Autowired
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    private static List<Path> paths;
    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @GetMapping("/vip")
    public String videoPath() throws Exception {
        StringBuffer pathStr = new StringBuffer();
        //獲取公网IP
        //访问地址
        String url = "http://" + getInternetIp() + ":6066/hhczy/v/vip/";
        if (paths == null) {
            List<Path> videoList = new ArrayList<>();
            videoList.addAll(FilesUtils.getAllFilesPaths("H:\\迅雷下载"));
            paths = videoList.stream().filter(x -> x.toString().toUpperCase().lastIndexOf(".MP4") != -1&&x.toString().indexOf("\\29\\(")==-1).collect(Collectors.toList());
        }
        for (int i = 0; i < paths.size(); i++) {
            String name = paths.get(i).toString();
            pathStr.append(String.format("<p><a href=\"%s%d\">%d.%s</a></p>",url,i,++i, name.substring(name.lastIndexOf("\\")+1)));
        }
        return pathStr.toString();
    }
    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @GetMapping("/vip/{index}")
    public void videoPreview(@PathVariable int index,HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (paths == null) {
            List<Path> videoList = new ArrayList<>();
            videoList.addAll(FilesUtils.getAllFilesPaths("H:\\迅雷下载"));
            videoList.addAll(FilesUtils.getAllFilesPaths("F:\\迅雷下载2"));
            videoList.addAll(FilesUtils.getAllFilesPaths("F:\\迅雷下载2"));
            paths = videoList.stream().filter(x -> x.toString().toUpperCase().lastIndexOf(".MP4") != -1&&x.toString().indexOf("\\29\\(")==-1).collect(Collectors.toList());
        }
        preview(request,response,paths.get(index));
    }
    private void preview(HttpServletRequest request, HttpServletResponse response,Path filePath) throws Exception {
//        String path = request.getParameter("path");
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    /**
     * 获取公网IP
     * @return
     * @throws Exception
     */
    private static String getInternetIp() throws Exception{
        try {
            // 打开连接
            Document doc = Jsoup.connect("http://chaipip.com/").get();
            Elements eles = doc.select("#ip");
            return eles.attr("value");
        }catch (IOException e) {
            e.printStackTrace();
        }

        return InetAddress.getLocalHost().getHostAddress();
    }
}
