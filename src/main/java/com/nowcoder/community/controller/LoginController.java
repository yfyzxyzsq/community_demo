package com.nowcoder.community.controller;

import com.nowcoder.community.constant.ActivationConstant;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description:处理登录注册相关请求
 * @Author:DDD_coder
 * @Date:2023/1/3 8:28
 */

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
       return "/site/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已经向你的邮件发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }

    }


    @RequestMapping(value = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        int activationResult = userService.activation(userId, code);
        if(activationResult == ActivationConstant.ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账户现在已经可以正常使用！");
            model.addAttribute("target", "/login");
        }else if(activationResult == ActivationConstant.ACTIVATION_FAILURE){
            model.addAttribute("msg", "激活失败，请检查您的激活码是否正确！");
            model.addAttribute("target", "/index");
        }else if(activationResult == ActivationConstant.ACTIVATION_REPEAT){
            model.addAttribute("msg", "无效操作，您的账号已经完成激活，请勿重复操作！");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }
}
