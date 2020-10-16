package com.king.hhczy.common.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

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
@Component
public class WordsReading {
    @Value("${ftp.absolute.addr}")
    private String path;

    //朗读,不能并发朗读，通过先生成音频再读实现并发
    public void speak(String words) {
//        System.out.println(System.getProperty("java.library.path"));
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

    /**
     * host不为空的话，就返回服务地址
     * 生成文件,再读取
     * @param words
     * @param host
     * @return
     */
    public String build(String words, String host) {
        //以年月日为路径
        LocalDate nowDate = LocalDate.now();
        Path directors = Paths.get(path, nowDate.getYear() + "", nowDate.getMonthValue() + "", nowDate.getDayOfMonth() + "");
        try {
            Files.createDirectories(directors);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String localPath = directors.toString() + "\\" + RandomUtil.uuid() + ".wav";
        ActiveXComponent ax = new ActiveXComponent("Sapi.SpVoice");
        Dispatch spVoice = ax.getObject();
        try {
            //下面是构建文件流把生成语音文件

            ax = new ActiveXComponent("Sapi.SpFileStream");
            Dispatch spFileStream = ax.getObject();

            ax = new ActiveXComponent("Sapi.SpAudioFormat");
            Dispatch spAudioFormat = ax.getObject();

            //设置音频流格式
            Dispatch.put(spAudioFormat, "Type", new Variant(22));
            //设置文件输出流格式
            Dispatch.putRef(spFileStream, "Format", spAudioFormat);
            //调用输出 文件流打开方法，创建一个.wav文件
            Dispatch.call(spFileStream, "Open", new Variant(localPath), new Variant(3), new Variant(true));
            //设置声音对象的音频输出流为输出文件对象
            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
            //设置音量 0到100
            Dispatch.put(spVoice, "Volume", new Variant(100));
            //设置朗读速度
            Dispatch.put(spVoice, "Rate", new Variant(0));
            //开始朗读
            Dispatch.call(spVoice, "Speak", new Variant(words));

            //关闭输出文件
            Dispatch.call(spFileStream, "Close");
            Dispatch.putRef(spVoice, "AudioOutputStream", null);
            if (StringUtils.hasText(host)) {
                localPath = localPath.replace(path,host+"/").replace("\\","/");
            }
            return localPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            spVoice.safeRelease();
            ax.safeRelease();
        }
        return null;
    }

    /**
     * 只能播放到本地，不能远程访问收听
     *
     * @param path
     * @throws LineUnavailableException
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    public void playLocalAudio(String path) throws LineUnavailableException,
            UnsupportedAudioFileException, IOException {
        // 获取音频输入流
        AudioInputStream audioInputStream = AudioSystem
                .getAudioInputStream(new File(path));
        // 获取音频编码对象
        AudioFormat audioFormat = audioInputStream.getFormat();

        // 设置数据输入
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
                audioFormat, AudioSystem.NOT_SPECIFIED);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem
                .getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        /*
         * 从输入流中读取数据发送到混音器
         */
        int count;
        byte tempBuffer[] = new byte[1024];
        while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
            if (count > 0) {
                sourceDataLine.write(tempBuffer, 0, count);
            }
        }

        // 清空数据缓冲,并关闭输入
        sourceDataLine.drain();
        sourceDataLine.close();
    }

    /**
     * 也是调用本地播放器播放
     *
     * @param path
     */
    public void playAudio(String path) {
        StdAudio.play(path);
    }

}
