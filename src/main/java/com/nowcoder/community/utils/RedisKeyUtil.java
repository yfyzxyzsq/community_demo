package com.nowcoder.community.utils;

import com.nowcoder.community.constant.RedisAboutConstant;

/**
 * @Description:生成redis key的工具
 * @Author:DDD_coder
 * @Date:2023/1/10 10:26
 */
public class RedisKeyUtil {

    //like:entity:entityType:entityId的形式的key
    public static String getEntityLikeKey(int entityType, int entityId){
        return RedisAboutConstant.PREFIX_ENTITY_LIKE + RedisAboutConstant.SPLIT + entityType + RedisAboutConstant.SPLIT + entityId;
    }

    //like:user:userId
    public static String getUserLikeKey(int userId){
        return RedisAboutConstant.PREFIX_USER_LIKE + RedisAboutConstant.SPLIT + userId;
    }
}
