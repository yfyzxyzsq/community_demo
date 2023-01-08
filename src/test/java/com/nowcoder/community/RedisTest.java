package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;

/**
 * @Description:测试redis template
 * @Author:DDD_coder
 * @Date:2023/1/8 20:21
 */


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings(){
        String key = "test:count";
        redisTemplate.opsForValue().set(key, 1);
        System.out.println(redisTemplate.opsForValue().get(key));
        System.out.println(redisTemplate.opsForValue().increment(key));
        System.out.println(redisTemplate.opsForValue().decrement(key));
    }

    @Test
    public void testHashes(){
        String key = "test:user";

        redisTemplate.opsForHash().put(key, "id", 1);
        redisTemplate.opsForHash().put(key, "username", "ddd_coder");

        System.out.println(redisTemplate.opsForHash().get(key, "id"));
        System.out.println(redisTemplate.opsForHash().get(key, "username"));
    }

    //将频繁操作的key绑定到对象上，这样可以简化操作
    @Test
    public void testBoundOperations(){
        String key = "test:count";
        BoundValueOperations ops = redisTemplate.boundValueOps(key);
        ops.increment();
        ops.increment();//此时通过对象调用api即可

    }

    //编程式事务
    @Test
    public void testTransactional(){

        Object result = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String key = "test:tx";

                operations.multi();//启用事务

                operations.opsForSet().add(key, "aaa");
                operations.opsForSet().add(key, "bbb");
                //事务中的所有操作是提交到队列中，然后一次性执行，所以事务中不要做查询
                System.out.println(operations.opsForSet().members(key));

                return operations.exec();
            }
        });
        System.out.println(result);
    }
}
