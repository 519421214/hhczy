package com.king.hhczy.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxGroupBuyDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 序号
     */
    private Integer no;

    private String name;

    private Integer wantNumber;

    /**
     * 1true0false
     */
    private Boolean getMoney;

    /**
     * 1true0false
     */
    private Boolean getGoods;

    private LocalDateTime createTime;

    private String lastEditor;

    private LocalDateTime updateTime;


}
