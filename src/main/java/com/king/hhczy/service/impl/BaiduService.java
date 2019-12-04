package com.king.hhczy.service.impl;

import com.baidu.aip.ocr.AipOcr;
import com.king.hhczy.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-12-04
 */
@Component
@Slf4j
public class BaiduService{
    //自定义成功返回码
    private Integer SUCCESS_CODE=0;
    //每日超出限额错误码
    private Integer REACHED_ERROR_CODE=17;
    //图片尺寸超出范围
    private Integer IMAGE_SIZE_ERROR_CODE=216202;
    @Autowired
    private AipOcr aipOcr;
    /**
     * ocr图片的中英文
     * @param image 图片字节
     * @return
     */
    public Map ocr(byte[] image) {
        String msg = null;
        int code = 0;
        //参数配置
        HashMap<String, String> options = new HashMap();
        options.put("language_type", "CHN_ENG"); //中英语言
//        options.put("detect_direction", "false");//是否检测图像朝向 以下默认值false
//        options.put("detect_language", "false");//是否检测语言
//        options.put("probability", "false");//是否返回识别结果中每一行的置信度

        //高精度，免费次数500次/天
        JSONObject res = aipOcr.basicAccurateGeneral(image, options);
        //如果超过次数，换接口
        if (res.has("error_code")) {
            code = res.getInt("error_code");
            if (code ==REACHED_ERROR_CODE) {
                log.warn("高精度ocr次数已用完");
                //通用ocr，免费次数50000次/天
                res = aipOcr.basicGeneral(image, options);
                code = SUCCESS_CODE;
            }else if(code ==IMAGE_SIZE_ERROR_CODE){
                msg = "OCR识别图片尺寸超出范围";
            }else {
                msg = res.getString("error_msg");
            }
            log.error(msg);
        }
        try {
            Map<String, Object> result = JsonUtils.json2map(res.toString());
            result.put("error_code",code);
            result.put("error_msg",msg);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
