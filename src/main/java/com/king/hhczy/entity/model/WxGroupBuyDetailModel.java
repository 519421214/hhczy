package com.king.hhczy.entity.model;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 * GBVHJXC
 * @author ningjinxiang
 * @since 2019-10-17
 */
@Data
public class WxGroupBuyDetailModel {

    private Integer id;
    private Integer lastEditorId;

    /**
     * 1true0false
     */
    private Boolean getMoney;

    /**
     * 1true0false
     */
    private Boolean getGoods;

    private Integer wantNum;

}
