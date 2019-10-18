package com.king.hhczy.controller;


import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.model.WxUserModel;
import com.king.hhczy.service.IWxUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-17
 */
@RestController
@RequestMapping("/wx-user")
public class WxUserController {
    private IWxUserService userService;
    @PostMapping("save")
    public RespBody save(WxUserModel userModel){
        return userService.notExistToSave(userModel);
    }
}

