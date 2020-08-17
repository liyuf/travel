package com.heima.travel.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heima.travel.pojo.*;
import com.heima.travel.service.RouteService;
import com.heima.travel.util.JedisUtil;
import com.heima.travel.util.LoginStatuUtil;
import com.heima.travel.util.ObjectToStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/routeController")
public class RouteController {
    @Autowired private RouteService routeService;

    //导航栏展示
    @RequestMapping("/categoryShow")
    public void categoryShow(HttpServletRequest request, HttpServletResponse response){

            //从数据库中查找
            List<Category> list=routeService.getCategory();
        try {
            ObjectToStringUtil.write(list,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //路线查询
    @RequestMapping("/routeInfo")
    public String routeInfo(int cid,int currentPage,int count,HttpServletRequest request,HttpServletResponse response){
        //查询线路
        PageBean routeList=routeService.findRouteByCid(cid,currentPage,count);
        //将routelist设置到request域中
        request.setAttribute("routeInfo",routeList);
        request.setAttribute("cid",cid);
        return "forward:/route_list.jsp";
    }

    //路线搜索
    @RequestMapping("/routeSearch")
    public String routeSearch(String keyword,int currentPage,int count,HttpServletRequest request,HttpServletResponse response){
        //调用service
        PageBean routeList=routeService.findRouteByName(keyword,currentPage,count);
        //设置到request域
        request.setAttribute("routeInfo",routeList);
        request.setAttribute("keyword",keyword);
        return "forward:/route_list.jsp";
    }

    //路线详情
    @RequestMapping("/routeDetail")
    public String routeDetail(String rid,HttpServletRequest request,HttpServletResponse response){
        //调用service
        Route route=routeService.findRouteDetail(rid);
        //设置request域
        request.setAttribute("routeDetails",route);
        return "forward:/route_detail.jsp";
    }

    //加入购物车
    @RequestMapping("/addCommodityToCart")
    public void addCommodityToCart(int num,int rid,HttpServletRequest request,HttpServletResponse response){
        //判断登录状态
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            //用户重新登录
            try {
                ObjectToStringUtil.write(resultInfo,response);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //添加商品
        User currentUser= (User) resultInfo.getObject();
        resultInfo=routeService.addCommodity(currentUser.getUid(),rid,num);
        //返回给前端
        try {
            ObjectToStringUtil.write(resultInfo,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加入成功
    @RequestMapping("/cartSuccess")
    public String cartSuccess(Commodity commodity,int rid,HttpServletRequest request){
        //调用service
        Route route = routeService.getRoute(rid);
        commodity.setRoute(route);
        //设置到request域中
        request.setAttribute("commodity",commodity);
        return "forward:/WEB-INF/pages/cart_success.jsp";
    }

    //展示购物车
    @RequestMapping("/showCart")
    public String showCart(HttpServletRequest request,HttpServletResponse response){
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            //重新登录
            return "redirect:/index.jsp";
        }
        User currentUser= (User) resultInfo.getObject();
        int uid = currentUser.getUid();
        //获取service服务
        Cart cart=routeService.showCart(uid);
        //保存到request域中
        request.setAttribute("cart",cart);
        return "forward:/cart.jsp";
    }

    //删除购物车
    @RequestMapping("/deleteCart")
    public String deleteCart(int rid,HttpServletRequest request,HttpServletResponse response){
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            //重新登录
            return "redirect:/index.jsp";
        }
        User currentUser= (User) resultInfo.getObject();
        int uid = currentUser.getUid();
        //调用service
        routeService.deleteCommodity(uid,rid);
        return "redirect:/routeController/showCart";
    }

    //显示已选中的商品
    @RequestMapping("/selectedCart")
    public String selectedCart(Integer[] rids,HttpServletRequest request,HttpServletResponse response){
        //判断用户登录状态
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            //重新登录
            return "redirect:/index.jsp";
        }
        //获取用户id
        User currentUser= (User) resultInfo.getObject();
        int uid = currentUser.getUid();
        //调用service
        Cart cart=routeService.selectedCart(uid,rids);
        //封装到request域
        request.setAttribute("cart",cart);
        return "forward:/order_info.jsp";
    }
}
