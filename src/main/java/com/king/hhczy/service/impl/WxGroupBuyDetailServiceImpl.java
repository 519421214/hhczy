package com.king.hhczy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.hhczy.common.result.BaseResultCode;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.domain.WxGroupBuyDetail;
import com.king.hhczy.entity.model.WxGroupBuyDetailModel;
import com.king.hhczy.mapper.WxGroupBuyDetailMapper;
import com.king.hhczy.service.IWxGroupBuyDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
@Service
public class WxGroupBuyDetailServiceImpl extends ServiceImpl<WxGroupBuyDetailMapper, WxGroupBuyDetail> implements IWxGroupBuyDetailService {

    @Override
    public RespBody listByGroupBuyId(Integer groupBuyId) {
        RespBody respBody = new RespBody<>();
        QueryWrapper<WxGroupBuyDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("group_buy_id", groupBuyId);
        respBody.result(BaseResultCode.SUCCESS);
        respBody.setData(this.list(wrapper));
        return respBody;
    }

    @Override
    public RespBody edit(WxGroupBuyDetailModel groupBuyDetailModel) {
        RespBody respBody = new RespBody<>();
        WxGroupBuyDetail wxGroupBuyDetail = new WxGroupBuyDetail();
        BeanUtils.copyProperties(groupBuyDetailModel,wxGroupBuyDetail);
        wxGroupBuyDetail.setUpdateTime(LocalDateTime.now());
        this.updateById(wxGroupBuyDetail);
        respBody.result(BaseResultCode.SUCCESS);
        return respBody;
    }
}
