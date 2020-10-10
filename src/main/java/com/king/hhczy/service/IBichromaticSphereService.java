package com.king.hhczy.service;

import com.king.hhczy.entity.BichromaticSphere;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 双色球开奖结果 服务类
 * </p>
 *
 * @author ningjinxiang
 * @since 2020-09-15
 */
public interface IBichromaticSphereService extends IService<BichromaticSphere> {
    String analyse(String[] redNos,int blueNo);
}
