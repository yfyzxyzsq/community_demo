package com.nowcoder.community.othertests;

import com.nowcoder.community.CommunityApplication;
import com.nowcoder.community.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @Description:test sensitive words filter
 * @Author:DDD_coder
 * @Date:2023/1/5 18:18
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveFilterTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testFilter(){
        String result = sensitiveFilter.filter("aaaaaa赌☆博玩哈哈哈抽＜（＾－＾）＞烟啊啊啊");
        System.out.println(result);
    }
}
