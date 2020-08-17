package com.heima.travel.service;

import com.heima.travel.pojo.*;

import java.util.Map;

public interface OrderService {
    //生成订单
    ResultInfo addOrder(Address deliveryAddress, Integer[] rids);

    PageBean findOrdersByUid(int uid, int currentPage, int count,int state);

    void updateOrderState(Map<String,String> param);

    ResultInfo orderState(String oid);

    Order findPayedOrderByOid(String oid);

    Order findUnpayedOrderByOid(String oid);
}
