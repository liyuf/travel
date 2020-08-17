package com.heima.travel.util;

import com.heima.travel.pojo.ResultInfo;
import com.heima.travel.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginStatuUtil {
    public static ResultInfo loginStatus(HttpServletRequest request){
        ResultInfo resultInfo=null;
        //获取当前session
        HttpSession session = request.getSession();
        //查看当前用户
        User currentUser = (User) session.getAttribute("currentUser");
        //判断
        if (currentUser==null) {
            resultInfo=new ResultInfo(null,false,"请重新登录...");
        }else{
            resultInfo = new ResultInfo(currentUser, true, "");
        }
        return resultInfo;
    }
}
