package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;
import sun.security.krb5.internal.Ticket;

/**
 * @Description:登录凭证DAO层
 * @Author:DDD_coder
 * @Date:2023/1/3 21:31
 */
@Mapper
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket (user_id, ticket, status, expired) ",
            "values(#{userId}, #{ticket}, #{status}, #{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id, user_id, ticket, status, expired from login_ticket ",
            "where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    //下面是使用注解加动态Sql的示例
    @Update({
//            "<script>",
            "update login_ticket set status = #{status} where ticket = #{ticket}"
//            "<if test=\"ticket!=null\">",
//            "and ...",
//            "</if>",
//            "</script>"
    })
    int updateStatus(String ticket, int status);

}
