package com.nowcoder.community.controller.interceptor;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CookieUtil;
import com.nowcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Description:处理每次登陆时显示用户信息
 * @Author:DDD_coder
 * @Date:2023/1/4 10:07
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {


    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    /**
     *com.nowcoder.community.controller.interceptor.LoginTicketInterceptor.preHandle();
     *@Description:通过request获取凭证，在每次controller执行前通过ticket将User暂存
     *@ParamType:[javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object]
     *@ParamName:[request, response, handler]
     *@Return:boolean
     *@author fyd
     *@create 2023/1/4 10:50
     *@version:
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");

        if(ticket != null){
//            LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                User user = userMapper.selectById(loginTicket.getUserId());
                hostHolder.setUser(user);

                //构建用户认证的结果，并存入SecurityContext，以便于Secutiy认证
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId())
                );
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        User user = hostHolder.getUser();
        if(modelAndView != null && user != null){
            modelAndView.addObject("loginUser", user);
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(hostHolder != null){
            hostHolder.clear();
        }
        SecurityContextHolder.clearContext();
    }
}
