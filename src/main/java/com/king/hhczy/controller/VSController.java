package com.king.hhczy.controller;

import com.king.hhczy.common.util.WordsReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 影音类
 */
@RestController
public class VSController {
    @Autowired
    private WordsReading wordsReading;
    @Value("${realm.name}")
    private String realmName;
    @Value("${server.port}")
    private String port;
    /**
     * 本地方式上传文件/图片
     *
     * @param words
     * @return
     */
    @PostMapping("/word-to-speak")
    public String wordToSpeak(@RequestBody Map<String,String> words) {
        String inWords = words.get("words");
        String com = realmName + ":" + port;
        return wordsReading.build(inWords,com);
    }

}