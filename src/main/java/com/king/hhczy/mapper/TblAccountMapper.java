package com.king.hhczy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.king.hhczy.entity.domain.TblAccount;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 子平台登录账户信息 Mapper 接口
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-08-08
 */
public interface TblAccountMapper extends BaseMapper<TblAccount> {
    @Select("select * from tbl_account")
    IPage<Map> listAccounts(IPage page);
    @Select("select * from tbl_account")
    List listAccounts();
}
