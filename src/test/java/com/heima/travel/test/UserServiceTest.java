package com.heima.travel.test;

import com.heima.travel.pojo.ResultInfo;
import com.heima.travel.pojo.User;
import com.heima.travel.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration("classpath:applicationContext.xml")
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired private UserService userService;
    @Autowired private User user;

    //登录测试
    @Test
    public void test01() throws Exception{
        user.setUsername("张无忌");
        ResultInfo resultInfo = userService.login(user);
        System.out.println(resultInfo);
    }
    //短信测试
    @Test
    public void test02() throws Exception{
        ResultInfo resultInfo = userService.smsService("18801738929", "123456");
        System.out.println(resultInfo);
    }
    //更新测试
    @Test
    public void test03() throws Exception{
        user.setNickname("苏苏");
        ResultInfo result = userService.update("18801738929", user);
        System.out.println(result);
    }
}
