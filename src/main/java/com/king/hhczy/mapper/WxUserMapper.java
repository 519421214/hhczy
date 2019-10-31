package com.king.hhczy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.hhczy.entity.domain.WxUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
public interface WxUserMapper extends BaseMapper<WxUser> {
    @Select("SELECT u2.id,u2.nick_name 'nickName' FROM wx_user u1 INNER JOIN wx_user u2 ON u1.share_to LIKE CONCAT('%/',u2.id,'/%') WHERE u1.id=#{userId}")
    List<Map> getSharersByUserId(@Param("userId") Integer userId);
    @Update("UPDATE wx_user set share_to=CONCAT(share_to,#{sharerId},'/') where id=#{userId}")
    int updateSharersById(@Param("userId") Integer userId,@Param("sharerId") Integer sharerId);
}
