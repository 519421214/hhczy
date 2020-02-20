package com.king.hhczy.controller;

import com.king.hhczy.common.util.PaChongUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ningjinxiang
 * @ClassName: ApiController
 * @Description: 对外服务提供controller
 * @date 2020年2月19日
 * by ningjinxiang
 */
@RestController
@Slf4j
@Validated
@Api(value = "爬虫层接口")
@RequestMapping(value = "/pa-chong")
public class PachongController {
    @Autowired
    private PaChongUtil paChongUtil;

    @GetMapping("/gov-news")
    public void html() {
        paChongUtil.govNews();
    }
}
