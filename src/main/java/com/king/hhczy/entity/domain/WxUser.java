package com.king.hhczy.entity.domain;

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
 * @since 2019-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

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

    private String openid;

    private String shareTo;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}