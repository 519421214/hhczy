package com.king.hhczy.controller;

import com.king.hhczy.common.util.FilesUtils;
import com.king.hhczy.common.util.NonStaticResourceHttpRequestHandler;
import com.king.hhczy.common.util.WordsReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 影音类
 */
@RestController
public class VSController {
    @Autowired
    private WordsReading wordsReading;
    @Value("${realm.name}")
    private String realmName;
    @Value("${server.port}")
    private String port;

    //视频
    @Autowired
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    //视频扫描路径
    private static String[] videoPaths = {"H:\\迅雷下载"};
    //视频格式
    private static String[] videoFormat = {"MP4","AVI"};
    private List<Path> paths;
    /**
     * 本地方式上传文件/图片
     *
     * @param words
     * @return
     */
    @PostMapping("/word-to-speak")
    public String wordToSpeak(@RequestBody Map<String,String> words) {
        String inWords = words.get("words");
        String com = realmName + ":" + port;
        return wordsReading.build(inWords,com);
    }
    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @GetMapping("/v-list")
    public String videoPath(HttpServletRequest request) throws Exception {
        String url = realmName +":"+ port;
        StringBuffer pathStr = new StringBuffer();
        if (StringUtils.isEmpty(url)) {
            url = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getServletPath()));
        }
        url += "/v-play/";
        //獲取公网IP
        //访问地址
        if (paths == null || paths.size() == 0) {
            List<Path> videoList = new ArrayList<>();
            for (String videoPath : videoPaths) {
                //若存在配置的视频格式
                videoList.addAll(FilesUtils.getAllFilesPaths(videoPath).//找到所有视频路径
                        stream().filter(//过滤
                        y -> Arrays.stream(videoFormat).filter(//若路径属于指定视频格式↓
                                x -> y.toString().toUpperCase().lastIndexOf("." + x.toUpperCase()) != -1).findAny().isPresent()
                ).collect(Collectors.toList()));//重新收集
            }
            paths = videoList.stream().collect(Collectors.toList());
        }
        for (int i = 0; i < paths.size(); i++) {
            String name = paths.get(i).toString();
            //适配手机显示
            pathStr.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\">\n");
            //拼接播放地址与名称
            pathStr.append(String.format("<p><a href=\"%s%d\" target=\"_blank\">%d. %s</a></p>", url, i, i + 1, name.substring(name.lastIndexOf("\\") + 1)));
        }
        return pathStr.toString();
    }

    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @GetMapping("/v-play/{index}")
    public void videoPreview(@PathVariable int index, HttpServletRequest request, HttpServletResponse response) throws Exception {
        preview(request, response, paths.get(index));
    }

    private void preview(HttpServletRequest request, HttpServletResponse response, Path filePath) throws Exception {
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
}