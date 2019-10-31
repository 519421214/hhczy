package com.king.hhczy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.domain.WxGroupBuy;
import com.king.hhczy.entity.model.WxGroupBuyModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
public interface IWxGroupBuyService extends IService<WxGroupBuy> {
    RespBody listAllOfAllow(Integer userId);
    RespBody add(WxGroupBuyModel groupBuyModel);
}
