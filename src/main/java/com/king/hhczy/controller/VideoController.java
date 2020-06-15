package com.king.hhczy.controller;

import com.king.hhczy.common.util.FilesUtils;
import com.king.hhczy.common.util.NonStaticResourceHttpRequestHandler;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过接口直接获取视频流播放
 *
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
    private List<Path> paths;
    private static String url;
    //视频扫描路径
    private static String[] videoPaths = {"H:\\迅雷下载"};
    //视频格式
    private static String[] videoFormat = {"MP4"};

    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @GetMapping("/vip")
    public String videoPath(HttpServletRequest request) throws Exception {
        StringBuffer pathStr = new StringBuffer();
        if (StringUtils.isEmpty(url)) {
            url = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getServletPath())) + "/v/vip/";
        }
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
            pathStr.append(String.format("<p><a href=\"%s%d\">%d. %s</a></p>", url, i, i + 1, name.substring(name.lastIndexOf("\\") + 1)));
        }
        return pathStr.toString();
    }

    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @GetMapping("/vip/{index}")
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
