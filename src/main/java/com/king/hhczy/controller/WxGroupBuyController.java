package com.king.hhczy.controller;


import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.model.WxGroupBuyModel;
import com.king.hhczy.service.IWxGroupBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
@RestController
@RequestMapping("/wx-group-buy")
public class WxGroupBuyController {
    @Autowired
    private IWxGroupBuyService groupBuyService;
    @GetMapping("/list")
    public RespBody list(Integer userId) {
        return groupBuyService.listAllOfAllow(userId);
    }
    @PostMapping("/add")
    public RespBody add(@RequestBody@Valid WxGroupBuyModel groupBuyModel) {
        return groupBuyService.add(groupBuyModel);
    }
}

