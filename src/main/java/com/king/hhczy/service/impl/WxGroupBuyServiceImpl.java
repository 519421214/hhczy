package com.king.hhczy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.hhczy.common.result.BaseResultCode;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.domain.WxGroupBuy;
import com.king.hhczy.entity.domain.WxGroupBuyDetail;
import com.king.hhczy.entity.domain.WxUser;
import com.king.hhczy.entity.model.WxGroupBuyModel;
import com.king.hhczy.mapper.WxGroupBuyMapper;
import com.king.hhczy.mapper.WxUserMapper;
import com.king.hhczy.service.IWxGroupBuyDetailService;
import com.king.hhczy.service.IWxGroupBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
@Service
public class WxGroupBuyServiceImpl extends ServiceImpl<WxGroupBuyMapper, WxGroupBuy> implements IWxGroupBuyService {
    @Autowired
    private WxUserMapper userMapper;
    @Autowired
    private IWxGroupBuyDetailService groupBuyDetailService;
    @Override
    public RespBody listAllOfAllow(Integer userId) {
         RespBody respBody = new RespBody<>();

        QueryWrapper<WxUser> userWrapper = new QueryWrapper<>();
        userWrapper.like("share_to", "/".concat(userId.toString()).concat("/"));
        List<WxUser> wxUsers = userMapper.selectList(userWrapper);
        List<Integer> creatorsId = new ArrayList<>();
        if (!ObjectUtils.isEmpty(wxUsers)) {
            //查询所有有权限的
            creatorsId = wxUsers.stream().map(x -> x.getId()).collect(Collectors.toList());
        }
        //加上自己的
        creatorsId.add(userId);
        QueryWrapper<WxGroupBuy> gbWrapper = new QueryWrapper<>();
        gbWrapper.in("creator_id",creatorsId);
        respBody.result(BaseResultCode.SUCCESS);
        respBody.setData(this.list(gbWrapper));
        return respBody;
    }

    @Override
    @Transactional
    public RespBody add(WxGroupBuyModel groupBuyModel) {
        RespBody<Object> respBody = new RespBody<>();
        WxGroupBuy groupBuy = new WxGroupBuy();
        groupBuy.setCreateTime(LocalDateTime.now());
        groupBuy.setUpdateTime(LocalDateTime.now());
        groupBuy.setCreatorId(groupBuyModel.getCreatorId());
        groupBuy.setTitle(groupBuyModel.getTitle());
        this.save(groupBuy);

        String contents = groupBuyModel.getContents();
        if (StringUtils.hasText(contents)) {
            List<WxGroupBuyDetail> groupBuyDetails = new ArrayList<>();
            String[] split = contents.split("\n");

            for (int i = 1; i < split.length; i++) {
                Pattern pattern = Pattern.compile("\\d+.*");//一定记住加“.”
                if (!pattern.matcher(split[i]).matches()) {
                    continue;
                }

                WxGroupBuyDetail wxGroupBuyDetail = new WxGroupBuyDetail();
                wxGroupBuyDetail.setGroupBuyId(groupBuy.getId());
                wxGroupBuyDetail.setContent(split[i]);
                wxGroupBuyDetail.setCreateTime(LocalDateTime.now());
                wxGroupBuyDetail.setUpdateTime(LocalDateTime.now());
                groupBuyDetails.add(wxGroupBuyDetail);
            }
            if (groupBuyDetails.size()>0) {
                groupBuyDetailService.saveBatch(groupBuyDetails);
            }
            respBody.result(BaseResultCode.SUCCESS);
            return respBody;
        }
        return null;
    }
}
