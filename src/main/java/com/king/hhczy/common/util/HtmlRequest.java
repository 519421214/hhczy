package com.king.hhczy.common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 扒一个网站的源码
 */
public class HtmlRequest {
    /** *//**
     * @param args
     */
    public static void main(String[] args) throws Exception{
        String url = "http://www.ifeng.com";
        String urlsource = getURLSource(url);
        System.out.println(urlsource);
    }

    /** *//**
     * 通过网站域名URL获取该网站的源码
     * @return String
     * @throws Exception
     */
    public static String getURLSource(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            InputStream inStream =  conn.getInputStream();  //通过输入流获取html二进制数据
            byte[] data = readInputStream(inStream);        //把二进制数据转化为byte字节数据
            String htmlSource = new String(data);
            return htmlSource;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** *//**
     * 把二进制流转化为byte字节数组
     * @param instream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream instream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]  buffer = new byte[1204];
        int len = 0;
        while ((len = instream.read(buffer)) != -1){
            outStream.write(buffer,0,len);
        }
        instream.close();
        return outStream.toByteArray();
    }
}
