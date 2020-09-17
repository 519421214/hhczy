package com.king.hhczy.common.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 语音播报的方式有很多中。通过jacob实现语音朗读，则是其中最简单的一种实现方式。
 * 实现步骤：
 * 1、下载Jacob文件。
 * 官网下载：https://sourceforge.net/projects/jacob-project/
 * 2、把下载解压之后里面的“jacob-1.19-x64.dll”文件复制到jdk安装的bin目录中（切记：.dll文件根据自己电脑位数选择不同的文件）。
 * 3、通过build Path把文件里的“jacob.jar”jar包添加到项目中。 测试通过maven会报错，未解决
 * 4、语音播报实现代码
 *
 * @author ningjinxiang
 */
public class WordsReading {
    public static void speak(String words) {
        System.out.println(System.getProperty("java.library.path"));
        ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
        Dispatch sapo = sap.getObject();
        try {
            // 音量 0-100
            sap.setProperty("Volume", new Variant(100));
            // 语音朗读速度 -10 到 +10
            sap.setProperty("Rate", new Variant(0));
            // 执行朗读
            Dispatch.call(sapo, "Speak", new Variant(words));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sapo.safeRelease();
            sap.safeRelease();
        }
    }
}
