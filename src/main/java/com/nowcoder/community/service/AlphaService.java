package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Description:test
 * @Author:DDD_coder
 * @Date:2022/12/30 16:46
 */

@Service
//@Scope("prototype")  多个实例, 默认单例singleton
public class AlphaService {

    public AlphaService(){
        System.out.println("实例化AlphaService");
    }

    @PostConstruct //实例化之后
    public void init(){
        System.out.println("初始化AlphaService");
    }

    @PreDestroy  //销毁之前
    public void destory(){
        System.out.println("销毁AlphaService");
    }
}
