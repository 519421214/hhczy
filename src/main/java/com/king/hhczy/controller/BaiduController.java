package com.king.hhczy.controller;

import com.king.hhczy.service.impl.BaiduService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public Map ocr(MultipartFile file) throws Exception{
        //接收图像二进制数据
        byte[] buf = file.getBytes();
        return baiduService.ocr(buf);
    }
}

