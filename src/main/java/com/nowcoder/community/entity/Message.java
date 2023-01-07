package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:会话中消息对应的实体类
 * @Author:DDD_coder
 * @Date:2023/1/7 19:42
 */

@Data
public class Message {

    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;


}
