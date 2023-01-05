package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Description:user 相关
 * @Author:DDD_coder
 * @Date:2023/1/4 11:52
 */
@RequestMapping("/user")
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HostHolder hostHolder;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {

        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确");
            return "/site/setting";
        }
        fileName = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + fileName);

        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }

        User user = hostHolder.getUser();
        user.setHeaderUrl(domain + contextPath + "/user/header/" + fileName);

        userService.updateHeaderUrl(user.getId(), user.getHeaderUrl());

        return "redirect:/index";
    }

    @RequestMapping(value = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        fileName = uploadPath + "/" + fileName;
        String suffix  = fileName.substring(fileName.lastIndexOf("."));

        response.setContentType("image/" + suffix);

        try(
                FileInputStream fileInputStream = new FileInputStream(fileName);
                ){
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fileInputStream.read(buffer)) != -1){
                outputStream.write(buffer);
            }
        } catch (IOException e) {
            logger.error("获取头像失败" + e.getMessage());
        }

    }



}
