package com.king.hhczy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.domain.WxUser;
import com.king.hhczy.entity.model.WxUserModel;
import com.king.hhczy.mapper.WxUserMapper;
import com.king.hhczy.service.IWxUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-17
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements IWxUserService {

    public RespBody notExistToSave(WxUserModel userModel){
        RespBody<Object> respBody = new RespBody<>();
        QueryWrapper<WxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("code", userModel.getCode());
        WxUser one = this.getOne(wrapper);
        if (ObjectUtils.isEmpty(one)) {
            WxUser wxUser = new WxUser();
            wxUser.setCode(userModel.getCode());
            wxUser.setName(userModel.getName());
            wxUser.setCreateTime(LocalDateTime.now());
            wxUser.setUpdateTime(LocalDateTime.now());
            this.save(wxUser);
        }
        return respBody;
    }
}
