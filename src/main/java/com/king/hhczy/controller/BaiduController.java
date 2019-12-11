package com.king.hhczy.controller;

import com.king.hhczy.service.impl.BaiduService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
@RestController
@RequestMapping("/baidu")
public class BaiduController {
    @Autowired
    private BaiduService baiduService;
    /**
     * 图片文字识别、中英文识别
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/ocr")
    public ResponseEntity<Map> ocr(MultipartFile file) throws Exception{
        //接收图像二进制数据
        byte[] buf = file.getBytes();
        return ResponseEntity.ok(baiduService.ocr(buf));
    }
    /**
     * 图片文字识别、中英文识别
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/ocr-image")
    public ResponseEntity<Map> ocrImage(HttpServletRequest request) throws Exception{
        //获取从前台传过来得图片
        MultipartHttpServletRequest req =(MultipartHttpServletRequest)request;
        MultipartFile file = req.getFile("file");
        //接收图像二进制数据
        byte[] buf = file.getBytes();
        return ResponseEntity.ok(baiduService.ocr(buf));
    }
}