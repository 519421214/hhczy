package com.king.hhczy.common.util;

import java.util.UUID;

/**
 * @author ningjinxiang
 */
public class RandomUtil {
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
