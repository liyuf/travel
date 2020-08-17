package com.heima.travel.test;

import com.heima.travel.dao.OrderDao;
import com.heima.travel.dao.RouteDao;
import com.heima.travel.dao.UserDao;
import com.heima.travel.pojo.Order;
import com.heima.travel.pojo.Route;
import com.heima.travel.pojo.User;
import com.heima.travel.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ContextConfiguration("classpath:applicationContext.xml")
@RunWith(SpringRunner.class)
public class proxyObjTest {
    //动态代理测试
    @Test
    public void test01() throws Exception{
        //获取userDao的代理对象
        UserDao userDao = MybatisUtil.getProxyObject(UserDao.class);
        User user = userDao.findByname("张无忌");
        System.out.println(user);

    }
    //更新操作
    @Test
    public void test02() throws Exception{
        UserDao userDao = MybatisUtil.getProxyObject(UserDao.class);
        User user = new User();
        user.setNickname("youyou");
        userDao.update("18801738929",user);
        userDao.findByTel("18801738929");
    }
    //原生类
    @Test
    public void test03() throws Exception{
        SqlSession sqlSession = MybatisUtil.openSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        User user = mapper.findByname("张无忌");
        System.out.println(user);
    }

    //模糊查询
    @Test
    public void test04() throws Exception{
        RouteDao proxyObject = MybatisUtil.getProxyObject(RouteDao.class);
        int count = proxyObject.findRouteByName("温泉");
        System.out.println(count);
    }
    //分页查询
    @Test
    public void test05() throws Exception{
        RouteDao proxyObject = MybatisUtil.getProxyObject(RouteDao.class);
        List<Route> list = proxyObject.showRouteFindByName("温泉", 0, 3);
        System.out.println(list);
    }
    //订单查询
    @Test
    public void test06() throws Exception{
        OrderDao proxyObject = MybatisUtil.getProxyObject(OrderDao.class);
        List<Order> orderByUid = proxyObject.findOrderByUid(26, 1, 4,1);
        System.out.println(orderByUid);
    }

}
