package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @Description:test bean
 * @Author:DDD_coder
 * @Date:2022/12/30 16:33
 */

//@Repository
@Repository("alphaHibernate")  //定义了bean的名字
public class AlphaDaoHibernateImpl implements AlphaDao{


    @Override
    public String select() {
        return "Hibernate";
    }
}
