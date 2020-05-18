package com.king.hhczy.controller;

import com.king.hhczy.common.util.NonStaticResourceHttpRequestHandler;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @GetMapping("/vip1")
    public void videoPreview1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        preview(request,response,"F:\\迅雷下载2\\00后软萌小萝莉约小帅哥直播啪啪\\00后软萌小萝莉约小帅哥直播啪啪.mp4");
    }
    @GetMapping("/vip2")
    public void videoPreview2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        preview(request,response,"F:\\迅雷下载2\\重金购得逼逼超粉嫩的抖音萌妹子\\重金购得逼逼超粉嫩的抖音萌妹子（白袜袜格罗丫）裸舞与粉丝乳交足交无套啪啪.mp4");
    }
    @GetMapping("/vip3")
    public void videoPreview3(HttpServletRequest request, HttpServletResponse response) throws Exception {
        preview(request,response,"D:\\movie.mp4");
    }
    private void preview(HttpServletRequest request, HttpServletResponse response,String path) throws Exception {
//        String path = request.getParameter("path");
        Path filePath = Paths.get(path);
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
