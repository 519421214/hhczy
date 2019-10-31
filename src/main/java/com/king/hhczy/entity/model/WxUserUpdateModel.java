package com.king.hhczy.entity.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-19
 */
@Data
public class WxUserUpdateModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String nickName;
    private String avatarUrl;
    private String country;


    /**
     * 1男
     */
    private Boolean gender;

    private String language;

    private String province;

    private String city;

    private LocalDateTime updateTime;


}
