package com.king.hhczy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.domain.WxGroupBuyDetail;
import com.king.hhczy.entity.model.WxGroupBuyDetailModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
public interface IWxGroupBuyDetailService extends IService<WxGroupBuyDetail> {
    RespBody listByGroupBuyId(Integer groupBuyId);

    RespBody edit(WxGroupBuyDetailModel groupBuyDetailModel);
}
