package com.heima.travel.service;

import com.heima.travel.pojo.Address;
import com.heima.travel.pojo.ResultInfo;
import com.heima.travel.pojo.User;

import java.util.List;

public interface UserService {

    //登录
    ResultInfo login(User user);
    //短信登录查询
    ResultInfo findByTel4Login(String telephone);
    //短信注册查询
    ResultInfo findByTel4Register(String telephone);
    //用户名称查询
    ResultInfo findByname(String username);
    //短信服务
    ResultInfo smsService(String telephone,String valideCode);
    //添加用户
    void add(User user);
    // 更新用户信息
    ResultInfo update(String telephone, User user);

    //添加用户地址
    ResultInfo addAddress(Address address);
    //删除用户
    List<Address> deleteAddress(int aid,int uid);
    //显示地址列表
    List<Address> showAddress(int uid);

    ResultInfo updateAddress(Address address);

    List<Address> setDefault(int aid, User user);
}
