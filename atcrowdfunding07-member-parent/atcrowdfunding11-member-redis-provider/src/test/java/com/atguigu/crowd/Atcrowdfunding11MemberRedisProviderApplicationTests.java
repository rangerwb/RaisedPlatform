package com.atguigu.crowd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Atcrowdfunding11MemberRedisProviderApplication.class})
public class Atcrowdfunding11MemberRedisProviderApplicationTests {

    private Logger logger = LoggerFactory.getLogger(Atcrowdfunding11MemberRedisProviderApplication.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void RedisTest() {

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String key = "hello";
        String value = "world";
        operations.set(key, value);

    }

}
