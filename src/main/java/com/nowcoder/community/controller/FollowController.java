package com.nowcoder.community.controller;

import com.nowcoder.community.constant.EntityTypeConstant;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
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
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.Map;

/**
 * @Description:关注取关功能控制层组件
 * @Author:DDD_coder
 * @Date:2023/1/10 15:23
 */

@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId){
        User user = hostHolder.getUser();

        followService.follow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0, "已关注！");
    }

    @RequestMapping(value = "unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unFollow(int entityType, int entityId){
        User user = hostHolder.getUser();

        followService.unFollow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0, "已取消关注！");
    }

    @RequestMapping(value = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Model model, Page page){
        User user = userService.findUserById(userId);

        model.addAttribute("user", user);

        if(user == null){
            throw new RuntimeException("该用户不存在！");
        }

        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int)followService.findFollowerCount(EntityTypeConstant.ENTITY_TYPE_USER, userId));

        List<Map<String, Object>> followers = followService.getFollowers(userId, page.getOffset(), page.getLimit());
        if(followers != null){
            for(Map<String, Object> follower : followers){
                user = (User) follower.get("user");
                boolean followStatus = getFollowStatus(user.getId());
                follower.put("followStatus", followStatus);
            }
        }

        model.addAttribute("followerList", followers);

        return "/site/follower";
    }

    @RequestMapping(value = "/followees/{userId}", method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Model model, Page page){
        User user = userService.findUserById(userId);

        model.addAttribute("user", user);

        if(user == null){
            throw new RuntimeException("该用户不存在！");
        }

        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int)followService.findFolloweeCount(userId, EntityTypeConstant.ENTITY_TYPE_USER));

        List<Map<String, Object>> followees = followService.getFollowees(userId, page.getOffset(), page.getLimit());
        if(followees != null){
            for(Map<String, Object> followee : followees){
                user = (User) followee.get("user");
                boolean followStatus = getFollowStatus(user.getId());
                followee.put("followStatus", followStatus);
            }
        }

        model.addAttribute("followeeList", followees);

        return "/site/followee";
    }

    private boolean getFollowStatus(int userId){
        if(hostHolder.getUser() == null){
            return false;
        }
        return followService.findFollowStatus(hostHolder.getUser().getId(), EntityTypeConstant.ENTITY_TYPE_USER, userId);
    }

}
