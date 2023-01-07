package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:message 业务层组件
 * @Author:DDD_coder
 * @Date:2023/1/7 20:48
 */

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    public List<Message> findConversations(int userId, int offset, int limit){
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit){
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(int userId, String conversationId){
        return messageMapper.selectLetterCount(userId, conversationId);
    }

    public int findUnreadCount(int userId, String conversationId){
        return messageMapper.selectUnreadCount(userId, conversationId);
    }

}
