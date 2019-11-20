package com.king.hhczy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.hhczy.entity.domain.WxGroupBuy;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface WxGroupBuyMapper extends BaseMapper<WxGroupBuy> {
    @Select("SELECT gb.id,gb.title,gb.creator_id 'creatorId',u.nick_name 'nickName',gb.number," +
            "gb.last_editor_id 'lastEditorId',DATE_FORMAT(gb.create_time, '%Y-%m-%d %H:%i:%s') 'createTime' from wx_group_buy gb " +
            " INNER JOIN wx_user u on u.id=gb.creator_id WHERE gb.creator_id in(${userIds}) ORDER BY createTime DESC")
    List<Map> listDetail(@Param("userIds") String userIds);
}
