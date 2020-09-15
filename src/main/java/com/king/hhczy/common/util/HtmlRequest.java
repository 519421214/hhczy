package com.king.hhczy.common.util;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 扒一个网站的源码
 */
public class HtmlRequest {
    public static void main(String[] args) {
        try {
            /*
             * 请求有权威证书的地址
             */
            String requestPath = "https://www.baidu.com/";
            HttpsURLConnection connection = getHttpsURLConnection(requestPath, "GET");
            String result = new String(readInputStream(connection.getInputStream()));
            System.out.println("GET1返回结果：" + result);

            /*
             * 请求自定义证书的地址
             */
            //获取信任库
            KeyStore trustStore = getkeyStore("jks", "d:/temp/cacerts", "123456");

            //不需客户端证书
            requestPath = "https://x.x.x.x:9010/zsywservice";
            result = getHttpsURLConnection(requestPath, "GET", null, null, trustStore);
            System.out.println("GET2返回结果：" + result);

            //需客户端证书
            requestPath = "https://x.x.x.x:9016/zsywservice";
            KeyStore keyStore = getkeyStore("pkcs12", "d:/client.p12", "123456");
            result = getHttpsURLConnection(requestPath, "GET", keyStore, "123456", trustStore);
            System.out.println("GET3返回结果：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取证书
     * @return
     */
    private static KeyStore getkeyStore(String type, String filePath, String password) {
        KeyStore keySotre = null;
        try(FileInputStream in = new FileInputStream(new File(filePath))) {
            keySotre = KeyStore.getInstance(type);
            keySotre.load(in, password.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keySotre;
    }

    private static HttpsURLConnection getHttpsURLConnection(String uri, String method) throws Exception {
        URL url = new URL(uri);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    /**
     * 获取连接
     * @param uri
     * @param method
     * @param keyStore
     * @param trustStore
     * @return
     * @throws Exception
     */
    public static String getHttpsURLConnection(String uri, String method, KeyStore keyStore, String keyStorePassword, KeyStore trustStore) throws Exception {
        KeyManager[] keyManagers = null;
        TrustManager[] trustManagers = null;
        if (keyStore != null) {
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
            keyManagers = keyManagerFactory.getKeyManagers();
        }
        if (trustStore != null) {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);
            trustManagers = trustManagerFactory.getTrustManagers();
        } else {
            trustManagers = new TrustManager[] { new DefaultTrustManager()};
        }

        //设置服务端支持的协议
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(keyManagers, trustManagers, null);
        SSLSocketFactory sslFactory = context.getSocketFactory();

        URL url = new URL(uri);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sslFactory);
        //验证URL的主机名和服务器的标识主机名是否匹配
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //if ("xxx".equals(hostname)) {
                //    return true;
                //} else {
                //    return false;
                //}
                return true;
            }
        });
        connection.setRequestMethod(method);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return new String(readInputStream(connection.getInputStream()));
//        return connection;
    }

    /**
     * 默认的TrustManager实现，不安全
     */
    private static final class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /**
     * 通过网站域名URL获取该网站的源码
     * @return String
     * @throws Exception
     */
    public static String getURLSource(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60 * 1000);
            InputStream inStream =  conn.getInputStream();  //通过输入流获取html二进制数据
            byte[] data = readInputStream(inStream);        //把二进制数据转化为byte字节数据
            String htmlSource = new String(data);
            inStream.close();
            return htmlSource;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }
    /**
     * 通过网站域名URL获取该网站的源码 (乱码问题未解决)
     * @return String
     * @throws Exception
     */
    public static String getHttpsURLSource(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60 * 1000);
            InputStream inStream =  conn.getInputStream();  //通过输入流获取html二进制数据
            byte[] data = readInputStream(inStream);        //把二进制数据转化为byte字节数据
            String htmlSource = new String(data);
            inStream.close();
            return htmlSource;
        } catch (Exception e) {
//            e.printStackTrace();
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
