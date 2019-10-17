package com.king.hhczy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.hhczy.common.result.MyPage;
import com.king.hhczy.common.result.RespBody;
import com.king.hhczy.entity.domain.TblAccount;
import com.king.hhczy.mapper.TblAccountMapper;
import com.king.hhczy.service.ITblAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 子平台登录账户信息 服务实现类
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-08-08
 */
@Service
public class TblAccountServiceImpl extends ServiceImpl<TblAccountMapper, TblAccount> implements ITblAccountService {
    @Autowired
    private TblAccountMapper accountMapper;

    @Override
    public RespBody testSpringCache(Integer areaId) {
        System.out.println("从数据库拿到数据");
        return null;
    }

    @Override
    public RespBody listAccounts() {
//        QueryWrapper<TblAccount> wrapper = new QueryWrapper();
        this.list();
        //分页查询1
        IPage<TblAccount> iPage = new MyPage(1,20);
//        IPage<TblAccount> iPage2 = new MyPage(1,20,false);//不关心总数，提升性能
//        QueryWrapper<TblAccount> wrapper = new QueryWrapper();
//        wrapper.eq("company_code", 123);
        IPage<TblAccount> page = this.page(iPage, null);
        //mybatis分页查询方法2，关联查询
        IPage<Map> page2 = accountMapper.listAccounts(iPage);

        Map longLongMap = accountMapper.listInteger();
        int id = Integer.parseInt(longLongMap.get("id").toString());
        //分页查询3 由于PageHelper跟mybatis包冲突，这里测不了
//        PageHelper.startPage(1, 20);
//        List<Map> alarms = accountMapper.listAccounts();
//        PageInfo pageInfo = new PageInfo<>(alarms);

        return null;
    }
}
