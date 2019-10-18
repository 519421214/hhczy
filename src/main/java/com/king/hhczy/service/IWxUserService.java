package com.king.hhczy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.domain.WxUser;
import com.king.hhczy.entity.model.WxUserModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-17
 */
public interface IWxUserService extends IService<WxUser> {
    RespBody notExistToSave(WxUserModel userModel);
}
