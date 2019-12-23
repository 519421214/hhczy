package com.king.hhczy.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.net.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT:Josn web tokens
 * 组成：头部(HEADER)+载荷(PAYLOAD)+签证(SIGNATURE)
 * JWT 的最大缺点是，由于服务器不保存 session 状态，因此无法在使用过程中废止某个 token，或者更改 token 的权限。也就是说，一旦 JWT 签发了，在到期之前就会始终有效，除非服务器部署额外的逻辑
 * JWT 本身包含了认证信息，一旦泄露，任何人都可以获得该令牌的所有权限。为了减少盗用，JWT 的有效期应该设置得比较短。对于一些比较重要的权限，使用时应该再次对用户进行认证
 * 为了减少盗用，JWT 不应该使用 HTTP 协议明码传输，要使用 HTTPS 协议传输
 * @author ningjinxiang
 */
public class JwtUtils {
	/**
	 * JWT_WEB_TTL：WEBAPP应用中token的有效时间,默认30分钟
	 */
	public static final long JWT_WEB_TTL = 30 * 60 * 1000;

	/**
	 * 将jwt令牌保存到header中的key
	 */
	public static final String JWT_HEADER_KEY = "jwt";

	// 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
	private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
	private static final String JWT_SECRET = "20180307";// JWT密匙
	private static final SecretKey JWT_KEY;// 使用JWT密匙生成的加密key

	static {
		byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
		JWT_KEY = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
	}

	private JwtUtils() {
	}

	/**
	 * 解密jwt，获得所有声明(包括标准和私有声明)
	 *
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public static Claims parseJwt(String jwt) {
		Claims claims = Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(jwt).getBody();
		return claims;
	}

	/**
	 * 创建JWT令牌，签发时间为当前时间
	 *
	 * @param claims
	 *            创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
	 * @param ttlMillis
	 *            JWT的有效时间(单位毫秒)，当前时间+有效时间=过期时间
	 * @return jwt令牌
	 */
	public static String createJwt(Map<String, Object> claims, long ttlMillis) {
		// 生成JWT的时间，即签发时间
		long nowMillis = System.currentTimeMillis();

		// 下面就是在为payload添加各种标准声明和私有声明了
		// 这里其实就是new一个JwtBuilder，设置jwt的body
		JwtBuilder builder = Jwts.builder()
				// 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				.setClaims(claims)
				// 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				// 可以在未登陆前作为身份标识使用
				.setId(UUID.randomUUID().toString().replace("-", ""))
				// iss(Issuser)签发者，写死
				// .setIssuer("zking")
				// iat: jwt的签发时间
				.setIssuedAt(new Date(nowMillis))
				// 代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可放数据{"uid":"zs"}。此处没放
				// .setSubject("{}")
				// 设置签名使用的签名算法和签名使用的秘钥
				.signWith(SIGNATURE_ALGORITHM, JWT_KEY)
				// 设置JWT的过期时间
				.setExpiration(new Date(nowMillis + ttlMillis));

		return builder.compact();
	}

	/**
	 * 复制jwt，并重新设置签发时间(为当前时间)和失效时间
	 *
	 * @param jwt
	 *            被复制的jwt令牌
	 * @param ttlMillis
	 *            jwt的有效时间(单位毫秒)，当前时间+有效时间=过期时间
	 * @return
	 */
	public static String copyJwt(String jwt, Long ttlMillis) {
		Claims claims = parseJwt(jwt);

		// 生成JWT的时间，即签发时间
		long nowMillis = System.currentTimeMillis();

		// 下面就是在为payload添加各种标准声明和私有声明了
		// 这里其实就是new一个JwtBuilder，设置jwt的body
		JwtBuilder builder = Jwts.builder()
				// 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				.setClaims(claims)
				// 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				// 可以在未登陆前作为身份标识使用
				//.setId(UUID.randomUUID().toString().replace("-", ""))
				// iss(Issuser)签发者，写死
				// .setIssuer("zking")
				// iat: jwt的签发时间
				.setIssuedAt(new Date(nowMillis))
				// 代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可放数据{"uid":"zs"}。此处没放
				// .setSubject("{}")
				// 设置签名使用的签名算法和签名使用的秘钥
				.signWith(SIGNATURE_ALGORITHM, JWT_KEY)
				// 设置JWT的过期时间
				.setExpiration(new Date(nowMillis + ttlMillis));
		return builder.compact();
	}
}
