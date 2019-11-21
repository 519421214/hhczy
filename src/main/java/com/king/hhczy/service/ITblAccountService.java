package com.king.hhczy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.domain.TblAccount;

/**
 * <p>
 * 子平台登录账户信息 服务类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-08-08
 */
public interface ITblAccountService extends IService<TblAccount> {
    RespBody testSpringCache(Integer areaId);
    RespBody listAccounts();
}
