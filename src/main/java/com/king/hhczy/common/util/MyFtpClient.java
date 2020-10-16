package com.king.hhczy.common.util;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ConnectException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 不支持中文路径 todo
 *
 * @author wobendiankun
 * @author ningjinxiang
 * 20190111
 */
@Component
public class MyFtpClient {
    private static final Logger logger = LoggerFactory.getLogger(MyFtpClient.class);
    //ftp服务器地址
    public static String server;
    // 端口
    @Value("${ftp.port}")
    private int port;
    //用户名
    @Value("${ftp.username}")
    private String username;
    //密码
    @Value("${ftp.password}")
    private String password;
    //目标文件夹
    @Value("${ftp.remote.dir}")
    private String remoteDir;

    @Value("${ftp.host}")
    public void setServer(String server) {
        MyFtpClient.server = server;
    }

    /**
     * 单文件上传
     *
     * @param remoteFileName 远程文件名称
     * @param locaFileName   本地文件名称
     */
    public void upload(String remoteFileName, String locaFileName) {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            //监听日志
//            ftp.addProtocolCommandListener( new PrintCommandListener( new PrintWriter( System.out ), true ) );
            //连接ftp服务器
            connect(ftp);
            //设置属性
            setProperty(ftp);
            //上传文件
            upload(ftp, remoteFileName, locaFileName);
            //退出
            logout(ftp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                }
            }
        }

    }

    /**
     * 多文件上传
     */
    public void uploadAllOfDir(boolean needDelete, String filePath) {
        FTPClient ftp = null;
        try {
            //判断是否有需要上送的文件
            if (Files.list(Paths.get(filePath)).count() == 0) {
                return;
            }
            ftp = new FTPClient();
            //监听日志
//            ftp.addProtocolCommandListener( new PrintCommandListener( new PrintWriter( System.out ), true ) );
            //连接ftp服务器
            connect(ftp);
            //设置属性
            setProperty(ftp);
            DirectoryStream<Path> files = Files.newDirectoryStream(Paths.get(filePath));
            for (Path file : files) {
//                upload( ftp, remoteDir+file.getFileName()+"_"+Files.size(file)+SysConstants.POSTFIX, SysConstants.FILE_PATH+file.getFileName(),needDelete);

            }
            //上传文件
            //退出
            logout(ftp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftp != null && ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                }
            }
        }

    }

    /**
     * 下载文件到本地
     *
     * @param remoteFileName 远程文件名称
     * @param locaFileName   本地文件名称
     */
    public void download(String remoteFileName, String locaFileName) {
        downloadDeal(remoteFileName, locaFileName, null);
    }

    /**
     * 下载文件到流
     *
     * @param remoteFileName 远程文件名称
     * @param ops            响应流
     */
    public void download(String remoteFileName, OutputStream ops) {
        downloadDeal(remoteFileName, null, ops);
    }

    /**
     * 下载文件到流
     *
     * @param remoteFileName 远程文件名称
     * @param ops            响应流
     */
    private void downloadDeal(String remoteFileName, String locaFileName, OutputStream ops) {
        if (ops == null && locaFileName == null) {
            logger.error("下载出错，下载路径和流不能全为空");
            return;
        }
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
            //连接ftp服务器
            connect(ftp);
            //设置属性
            setProperty(ftp);
            //下载文件
            if (locaFileName != null) {
                ops = new FileOutputStream(locaFileName);
            }
            ftp.retrieveFile(remoteFileName, ops);
            ops.close();
            //退出
            logout(ftp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                }
            }
        }
    }

    /**
     * 创建文件夹
     *
     * @param remotePathName 远程文件夹名称
     */
    public void mkdir(String remotePathName) {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
            //连接ftp服务器
            connect(ftp);
            //设置属性
            setProperty(ftp);
            //创建文件夹
            mkdir(ftp, remotePathName);
            //退出
            logout(ftp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                }
            }
        }
    }

    /**
     * @param ftp
     * @param remotePathName
     */
    private void mkdir(FTPClient ftp, String remotePathName) throws Exception {
        ftp.makeDirectory(remotePathName);
    }


    /**
     * @param ftp
     * @throws Exception
     */
    private void setProperty(FTPClient ftp) throws Exception {
        ftp.enterLocalPassiveMode();
        //二进制传输,默认为ASCII
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
    }

    /**
     * @param ftp
     */
    private void logout(FTPClient ftp) throws Exception {
        ftp.noop();
        ftp.logout();
    }

    /**
     * @param ftp
     * @param remoteFileName
     * @param locaFileName
     */
    private void upload(FTPClient ftp, String remoteFileName,
                        String locaFileName) throws Exception {
        //上传
        upload(ftp, remoteFileName, locaFileName, false);
    }

    private void upload(FTPClient ftp, String remoteFileName,
                        String locaFileName, boolean needDelete) throws Exception {
        //不支持文件夹上传
        if (Files.isDirectory(Paths.get(locaFileName))) {
            return;
        }
        //上传
        InputStream input;
        input = new FileInputStream(locaFileName);
        //若上送成功，返回true，不成功false
        if (ftp.storeFile(remoteFileName, input)) {
            input.close();//及时关闭流，否则无法对生成文件操作
            if (needDelete) {
                Files.delete(Paths.get(locaFileName));
            }
        } else {
            //失败原因处理
            if (!ftp.changeWorkingDirectory(remoteDir)) {
                ftp.makeDirectory(remoteDir);
                logger.warn("配置的上送目录{}不存在，已新建目录", remoteDir);
            }
            //待可能出现的原因处理...

            //所有可能的失败原因处理完后再次上送
            boolean dealResult = ftp.storeFile(remoteFileName, input);
            //及时关闭流，否则无法对生成文件操作
            input.close();
            if (!dealResult) {
                logger.info("{}上送失败！", remoteFileName);
                return;
            }
            if (needDelete) {
                Files.delete(Paths.get(locaFileName));
            }
        }
        logger.info("{}上送成功！", remoteFileName);
    }

    /**
     * @param ftp
     */
    private void connect(FTPClient ftp) throws Exception {
        //连接服务器
        ftp.connect(server, port);
        int reply = ftp.getReplyCode();
        //是否连接成功
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new ConnectException(server + " 服务器拒绝连接");
        }
        //登陆
        if (!ftp.login(username, password)) {
            throw new ConnectException("用户名或密码错误");
        }
    }
}