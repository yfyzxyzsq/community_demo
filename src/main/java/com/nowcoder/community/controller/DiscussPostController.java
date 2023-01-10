package com.nowcoder.community.controller;

import com.nowcoder.community.constant.EntityTypeConstant;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @Description:处理discuss post相关请求
 * @Author:DDD_coder
 * @Date:2023/1/6 8:47
 */

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJSONString(403, "您还没有登录！");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());

        discussPostService.addDiscussPost(discussPost);
        //报错的情况将来统一处理

        return CommunityUtil.getJSONString(0, "发布成功！");
    }

    @RequestMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        //查询帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);

        //帖子的作者
        User author = userService.findUserById(post.getUserId());
        model.addAttribute("author", author);

        //帖子的赞的数量和用户点赞状态
        long likeCount = likeService.findEntityLikeCount(EntityTypeConstant.ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);

        int likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), EntityTypeConstant.ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        //评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        //帖子的评论
        List<Comment> comments = commentService.findCommentsByEntity(
                EntityTypeConstant.ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(comments != null){
            for(Comment comment : comments){
                Map<String, Object> commentVo = new HashMap<>();
                //评论
                commentVo.put("comment", comment);
                //评论的作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                //评论的点赞状态
                likeCount = likeService.findEntityLikeCount(EntityTypeConstant.ENTITY_TYPE_COMMENT, comment.getId());
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), EntityTypeConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                commentVo.put("likeStatus", likeStatus);

                //对评论的回复
                List<Comment> replys = commentService.findCommentsByEntity(
                        EntityTypeConstant.ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if(replys != null){
                    for(Comment reply : replys){
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        //评论的回复的点赞状态
                        likeCount = likeService.findEntityLikeCount(EntityTypeConstant.ENTITY_TYPE_COMMENT, reply.getId());
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), EntityTypeConstant.ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);
                        replyVo.put("likeStatus", likeStatus);


                        User target = (reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId()));
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                int replyCount = commentService.findCommentCount(EntityTypeConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);

        return "/site/discuss-detail";
    }


}
