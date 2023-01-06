package com.nowcoder.community.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:生成随机字符串；加密操作
 * @Author:DDD_coder
 * @Date:2023/1/3 9:30
 */
public class CommunityUtil {


    /**
     *com.nowcoder.community.utils.CommunityUtil.generateUUID();
     *@Description:生成随机字符串
     *@ParamType:[]
     *@ParamName:[]
     *@Return:java.lang.String
     *@author fyd
     *@create 2023/1/3 10:27
     *@version:
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     *com.nowcoder.community.utils.CommunityUtil.md5();
     *@ParamType:[java.lang.String]
     *@ParamName:[key]
     *@Return:java.lang.String
     *@author fyd
     *@create 2023/1/3 10:26
     *@version:
     */
    public static String md5(String key){

        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     *com.nowcoder.community.utils.CommunityUtil.getJSONString();
     *@Description:根据传入的数据返回json格式的字符串
     *@ParamType:[int, java.lang.String, java.util.Map<java.lang.String,java.lang.Object>]
     *@ParamName:[code, msg, map]
     *@Return:java.lang.String
     *@author fyd
     *@create 2023/1/6 8:31
     *@version:
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if(map != null && !map.isEmpty()){
            for(String key : map.keySet()){
                jsonObject.put(key, map.get(key));
            }
        }

        return jsonObject.toJSONString();
    }


    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }


    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

}
