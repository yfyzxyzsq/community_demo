package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:elasticreaserch
 * @Author:DDD_coder
 * @Date:2023/1/14 9:40
 */

@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
}
