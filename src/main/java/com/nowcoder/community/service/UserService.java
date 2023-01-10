package com.nowcoder.community.service;

import com.nowcoder.community.constant.ActivationConstant;
import com.nowcoder.community.constant.RedisAboutConstant;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.HostHolder;
import com.nowcoder.community.utils.MailClient;
import com.nowcoder.community.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description:user service
 * @Author:DDD_coder
 * @Date:2023/1/1 17:47
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;


    public User findUserById(int userId) {
//        return userMapper.selectById(userId);
        User user = getCache(userId);
        if(user == null){
            user = initCache(userId);
        }
        return user;
    }

    /**
     *com.nowcoder.community.service.UserService.register();
     *@Description:注册方法
     *@ParamType:[com.nowcoder.community.entity.User]
     *@ParamName:[user]
     *@Return:java.util.Map<java.lang.String,java.lang.Object>
     *@author fyd
     *@create 2023/1/3 11:24
     *@version:
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        User exist = userMapper.selectByName(user.getUsername());
        if(exist != null){
            map.put("usernameMsg", "该账号已存在");
            return map;
        }
        exist = userMapper.selectByEmail(user.getEmail());
        if(exist != null){
            map.put("emailMsg", "该邮箱已被注册");
            return map;
        }

        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());

        userMapper.insertUser(user);

        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);

        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活邮箱", content);

        return map;
    }

    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return ActivationConstant.ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId, 1);
            clearCache(userId);
            return ActivationConstant.ACTIVATION_SUCCESS;
        }else{
            return ActivationConstant.ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        if(StringUtils.isBlank(username)){
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg", "该用户不存在！");
            return map;
        }

        if(user.getStatus() == 0){
            map.put("usernameMsg", "该用户未激活！");
            return map;
        }

        password = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg", "密码错误！");
            return map;
        }

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

//        loginTicketMapper.insertLoginTicket(loginTicket);

        String ticketKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(ticketKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket){
//        loginTicketMapper.updateStatus(ticket,1);
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(ticketKey, loginTicket);
    }

    public LoginTicket findLoginTicket(String ticket){
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        return loginTicket;
    }

    public int updateHeaderUrl(int userId, String url){
        int rows = userMapper.updateHeader(userId, url);
        clearCache(userId);
        return rows;
    }

    public Map<String, Object> updatePassword(int userId, String originPassword, String oldPassword, String newPassword){
        Map<String, Object> map = new HashMap<>();

        if(!(originPassword).equals(oldPassword)){
            map.put("oldPasswordError", "原密码错误");
            return map;
        }
        if(StringUtils.isBlank(newPassword)){
            map.put("newPasswordError", "密码不能为空");
            return map;
        }

        userMapper.updatePassword(userId, newPassword);
        return map;
    }

    public User findUserByName(String name){
        return userMapper.selectByName(name);
    }

    //1.优先从缓存中取值
    private User getCache(int userId){
        String userkey = RedisKeyUtil.getUserkey(userId);

        return (User) redisTemplate.opsForValue().get(userkey);
    }

    //2.取不到时初始化缓存数据
    private User initCache(int userId){
        User user = userMapper.selectById(userId);
        String userkey = RedisKeyUtil.getUserkey(userId);
        redisTemplate.opsForValue().set(userkey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    //3.数据变更时清除缓存数据
    private void clearCache(int userId){
        String userkey = RedisKeyUtil.getUserkey(userId);
        redisTemplate.delete(userkey);
    }

}
