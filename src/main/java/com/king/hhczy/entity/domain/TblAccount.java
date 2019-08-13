package com.king.hhczy.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TblAccount implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "account_id", type = IdType.AUTO)
    private int accountId;
    private String account;
    private String password;
    private String companyName;
    private String companyAddress;
    private String companyEmail;
    private String companyTelephone;
}
