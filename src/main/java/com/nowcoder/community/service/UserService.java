package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:user service
 * @Author:DDD_coder
 * @Date:2023/1/1 17:47
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int userId){
        return userMapper.selectById(userId);
    }
}
