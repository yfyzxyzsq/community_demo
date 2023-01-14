package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

/**
 * @Description:测试 elasticsearch
 * @Author:DDD_coder
 * @Date:2023/1/14 9:47
 */


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTests {


    @Autowired
    private DiscussPostMapper discussPostMapper;

//    @Autowired
//    private DiscussPostRepository discussPostRepository;
//
//    @Resource
//    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testInsert(){
//        discussPostRepository.save(discussPostMapper.selectDiscussPostById(241));
//        discussPostRepository.save(discussPostMapper.selectDiscussPostById(242));
//        discussPostRepository.save(discussPostMapper.selectDiscussPostById(243));
    }
}
