package com.king.hhczy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.hhczy.common.result.BaseResultCode;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.common.util.Log;
import com.king.hhczy.common.util.UUIDUtil;
import com.king.hhczy.common.util.WxAppUtil;
import com.king.hhczy.entity.domain.WxUser;
import com.king.hhczy.entity.model.WxUserUpdateModel;
import com.king.hhczy.mapper.RestTemplateMapper;
import com.king.hhczy.mapper.WxUserMapper;
import com.king.hhczy.service.IWxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements IWxUserService {
    @Autowired
    private RestTemplateMapper restTemplateMapper;
    @Autowired
    private WxUserMapper userMapper;
//    @Autowired
//    private RedissonClient redissonClient;

    @Override
    public RespBody<Integer> getUserId(String code) {
        RespBody<Integer> respBody = new RespBody();
        //远程获取appid
        JSONObject result = restTemplateMapper.getResult(WxAppUtil.code2SessionUrl,
                new String[]{"appid=" + WxAppUtil.appId, "secret=" + WxAppUtil.secret, "js_code=" + code, "grant_type=authorization_code"});

        String openid = result.getString("openid");
        QueryWrapper<WxUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        WxUser wxUser = this.getOne(queryWrapper);
        if (wxUser==null) {
            wxUser = new WxUser();
            wxUser.setOpenid(openid);
            wxUser.setShareTo("/");
            wxUser.setCreateTime(LocalDateTime.now());
            wxUser.setUpdateTime(LocalDateTime.now());
            this.save(wxUser);
        }
        respBody.result(BaseResultCode.SUCCESS);
        respBody.setData(wxUser.getId());
        return respBody;
    }

    @Override
    public RespBody addSharer(Integer userId,Integer sharerId) {
        sharerId = sharerId - 10000;
        RespBody respBody = new RespBody();
        if (sharerId==userId) {
            respBody.setMsg("不必共享给自己");
        }else {
            WxUser sharerUser = userMapper.selectById(sharerId);
            WxUser curUser = userMapper.selectById(userId);
            if (sharerUser!=null) {
                String shareTo = curUser.getShareTo();
                if (shareTo==null||shareTo.indexOf("/".concat(sharerId.toString()).concat("/"))==-1) {
                    if (userMapper.updateSharersById(userId,sharerId)>0) {
                        respBody.result(BaseResultCode.SUCCESS);
                        respBody.setData(userMapper.getSharersByUserId(userId));
                        return respBody;
                    }else {
                        respBody.setMsg("数据库异常");
                    }
                }else {
                    respBody.setMsg("该用户已共享");
                }

            }else {
                respBody.setMsg("该用户ID不存在");
            }
        }
        respBody.setCode(BaseResultCode.FAILED.getCode());
        return respBody;
    }

    @Override
    public RespBody getSharers(Integer userId) {
        RespBody respBody = new RespBody();
        List<Map> sharers = userMapper.getSharersByUserId(userId);
        respBody.result(BaseResultCode.SUCCESS);
        respBody.setData(sharers);
        return respBody;
    }

    @Override
    public RespBody updateBaseInfo(WxUserUpdateModel userUpdateModel) {
        RespBody respBody = new RespBody();
        WxUser wxUser = this.getById(userUpdateModel.getUserId());
        wxUser.setUpdateTime(LocalDateTime.now());
        wxUser.setAvatarUrl(userUpdateModel.getAvatarUrl());
        wxUser.setCity(userUpdateModel.getCity());
        wxUser.setCountry(userUpdateModel.getCountry());
        wxUser.setGender(userUpdateModel.getGender());
        wxUser.setLanguage(userUpdateModel.getLanguage());
        wxUser.setNickName(userUpdateModel.getNickName());
        wxUser.setProvince(userUpdateModel.getProvince());
        if (this.updateById(wxUser)) {
            respBody.result(BaseResultCode.SUCCESS);
            Log.info("个人数据更新成功");
        }else {
            respBody.result(BaseResultCode.FAILED);
            Log.debug("个人数据更新失败");
        }
        return respBody;
    }

    @Override
    public String getSaveToken(String code) {
        //远程获取appid
        JSONObject result = restTemplateMapper.getResult(WxAppUtil.code2SessionUrl,
                new String[]{"appid=" + WxAppUtil.appId, "secret=" + WxAppUtil.secret, "js_code=" + code, "grant_type=authorization_code"});

        String openid = result.getString("openid");
        QueryWrapper<WxUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        WxUser wxUser = this.getOne(queryWrapper);
        if (wxUser==null) {
            wxUser = new WxUser();
            wxUser.setOpenid(openid);
            wxUser.setCreateTime(LocalDateTime.now());
            wxUser.setUpdateTime(LocalDateTime.now());
            this.save(wxUser);
        }
        String token = UUIDUtil.uuid();
//        RMapCache<String, Integer> mapCache = redissonClient.getMapCache(CacheConstants.INIT_CACHE_PREFIX + CacheConstants.WECHAT + CacheConstants.USER);
        //设置token有效时间
//        mapCache.put(token, wxUser.getId(),30,TimeUnit.MINUTES,10,TimeUnit.MINUTES);
        return token;
    }
}
