package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:会话信息对应实体类
 * @Author:DDD_coder
 * @Date:2023/1/7 19:48
 */

@Mapper
public interface MessageMapper {

    //查询当前用户的会话列表，每个会话只返回最新的一条私信
    List<Message> selectConversations(int userId, int offset, int limit);

    //查询当前用户的会话数量
    int selectConversationCount(int userId);

    //查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    //查询某个会话所包含的私信数量
    int selectLetterCount(int userId, String conversationId);

    //查询未读私信数量，采用动态Sql，未传入会话ID则查用户所有的未读数量，传入则查该会话的未读数量
    int selectUnreadCount(int userId, String conversationId);

    //插入message
    int insertMessage(Message message);

    //更新状态
    int updateStatus(List<Integer> ids, int status);

}

