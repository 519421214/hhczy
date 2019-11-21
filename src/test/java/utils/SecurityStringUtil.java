package utils;


import org.springframework.util.StringUtils;

/**
 * 数据库加密、解密(按照部门其他同事那一套)
 * 《简化代码版本》
 * @date 2019/03/27
 * @author ningjinxiang
 */

public class SecurityStringUtil {
	/*
	 * 加密规则： 将每个字符转成对应的ascii分值，不足百位的用0补足
	 */
	private static boolean securityString=true;

	public static String encodeString(String source) {// 加密
		if (StringUtils.hasText(source)) {
			return "";
		}
		if (securityString) {
			return source.chars().boxed().map(x->String.format("%03d",x)).reduce("000",String::concat);
		}else {
			return source;
		}
	}

	public static String decodeString(String source) {// 解密
		if (StringUtils.hasText(source)) {
			return "";
		}
		if (securityString) {
			StringBuilder builder = new StringBuilder();
			source = source.trim();
			int length = source.length();
			for (int i = 0; i < length; i = i + 3) {
				String numStr = source.substring(i, length >= (i+3) ? (i+3):length);
				char charAt= (char)(Integer.valueOf(numStr)).intValue();
				builder.append(charAt);
			}
			return builder.toString();
		}else {
			return source;
		}
		
	}

	/**
	 * 是否是32-126 ASCLL
	 * @param s 待校验值
	 * by ningjinxiang
	 * @return true：通过检验，数值合法
	 */
	public static boolean ifUseAcsll(String s) {
		if (StringUtils.hasText(s)) {
			int i = s.chars().parallel().filter(x -> x > 126 || x < 32).findAny().orElse(0);
			if (i!=0) {
				return false;
			}
		}
		return true;
	}

}
