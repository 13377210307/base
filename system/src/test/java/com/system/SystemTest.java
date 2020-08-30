package com.system;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SystemTest {

    @Test
    public void test1() {
        /*String code = RandomUtil.randomString(4);
        System.out.println(code);*/
        String today= DateUtil.today();
        System.out.println(today);
    }
}
