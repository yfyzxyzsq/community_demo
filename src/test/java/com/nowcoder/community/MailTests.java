package com.nowcoder.community;

import com.nowcoder.community.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

/**
 * @Description:to test send email
 * @Author:DDD_coder
 * @Date:2023/1/2 17:26
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    //Autowired注解报错 no beans of "TemplateEngine" type found
    @Resource
    private TemplateEngine templateEngine;

    @Test
    public void testSendText(){
        mailClient.sendMail("2290957226@qq.com", "TEST", "ddd_coder011");
        System.out.println("send email");
    }

    @Test
    public void testSendHtml(){
        Context context = new Context();
        context.setVariable("username","ddd_coder");
        String content = templateEngine.process("/mail/demo", context);
        mailClient.sendMail("2290957226@qq.com", "TEST HTML", content);

    }



}
