package com.nowcoder.community;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.utils.CommunityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @Description:test user-mapper
 * @Author:DDD_coder
 * @Date:2023/1/1 16:19
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelect(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("123");
        user.setEmail("nowcoder11111@sina.com");
        user.setType(0);
        user.setStatus(1);
        user.setHeaderUrl("http://images.nowcoder.com/head/100t.png");
        user.setCreateTime(new Date());
        int insert = userMapper.insertUser(user);
        System.out.println(insert);
    }

    @Test
    public void testUpdate(){
        int updatePassword = userMapper.updatePassword(150, "wantodiwithyd");
        System.out.println(updatePassword);

        int updateHeader = userMapper.updateHeader(150, "http://images.nowcoder.com/head/149t.png");
        System.out.println(updateHeader);

        int updateStatus = userMapper.updateStatus(150, 0);
        System.out.println(updateStatus);


    }


    @Test
    public void testDiscussPostMapper(){

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);

        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for (DiscussPost discussPost : discussPosts){
            System.out.println(discussPost);
        }
    }

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testLoginTicketMapper(){
//        LoginTicket loginTicket = new LoginTicket();
//        loginTicket.setUserId(101);
//        loginTicket.setTicket("test");
//        loginTicket.setStatus(0);
//        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 30));
//        loginTicketMapper.insertLoginTicket(loginTicket);

        LoginTicket loginTicket = loginTicketMapper.selectByTicket("test");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("test", 1);

    }

    @Test
    public void testInsertDiscussPost(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle("test");
        discussPost.setUserId(11);
        discussPost.setContent("aaaaaaaaaaa");
        discussPost.setCreateTime(new Date());

        int result = discussPostMapper.insertDiscussPost(discussPost);

        System.out.println(result);
    }

    @Test
    public void testInsertComment(){
        Comment comment = new Comment();
        comment.setUserId(202);
        comment.setContent("aaaaaaaaaa");
        int insertComment = commentMapper.insertComment(comment);
        System.out.println(insertComment);
    }

    @Test
    public void testUpdateCommentCount(){
        int res = discussPostMapper.updateCommentCount(284, 52);
        System.out.println(res);
    }

}
