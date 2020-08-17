package com.heima.travel.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.heima.travel.pojo.*;
import com.heima.travel.service.OrderService;
import com.heima.travel.util.LoginStatuUtil;
import com.heima.travel.util.ObjectToStringUtil;
import com.heima.travel.util.PayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orderController")
public class OrderController {
    @Autowired private OrderService orderService;
    @Autowired private Address deliveryAddress;

    //添加订单
    @RequestMapping("/addOrder")
    public String addOrder(int addressId,Integer[] rids, HttpServletRequest request, HttpServletResponse response){
        //判断用户登录状态
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            //重新登录
            return "redirect:/index.jsp";
        }
        //获取用户地址
        User currentUser= (User) resultInfo.getObject();
        List<Address> addresses = currentUser.getAddresses();
        for (Address address : addresses) {
            if(address.getAid()==addressId){
                deliveryAddress=address;
            }
        }
        //调用service服务，添加订单
        resultInfo=orderService.addOrder(deliveryAddress,rids);
        //转发
        request.setAttribute("order",resultInfo);
        return "forward:/orderController/wxPay";
    }
    //生成支付
    @RequestMapping("/wxPay")
    public String wxPay(HttpServletRequest request,HttpServletResponse response){
        ResultInfo resultInfo = (ResultInfo) request.getAttribute("order");
        if(resultInfo!=null){
            //订单
            Order order= (Order) resultInfo.getObject();
            //微信处理订单
//            String url = PayUtils.createOrder(order.getOid(), (int) order.getTotal());
            String url = PayUtils.createOrder(order.getOid(), 1);
            //将resultInfo设置在request域中
            request.setAttribute("url",url);//支付链接
        }else{
            String oid = request.getParameter("oid");
            //查询订单
            Order order=orderService.findUnpayedOrderByOid(oid);
            resultInfo=new ResultInfo(order,true,"未付款");
            String url = PayUtils.createOrder(oid, 1);
            //将resultInfo设置在request域中
            request.setAttribute("order",resultInfo);
            request.setAttribute("url",url);//支付链接
        }
        //转发
        return "forward:/pay.jsp";
    }
    //支付通知
    @RequestMapping("/payNotify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        //获取servlet流
        try {
            ServletInputStream in = request.getInputStream();
            //xml解析器
            XmlMapper xmlMapper = new XmlMapper();
            //将xml文件解析成map
            Map<String,String> param = xmlMapper.readValue(in, Map.class);
            //调用service修改订单状态
            orderService.updateOrderState(param);
            //返回微信服务，接收成功
            Map<String,String> feedBack=new HashMap<>();
            feedBack.put("return_code","SUCCESS");
            feedBack.put("return_msg","OK");
            //封装到xml文件中
            String xml = xmlMapper.writeValueAsString(feedBack);
            response.setContentType("application/xml;charset=utf-8");
            response.getWriter().write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //订单状态
    @RequestMapping("/orderState")
    public void orderState(String oid,HttpServletRequest request,HttpServletResponse response){
        //调用service
        ResultInfo resultInfo=orderService.orderState(oid);
        //序列化成json
        try {
            String json = ObjectToStringUtil.objToJson(resultInfo);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //支付成功跳转
    @RequestMapping("/paySuccess")
    public String paySuccess(String oid,HttpServletRequest request,HttpServletResponse response){
        //调用service
        Order payedOrder=orderService.findPayedOrderByOid(oid);
        //放入到request域
        request.setAttribute("payedOrder",payedOrder);
        return "forward:/WEB-INF/pages/pay_success.jsp";
    }
    //支付失败
    @RequestMapping("/payFail")
    public String payFail(String oid,HttpServletRequest request,HttpServletResponse response){
        //调用servcie
        Order unpayedOrder=orderService.findUnpayedOrderByOid(oid);
        request.setAttribute("unpayedOrder",unpayedOrder);
        return "forward:/WEB-INF/pages/pay_fail.jsp";
    }


    //显示订单
    @RequestMapping("/showOrders")
    public String showOrders(int currentPage,int count,int uid,int state,HttpServletRequest request,HttpServletResponse response){
        //判断用户登录
        ResultInfo resultInfo = LoginStatuUtil.loginStatus(request);
        if(!resultInfo.isSuccess()){
            //重新登录
            return "redirect:/index.jsp";
        }
        if(state==1){
        //调取服务层
        PageBean orders=orderService.findOrdersByUid(uid,currentPage,count,state);
        //设置request域
        request.setAttribute("orders",orders);
        return "forward:/home_orderlist.jsp";
        }else{
            //调取服务层
            PageBean orders=orderService.findOrdersByUid(uid,currentPage,count,state);
            //设置request域
            request.setAttribute("orders",orders);
            return "forward:/home_unpayedorderlist.jsp";
        }
    }
}
