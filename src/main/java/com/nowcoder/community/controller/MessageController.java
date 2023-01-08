package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
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

import java.util.*;

/**
 * @Description:message 控制层组件
 * @Author:DDD_coder
 * @Date:2023/1/7 22:04
 */

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "letter/list", method = RequestMethod.GET)
    public String getConversationList(Model model, Page page){
        User user = hostHolder.getUser();

        page.setLimit(5);
        page.setRows(messageService.findConversationCount(user.getId()));
        page.setPath("/letter/list");

        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> conversations = new ArrayList<>();

        for(Message message : conversationList){
            Map<String, Object> map = new HashMap<>();
            map.put("conversation", message );      //存的至少代表一个会话的最新的数据
            //会话对象的id
            int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
            map.put("target", userService.findUserById(targetId));

            int unreadCount = messageService.findUnreadCount(user.getId(), message.getConversationId());
            map.put("unreadCount", unreadCount);

            int letterCount = messageService.findLetterCount(user.getId(), message.getConversationId());
            map.put("letterCount", letterCount);
            conversations.add(map);
        }
        model.addAttribute("conversations", conversations);

        int letterUnreadCount = messageService.findUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);

        return "/site/letter";
    }

    private List<Integer> getUnreadIds(List<Message> messages){
        List<Integer> ids = new ArrayList<>();
        if(messages != null){
            for(Message message : messages){
                if(hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    @RequestMapping("/letter/detail/{conversationId}")
    public String getConversationDetail(@PathVariable("conversationId") String conversationId, Model model, Page page){
        User user = hostHolder.getUser();

        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(user.getId(), conversationId));
        page.setLimit(5);

        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());


        Message m = letterList.get(0);
        int senderId = user.getId() == m.getFromId() ? m.getToId() : m.getFromId();
        User target = userService.findUserById(senderId);
        model.addAttribute("sender", target);

        List<Map<String, Object>> letters = new ArrayList<>();

        for(Message message : letterList){
            Map<String, Object> map = new HashMap<>();
            map.put("letter", message);

            User sender = (message.getFromId() == user.getId() ? user : userService.findUserById(message.getFromId()));
            map.put("user", sender);
            letters.add(map);
        }

        model.addAttribute("letters", letters);
        List<Integer> unreadIds = getUnreadIds(letterList);
        if(!unreadIds.isEmpty()){
            messageService.updateStatus(unreadIds, 1);
        }


        return "/site/letter-detail";
    }

    @RequestMapping(value = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content){
        User user = hostHolder.getUser();
        User target = userService.findUserByName(toName);
        Message message = new Message();
        message.setFromId(user.getId());
        message.setToId(target.getId());
        if(user.getId() < target.getId()){
            message.setConversationId(user.getId() + "_" + target.getId());
        }else {
            message.setConversationId(target.getId() + "_" + user.getId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());

        messageService.addMessage(message);
        return CommunityUtil.getJSONString(0);
    }
}
