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
    @RequestMapping("/chat")
    public String toGSChat(Model model, HttpServletRequest request) {
        String netUrl = "ws://" + request.getServerName() + "/chat/";
        if ("https".equals(request.getScheme())) {
            netUrl = "wss://" + request.getServerName() + "/chat/";
        }
        String netName = "李"+i+++"肠";
        model.addAttribute("netUrl", netUrl);
        model.addAttribute("netName", netName);
        return "/websocket/chat";
    }
}
