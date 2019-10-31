package com.king.hhczy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.domain.WxUser;
import com.king.hhczy.entity.model.WxUserUpdateModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
public interface IWxUserService extends IService<WxUser> {
    RespBody<Integer> getUserId(String code);
    RespBody addSharer(Integer userId,Integer sharerId);
    RespBody getSharers(Integer userId);
    RespBody updateBaseInfo(WxUserUpdateModel userUpdateModel);
    String getSaveToken(String code);
}
