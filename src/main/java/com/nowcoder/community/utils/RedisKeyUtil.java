package com.nowcoder.community.utils;

import com.nowcoder.community.constant.RedisAboutConstant;

/**
 * @Description:生成redis key的工具
 * @Author:DDD_coder
 * @Date:2023/1/10 10:26
 */
public class RedisKeyUtil {

    //like:entity:entityType:entityIdd -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId){
        return RedisAboutConstant.PREFIX_ENTITY_LIKE + RedisAboutConstant.SPLIT + entityType + RedisAboutConstant.SPLIT + entityId;
    }

    //like:user:userId -> int
    public static String getUserLikeKey(int userId){
        return RedisAboutConstant.PREFIX_USER_LIKE + RedisAboutConstant.SPLIT + userId;
    }

    //某个实体的关注者
    //follower:entityType:entityId -> zset(userId, now)
    public static String getFollowerKey(int entityType, int entityId){
        return RedisAboutConstant.PREFIX_FOLLOWER + RedisAboutConstant.SPLIT + entityType + RedisAboutConstant.SPLIT + entityId;
    }

    //某个用户关注的对象
    //followee:userId:entityType -> zset(entityId, now)
    public static String getFolloweeKey(int userId, int entityType){
        return RedisAboutConstant.PREFIX_FOLLOWEE + RedisAboutConstant.SPLIT + userId + RedisAboutConstant.SPLIT + entityType;
    }

    //验证码的key
    public static String getKaptchaKey(String owner){
        return RedisAboutConstant.PREFIX_KAPTCHA + RedisAboutConstant.SPLIT + owner;
    }

    //登录凭证
    public static String getTicketKey(String ticket){
        return RedisAboutConstant.PREFIX_TICKET + RedisAboutConstant.SPLIT + ticket;
    }

    //用户
    public static String getUserkey(int userId){
        return RedisAboutConstant.PREFIX_USER + RedisAboutConstant.SPLIT + userId;
    }
}
