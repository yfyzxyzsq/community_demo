package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:discuss post
 * @Author:DDD_coder
 * @Date:2023/1/1 17:11
 */

@Data
public class DiscussPost {

    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;

}
