package com.nowcoder.community.utils;

import org.springframework.http.HttpRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:cookie相关数据
 * @Author:DDD_coder
 * @Date:2023/1/4 10:11
 */
public class CookieUtil {

    /**
     *com.nowcoder.community.utils.CookieUtil.getValue();
     *@Description:通过request对象获取对应name的cookie的值
     *@ParamType:[javax.servlet.http.HttpServletRequest, java.lang.String]
     *@ParamName:[request, name]
     *@Return:java.lang.String
     *@author fyd
     *@create 2023/1/4 10:15
     *@version:
     */
    public static String getValue(HttpServletRequest request, String name){
        if(request == null || name == null){
            throw new IllegalArgumentException("参数为空！");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
