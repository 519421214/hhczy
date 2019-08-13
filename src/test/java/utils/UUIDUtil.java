package utils;

import java.util.UUID;

/**
 * @author ningjinxiang
 */
public class UUIDUtil {
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
