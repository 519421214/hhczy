package com.king.hhczy.common.util;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ningjinxiang
 */
public class MyBase64Utils {

	public static void main(String[] args) {
		String s = fileToBase64("C:\\Users\\ningjinxiang\\Desktop\\aaa.zip");
		Path path = Paths.get("C:\\Users\\ningjinxiang\\Desktop\\secret.txt");
		try {
			Files.write(path, s.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param path
	 * @return String
	 * @description 将文件转base64字符串
	 * @date 2018年3月20日
	 * @author changyl
	 * File转成编码成BASE64
	 */
	public static String fileToBase64(String path) {
		String base64 = null;
		InputStream in = null;
		try {
			File file = new File(path);
			in = new FileInputStream(file);
			byte[] bytes=new byte[(int)file.length()];
			in.read(bytes);
			base64 = Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return base64;
	}
	/**
	 * base64转文件并输出到指定目录
	 * @param base64Str
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	public static byte[] decode(String base64Str,String fileName,String filePath){
		File file = null;
		//创建文件目录
		File  dir=new File(filePath);
		if (!dir.exists() && !dir.isDirectory()) {
			dir.mkdirs();
		}
		BufferedOutputStream bos = null;
		java.io.FileOutputStream fos = null;

		byte[] b = null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			b = decoder.decodeBuffer(replaceEnter(base64Str));
			//window
			//file=new File(filePath+"\\"+fileName);
			//linux
			file=new File(filePath+"/"+fileName);
			fos = new java.io.FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return b;
	}
	public static String replaceEnter(String str){
		String reg ="[\n-\r]";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}
}
