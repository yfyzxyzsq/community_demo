package com.nowcoder.community.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/**
 * @Description:the client to send email
 * @Author:DDD_coder
 * @Date:2023/1/2 16:34
 */

@Component
public class MailClient{

    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Resource
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String content){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(helper.getMimeMessage());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送邮件出错:"+e.getMessage());
        }
    }
}
