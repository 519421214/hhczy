package com.king.hhczy.controller;

import com.baidu.aip.ocr.AipOcr;
import com.king.hhczy.common.util.JsonUtils;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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
        //初始化百度接口
        AipOcr client = new AipOcr("17853784","qFBpBnn3Ch3MkQT5GzIRceHG","znbNcfzv7tMOlHpFxZwrTwaYxdnyuBGt");
        //首选参数
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG");    //中英语言
        JSONObject res = client.basicGeneral(buf, options);
        System.out.println(res.toString());
        Map map = JsonUtils.json2map(res.toString());
        return map;
    }
}

