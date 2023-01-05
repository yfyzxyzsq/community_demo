package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:登录凭证
 * @Author:DDD_coder
 * @Date:2023/1/3 21:27
 */
@Data
public class LoginTicket {

    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;
}
