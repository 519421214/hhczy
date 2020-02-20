package com.king.hhczy.common.util;

import org.springframework.stereotype.Component;
import sun.awt.windows.WToolkit;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * 参考https://www.cnblogs.com/chy18883701161/p/10852203.html
 * https://blog.csdn.net/weixin_34195142/article/details/92612816?utm_source=distribute.pc_relevant.none-task
 *
 * @author ningjinxiang
 */
@Component
public abstract class KeyboardUtil {

    public static void copyAndSend(String words) {
        try {
            //打开微信
            openApp("C:\\\\Program Files (x86)\\\\Tencent\\\\WeChat\\\\WeChat.exe");
            WToolkit wToolkit = new WToolkit();
//            Clipboard clip = wToolkit.getSystemClipboard();
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = null;
//            Toolkit tolkit = Toolkit.getDefaultToolkit();
            Robot robot = new Robot();
            robot.delay(5000);//延迟5秒，主要是为了预留出打开窗口的时间，括号内的单位为毫秒
            tText = new StringSelection(words); //自己定义就需要把这行注释，下行取消注释
//            tText = new StringSelection("爱你每一天");//如果爱得深，把这行取消注释，把内容更换掉你自己想说的
            clip.setContents(tText, null);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);//释放所有按键
            robot.delay(1000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_CONTROL);//释放所有按键
            robot.delay(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //打开程序
    public static void openApp(String path) {
        Runtime runtime = Runtime.getRuntime();
        Process p = null;
//        String path = "C:\\Program Files (x86)\\Tencent\\WeChat\\WeChat.exe";
        try {
            p = runtime.exec(path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
//        openApp();
//        String[] lists = {"我只爱你四天,春天夏天秋天冬天", "我只爱你三天,昨天,今天,明天.", "我只爱你两天,白天,黑天", "我只爱你一天,每一天", "爱你么么哒"};
//        copyAndSend("123");
    }
}
