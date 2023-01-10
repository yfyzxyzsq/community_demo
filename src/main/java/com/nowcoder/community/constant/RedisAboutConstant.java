package com.nowcoder.community.constant;

/**
 * @Description:使用redis时，拼接key会用到的相关常量
 * @Author:DDD_coder
 * @Date:2023/1/10 10:24
 */
public final class RedisAboutConstant {

    public static final String SPLIT = ":";

    public static final String PREFIX_ENTITY_LIKE = "like:entity";

    public static final String PREFIX_USER_LIKE = "like:user";

    public static final String PREFIX_FOLLOWEE = "followee";

    public static final String PREFIX_FOLLOWER = "follower";

    private RedisAboutConstant(){}
}
