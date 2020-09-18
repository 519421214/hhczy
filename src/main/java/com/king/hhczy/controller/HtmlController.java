package com.king.hhczy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ningjinxiang
 * @ClassName: ApiController
 * @Description: 对外服务提供controller
 * @date 2019年3月29日
 */
@Controller
@Slf4j
@Validated
public class HtmlController {
    @RequestMapping("/to-gs-speak")
    public String toGSSpeak() {
        return "/websocket/gs-speak";
    }
}
