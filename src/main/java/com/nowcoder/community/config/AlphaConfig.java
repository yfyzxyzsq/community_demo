package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @Description:测试添加第三方的bean
 * @Author:DDD_coder
 * @Date:2022/12/30 16:52
 */
@Configuration
public class AlphaConfig {

    @Bean //Bean的名字就是方法名
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
