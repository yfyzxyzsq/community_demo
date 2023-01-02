package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @Description:the second implements of AlphaDao
 * @Author:DDD_coder
 * @Date:2022/12/30 16:35
 */

@Repository
@Primary  // 按照AlphaDao.class获取bean时，优先获取这个，如果没有注解会报错（因为有两个类都实现了这个接口）
public class AlphaDaoMybatisImpl implements AlphaDao{
    @Override
    public String select() {
        return "Mybatis";
    }
}
