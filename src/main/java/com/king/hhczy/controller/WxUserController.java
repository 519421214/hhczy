package com.king.hhczy.controller;


import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.model.WxUserUpdateModel;
import com.king.hhczy.service.IWxUserService;
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
@RequestMapping("/wx-user")
public class WxUserController {

    @Autowired
    private IWxUserService userService;

    @GetMapping("/login")
    public String doLogin(String code) {
        return userService.getSaveToken(code);
    }
    @GetMapping("/getUserId")
    public RespBody<Integer> getUserId(String code) {
        return userService.getUserId(code);
    }
    @GetMapping("/addSharer")
    public RespBody addSharer(Integer userId,Integer sharerId) {
        return userService.addSharer(userId,sharerId);
    }
    @GetMapping("/getSharers")
    public RespBody getSharers(Integer userId) {
        return userService.getSharers(userId);
    }
    @PostMapping("/update")
    public RespBody update(@RequestBody WxUserUpdateModel wxUser) {

        return userService.updateBaseInfo(wxUser);
    }
}

