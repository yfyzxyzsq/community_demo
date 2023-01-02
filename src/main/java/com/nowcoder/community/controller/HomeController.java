package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:about home page
 * @Author:DDD_coder
 * @Date:2023/1/1 19:09
 */

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;


    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        //方法调用前，SpringMVC会自动初始化model和page，并将page注入到model
        //所以，thymeleaf中可以直接访问Page
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(discussPosts != null) {
            for (DiscussPost discussPost : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                int userId = discussPost.getUserId();
                User user = userService.findUserById(userId);
                map.put("user", user);
                discussPosts.add(map);
            }
            model.addAttribute("discussPosts", discussPosts);
        }
        return "/index";
    }

}
