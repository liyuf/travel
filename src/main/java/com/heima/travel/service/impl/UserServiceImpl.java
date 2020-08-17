package com.heima.travel.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.heima.travel.dao.UserDao;
import com.heima.travel.pojo.Address;
import com.heima.travel.pojo.ResultInfo;
import com.heima.travel.pojo.User;
import com.heima.travel.service.UserService;
import com.heima.travel.util.MybatisUtil;
import com.heima.travel.util.SMSutil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao=MybatisUtil.getProxyObject(UserDao.class);
    //密码登录查询
    @Override
    public ResultInfo login(User user) {
        ResultInfo resultInfo=null;
        //用户名查询
        String username=user.getUsername();
        User userExist = userDao.findByname(username);
        if(userExist==null){
            resultInfo=new ResultInfo(null,false,"用户名不存在");
            return resultInfo;
        }
        //用户名和密码查询
        String password=user.getPassword();
        //MD5加密
        String securityPassword = SecureUtil.md5(password);
        userExist=userDao.findByTotal(username,securityPassword);
        if (userExist == null) {
            resultInfo = new ResultInfo(null, false, "密码输入有误");
            return resultInfo;
        }
        //登录成功
        resultInfo=new ResultInfo(userExist,true,"登录成功");
        return resultInfo;
    }

//   登录时电话查询
    @Override
    public ResultInfo findByTel4Login(String telephone) {
        ResultInfo resultInfo=null;
        User user = userDao.findByTel(telephone);
        if (user == null) {
            User newUser = new User();
            newUser.setTelephone(telephone);
            resultInfo=new ResultInfo(newUser,true,null);
        }else{
            resultInfo=new ResultInfo(user,true,null);
        }
        return resultInfo;
    }

    //注册时电话查询
    @Override
    public ResultInfo findByTel4Register(String telephone) {
        ResultInfo resultInfo=null;
        User user = userDao.findByTel(telephone);
        if(user==null){
            resultInfo=new ResultInfo(null,true,"√");
        }else{
            resultInfo=new ResultInfo(null,false,"该手机已注册");
        }
        return resultInfo;
    }

//    用户名查询
    @Override
    public ResultInfo findByname(String username) {
        ResultInfo resultInfo=null;
        User user = userDao.findByname(username);
        if (user != null) {
            resultInfo=new ResultInfo(user,false,"该用户名已存在");
        }else{
            resultInfo=new ResultInfo(user,true,"√");
        }
        return resultInfo;
    }

    //短信服务
    @Override
    public ResultInfo smsService(String telephone,String valideCode) {
        ResultInfo resultInfo=null;
        //调用aliyun的api
        try {
            SendSmsResponse sendSmsResponse = SMSutil.sendSms(telephone, "黑马旅游网", "SMS_190277286", "{\"valideCode\":\"" + valideCode + "\"}");
            if ("OK".equals(sendSmsResponse.getCode())) {
                resultInfo=new ResultInfo(null,true,"短信发送成功");
            }else{
                resultInfo=new ResultInfo(null, false, "短信发送失败");
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
//        return resultInfo=new ResultInfo(null,true,"短信发送成功");
        return resultInfo;
    }

    //添加用户
    @Override
    public void add(User user) {
        //获取密码
        String password = user.getPassword();
        String securityPWD = SecureUtil.md5(password);
        user.setPassword(securityPWD);
        userDao.add(user);
    }

    //更新用户信息
    @Override
    public ResultInfo update(String telephone, User user) {
        ResultInfo resultInfo=null;
        //更新用户信息
        userDao.update(telephone,user);
        //查询返回用户
        User updateduser = userDao.findByTel(telephone);
        resultInfo=new ResultInfo(updateduser,true,"更新成功...");
        return resultInfo;
    }
    //添加用户地址
    @Override
    public ResultInfo addAddress(Address address) {
        ResultInfo resultInfo=null;
        //添加用户地址
        int result=userDao.addAddress(address);
        //以用户名查找地址
        if(result>0){
            resultInfo=new ResultInfo(null,true,"用户添加成功");
        }else{
            resultInfo=new ResultInfo(null,true,"用户添加失败");
        }
        return resultInfo;
    }

    //删除地址

    @Override
    public List<Address> deleteAddress(int aid,int uid) {
        List<Address> list;
        userDao.deleteAddress(aid);
        list = userDao.findAddressByUid(uid);
        return list;
    }

    //显示地址列表

    @Override
    public List<Address> showAddress(int uid) {
        List<Address> list=null;
        list=userDao.findAddressByUid(uid);
        return list;
    }

    //地址更新
    @Override
    public ResultInfo updateAddress(Address address) {
        ResultInfo resultInfo=null;
        int result=userDao.updateAddress(address);
        if(result>0){
            resultInfo=new ResultInfo(null,true,"更新成功");
        }else{
            resultInfo = new ResultInfo(null, false, "更新失败");
        }
        return resultInfo;
    }

    @Override
    public List<Address> setDefault(int aid, User user) {
        List<Address> list=null;
        //获取用户id
        int uid = user.getUid();
        //开启事务
        SqlSession sqlSession = MybatisUtil.openSession();
        //获取userDao对象
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        //取消默认地址
        try {
            int result=mapper.cancleDefault(uid);
            //设置默认地址
            int result2=mapper.setDefault(aid);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }
        //回显地址
        list=userDao.findAddressByUid(uid);
        return list;
    }
}
