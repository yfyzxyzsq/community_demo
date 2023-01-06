package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:对应数据库中评论的实体类
 * @Author:DDD_coder
 * @Date:2023/1/6 12:12
 */

@Data
public class Comment {

    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;

}
