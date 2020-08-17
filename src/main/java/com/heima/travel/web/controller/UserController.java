package com.heima.travel.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heima.travel.pojo.Address;
import com.heima.travel.pojo.ResultInfo;
import com.heima.travel.pojo.User;
import com.heima.travel.service.UserService;
import com.heima.travel.util.JedisUtil;
import com.heima.travel.util.LoginStatuUtil;
import com.heima.travel.util.MybatisUtil;
import com.heima.travel.util.ObjectToStringUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/userController")
public class UserController {
    @Autowired
    private UserService userService;


/*------------------------------------注册模块---------------------------------------*/
    //注册模块
    //用户名校验
    @RequestMapping("/usernameCheck")
    public void usernameCheck(String username,HttpServletResponse response){
        //调用service层
        ResultInfo resultInfo = userService.findByname(username);
        try {
            //转成json格式
            String json = ObjectToStringUtil.objToJson(resultInfo);
            response.setContentType("application/text;charset=utf-8");
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //手机号码校验
    @RequestMapping("/telephoneCheck")
    public void telephoneCheck(String telephone, HttpServletResponse response) {
        //设置返回类型
        response.setContentType("application/json;charset=utf-8");
        //获取service层
        ResultInfo resultInfo = userService.findByTel4Register(telephone);
        //转化成json格式
        try {
            String json = ObjectToStringUtil.objToJson(resultInfo);
            //将json响应给前端
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //表单提交
    @RequestMapping("/register")
    public String register(User user,String smsCode,HttpServletRequest request,HttpServletResponse response){
        //验证码校验
        //从redis中获取验证码
        String serverCode = getValideCode();
        //判断
        if (!smsCode.equals(serverCode)) {
            request.setAttribute("valideCodeError","验证码输入有误");
            return "forward:/register.jsp";
        }
        //删除缓存验证码
        removeValideCode();
        //数据库中添加用户信息
        userService.add(user);
        //将user设置到session中
        HttpSession session = request.getSession();
        session.setAttribute("currentUser",user);
        return "forward:/WEB-INF/pages/register_ok.jsp";
    }

/*------------------------------------登录模块---------------------------------------*/
    //密码登录
    @RequestMapping("/login4PWD")
    public void login4PWD(User user, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        ResultInfo resultInfo = userService.login(user);
        //响应格式
        response.setContentType("application/json;charset=utf-8");
        if(resultInfo.isSuccess()){//登录成功
            User currentUser= (User) resultInfo.getObject();
            session.setAttribute("currentUser", currentUser);
            //json转换
            try {
                String json = ObjectToStringUtil.objToJson(resultInfo);
                //获取输出流
                response.getWriter().write(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //登录失败
            //json转换
            try {
                String json = ObjectToStringUtil.objToJson(resultInfo);
                response.getWriter().write(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //短信登录
    @RequestMapping("/login4SMS")
    public void login4SMS(String telephone,String valideCode,HttpServletRequest request,HttpServletResponse response){
        //响应格式
        response.setContentType("application/json;charset=utf-8");
        //从redis是获取验证码
        String serverCode=getValideCode();
        //判断验证码
        if(!valideCode.equalsIgnoreCase(serverCode)){
            ResultInfo resultInfo = new ResultInfo(null, false, "验证码不正确");
            try {
                String json = ObjectToStringUtil.objToJson(resultInfo);
                response.getWriter().write(json);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //删除缓存
        removeValideCode();
        //根据telephone查询
        ResultInfo resultInfo=userService.findByTel4Login(telephone);
        //session中设置user
        HttpSession session = request.getSession();
        User currentUser= (User) resultInfo.getObject();
        session.setAttribute("currentUser",currentUser);
        //响应给前端
        try {
            String json = ObjectToStringUtil.objToJson(resultInfo);
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/valideCodeSMS")
    public void valideCodeSMS(String telephone,HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        //生成6位数随机验证码
        String serverCode = RandomStringUtils.randomNumeric(6);
        System.out.println(serverCode);
        //获取阿里云短信服务
        ResultInfo resultInfo = userService.smsService(telephone, serverCode);
        String json=null;
        try {
            json = ObjectToStringUtil.objToJson(resultInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //判断
        if (resultInfo.isSuccess()) {
            //将验证码存入到redis中，有效时间为5min
            Jedis jedis = JedisUtil.getResource();
            jedis.setex("valideCode",300,serverCode);
            //将结果发送给前端
            try {
                response.getWriter().write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //将结果发送给前端
            try {
                response.getWriter().write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*------------------------------------退出模块---------------------------------------*/
    @RequestMapping("/logOff")
    public String logOff(HttpSession session){
        session.invalidate();
        return "redirect:/index.jsp";
    }

    /*------------------------------------用户个人中心模块---------------------------------------*/
    @RequestMapping("/userInfo")
    public void userInfo(User user,MultipartFile picFile,HttpServletRequest request, HttpServletResponse response){
        //判断用户登入转态
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if (!resultInfo.isSuccess()) {
            //重新登录
            try {
                ObjectToStringUtil.write(resultInfo,response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        //更新当前用户的个人信息
        User currentUser= (User) resultInfo.getObject();
        String telephone = currentUser.getTelephone();
        //调用UserService层
        //保存用户上传的文件
        //储存项目路径下的路径
        if(picFile.getOriginalFilename().length()>0) {//图片更新
            String path = "/pic/";
            String realPath = request.getServletContext().getRealPath(path);
            //设置用户头像保存地址
            //中文文件名编码
            String filename=null;
            try {
                 filename = URLEncoder.encode(picFile.getOriginalFilename(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            user.setPic(path + filename);
            try {
                picFile.transferTo(new File(realPath + filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        resultInfo=userService.update(telephone,user);
        //更新当前用户
        currentUser= (User) resultInfo.getObject();
        request.getSession().setAttribute("currentUser",currentUser);
        //响应给前端
        try {
            ObjectToStringUtil.write(resultInfo,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*------------------------------------用户地址管理模块---------------------------------------*/
    @RequestMapping("/addressManage")
    public String addressManage(HttpServletRequest request,HttpServletResponse response){
        //判断用户登录状态
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            return "redirect:/index.jsp";
        }
        User currentUser= (User) resultInfo.getObject();
        List<Address> list=userService.showAddress(currentUser.getUid());
        //设置request域
        request.getSession().setAttribute("addressList",list);
        return "redirect:/home_address.jsp";

    }
    //添加用户地址信息
    @RequestMapping("/addAddress")
    public String addAddress(Address address,HttpServletRequest request,HttpServletResponse response){
        //判断用户是否处于登录状态
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if (!resultInfo.isSuccess()) {
            //重定向到index.jsp
            return "redirect:/index.jsp";
        }
        //设置用户id
        User currentUser= (User) resultInfo.getObject();
        address.setUid(currentUser.getUid());
        //调用service层
        resultInfo=userService.addAddress(address);
        //将结果封装到request域
        return "forward:/userController/addressManage";
    }

    //删除用户地址信息
    @RequestMapping("/deleteAddress")
    public void deleteAddress(int aid,HttpServletRequest request,HttpServletResponse response){
        //判断用户登录状态
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            try {
                ObjectToStringUtil.write(resultInfo,response);//重新登录
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        User currentUser= (User) resultInfo.getObject();
        List<Address> list=userService.deleteAddress(aid,currentUser.getUid());
        //删除成功
        try {
            ObjectToStringUtil.write(list,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //更新地址
    @RequestMapping("/updateAddress")
    public String updateAddress(Address address,HttpServletRequest request,HttpServletResponse response){
        //判断用户登录转态
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            //重新登录
            return "redirect:/index.jsp";
        }
        //更新用户地址信息
        resultInfo=userService.updateAddress(address);
        //转发
        return "forward:/userController/addressManage";
    }

    //设置默认
    @RequestMapping("/setDefault")
    public void setDefault(int aid,HttpServletRequest request,HttpServletResponse response){
        //判断用户登录状态
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            //重新登录
            try {
                ObjectToStringUtil.write(resultInfo,response);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //设置默认
        User currentUser= (User) resultInfo.getObject();
        List<Address> list=userService.setDefault(aid,currentUser);
        //返回
        try {
            ObjectToStringUtil.write(list,response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*------------------------------------方法调用模块---------------------------------------*/
    //从redis中获取valideCode
    public String getValideCode() {
        String serverCode=null;
        //获取jedis的连接对象
        Jedis jedis = JedisUtil.getResource();
        //从redis中获取valideCoide
        serverCode = jedis.get("valideCode");
        JedisUtil.close(jedis);
        return serverCode;
    }

    //从redis中删除valideCode
    public void removeValideCode(){
        Jedis jedis = JedisUtil.getResource();
        jedis.del("valideCode");
    }


}
