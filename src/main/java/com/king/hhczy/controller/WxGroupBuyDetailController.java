package com.king.hhczy.controller;


import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.model.WxGroupBuyDetailModel;
import com.king.hhczy.service.IWxGroupBuyDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
@RestController
@RequestMapping("/wx-group-buy-detail")
public class WxGroupBuyDetailController {
    @Autowired
    private IWxGroupBuyDetailService groupBuyDetailService;
    @GetMapping("/list")
    public RespBody list(Integer groupBuyId) {
        return groupBuyDetailService.listByGroupBuyId(groupBuyId);
    }
    @PostMapping("/edit")
    public RespBody edit(@RequestBody WxGroupBuyDetailModel groupBuyDetailModel) {
        return groupBuyDetailService.edit(groupBuyDetailModel);
    }
}

