package com.king.hhczy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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
    private static int i = 0;
    @RequestMapping("/speak")
    public String toGSSpeak(Model model, HttpServletRequest request) {
        String netURL = "ws://" + request.getServerName() + ":6066/gp-read/李"+i+++"肠";
        model.addAttribute("netURL", netURL);
        return "/websocket/index";
    }
}
