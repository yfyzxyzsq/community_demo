package com.nowcoder.community.constant;

/**
 * @Description:实体类型常量
 * @Author:DDD_coder
 * @Date:2023/1/6 15:41
 */

public final class EntityTypeConstant {

    /**
     *@Description:帖子
     */
    public static final int ENTITY_TYPE_POST = 1;


    /**
     *@Description:评论
     */
    public static final int ENTITY_TYPE_COMMENT = 2;//todo 具体值根据实际情况分析，数据库未给出

    /**
     *@Description:用户
     */
    public static final int ENTITY_TYPE_USER = 3;

    private EntityTypeConstant(){}
}
