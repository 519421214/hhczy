package com.king.hhczy.entity.model;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-17
 */
@Data
public class WxGroupBuyDetailModel {

    private Integer id;

    /**
     * 1true0false
     */
    private Boolean getMoney;

    /**
     * 1true0false
     */
    private Boolean getGoods;

    private String userCode;

}
