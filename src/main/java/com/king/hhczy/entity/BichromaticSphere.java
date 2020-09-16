package com.king.hhczy.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 双色球开奖结果
 * </p>
 *
 * @author ningjinxiang
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BichromaticSphere implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 期数
     */
    private String no;

    private String one;

    private String two;

    private String three;

    private String four;

    private String five;

    private String six;

    private String seven;

    /**
     * 奖池
     */
    private String jackpot;

    /**
     * 一等奖注数
     */
    private Integer firstPrizeSum;

    /**
     * 一等奖金额
     */
    private String firstPrizeBonus;

    /**
     * 二等奖注数
     */
    private Integer secondPrizeSum;

    /**
     * 二等奖奖金
     */
    private String secondPrizeBonus;

    /**
     * 投注金额
     */
    private String bettingAmount;

    /**
     * 开奖日期
     */
    private LocalDate publishTime;

    private LocalDateTime insertTime;

    private LocalDateTime updateTime;


}
