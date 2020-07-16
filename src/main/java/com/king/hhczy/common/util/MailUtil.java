package com.king.hhczy.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author ningjinxiang
 */
@Component
public class MailUtil {
	@Autowired
	private JavaMailSender mailSender; //框架自带的

	@Value("${spring.mail.username}")  //发送人的邮箱  比如155156641XX@163.com
	private String from;
//	@Value("${spring.mail.nickname}")  //发送人昵称
	private String nickname = "内裤达人";

	@Async  //意思是异步调用这个方法
	public void sendMail(String title, String contents, String[] email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(nickname+'<'+from+'>'); // 发送人的邮箱
		message.setSubject(title); //标题
		message.setTo(email); //发给谁  对方邮箱
		message.setText(contents); //内容
		mailSender.send(message); //发送
	}
}
