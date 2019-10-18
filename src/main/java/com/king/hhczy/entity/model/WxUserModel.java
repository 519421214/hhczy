package com.king.hhczy.entity.model;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-17
 */
@Data
public class WxUserModel implements Serializable {

    private String name;

    private String code;

    private String group;


}
