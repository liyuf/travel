package com.heima.travel.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.heima.travel.dao.OrderDao;
import com.heima.travel.pojo.*;
import com.heima.travel.service.OrderService;
import com.heima.travel.util.JedisUtil;
import com.heima.travel.util.MybatisUtil;
import com.heima.travel.util.ObjectToStringUtil;
import com.heima.travel.util.PayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired private Order order;
    @Autowired private OrderItem orderItem;
    @Autowired private PageBean pageBean;
    private OrderDao orderDao=MybatisUtil.getProxyObject(OrderDao.class);
    //显示订单
    @Override
    public ResultInfo addOrder(Address deliveryAddress, Integer[] rids) {
        ResultInfo resultInfo=null;
        SqlSession sqlSession = MybatisUtil.openSession();
        //获得代理对象
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
//        添加订单
        //生成订单号
        String orderId = IdUtil.simpleUUID();
        //生成订单的时间
        Date now = new Date();
        long time = now.getTime();
        Timestamp ordertime = new Timestamp(time);
        //送货地址
        String address=deliveryAddress.getAddress();
        //收货人姓名
        String contact=deliveryAddress.getContact();
        //收货人手机
        String telephone=deliveryAddress.getTelephone();
        //订单所属
        int uid=deliveryAddress.getUid();
        //封装Order
        order.setOid(orderId);
        order.setOrdertime(ordertime);
        order.setState(0);
        order.setAddress(address);
        order.setContact(contact);
        order.setTelephone(telephone);
        order.setUid(uid);
//        生成订单信息
        //生成一个订单信息容器
        List<OrderItem> orderItemList=new ArrayList<>();
        //从cart中获取
        List<Integer> ridList=new ArrayList<>();
        Collections.addAll(ridList,rids);
        //redis连接
        Jedis jedis = JedisUtil.getResource();
        if (jedis.exists(String.valueOf(uid))) {
            String cartStr=jedis.get(String.valueOf(uid));
            //转成购物车
            try {
                Cart cart = ObjectToStringUtil.jsonToObj(cartStr, Cart.class);
                //商品列表
                List<Commodity> commodities = cart.getCart();
                Iterator<Commodity> commodityIterator = commodities.iterator();
                //遍历commodity
                while (commodityIterator.hasNext()){
                    Commodity commodity = commodityIterator.next();
                    int rid = commodity.getRid();
                    Iterator<Integer> ridIterator = ridList.iterator();
                    while(ridIterator.hasNext()){
                        Integer routeId = ridIterator.next();
                        if(routeId==rid){
                            //记录商品信息
                            orderItem.setItemtime(ordertime);
                            orderItem.setState(0);
                            orderItem.setNum(commodity.getNum());
                            orderItem.setSubtotal(commodity.getSubCost());
                            orderItem.setRid(rid);
                            orderItem.setOid(orderId);
                            orderItemList.add(orderItem);
                            //找到后删除该商品
                            ridIterator.remove();
                            commodityIterator.remove();
                        }
                    }
                }
                //重新封装未选中的商品
                cart.setCart(commodities);
                cartStr = ObjectToStringUtil.objToJson(cart);
                //存入到redis中
                jedis.set(String.valueOf(uid),cartStr);
                //关闭资源
                jedis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //数据库中添加订单
            orderDao.addOrder(order);
            //数据库中添加订单详情
            for (OrderItem item : orderItemList) {
                orderDao.addOrderItem(item);
            }

            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        //回显订单
        OrderDao orderDaoProxy = MybatisUtil.getProxyObject(OrderDao.class);
        Order order=orderDaoProxy.findOrderByOid(orderId);
        resultInfo=new ResultInfo(order,true,"订单添加成功");
        return resultInfo;
    }

    @Override
    public PageBean findOrdersByUid(int uid, int currentPage, int count,int state) {
        //封装pageBean
        pageBean.setCurrentPage(currentPage);
        pageBean.setCount(count);
        //查询所有已付款订单
        int total = orderDao.findTotalCountByUid(uid,state);
        //总页数
        int totalPages=(int)Math.ceil(total*1.0/count);
        //查询当前页
        List<Order> orders=orderDao.findOrderByUid(uid,(currentPage-1)*count,count,state);
        //封装
        pageBean.setTotal(total);
        pageBean.setList(orders);
        pageBean.setTotalPages(totalPages);
        return pageBean;
    }

    @Override
    public void updateOrderState(Map<String,String> param) {
        //获取订单号
        String oid = param.get("out_trade_no");
        //对oid状态进行更改
        orderDao.updateOrderStateByOid(oid);
    }

    @Override
    public ResultInfo orderState(String oid) {
        ResultInfo resultInfo=null;
        int state=orderDao.findOrderStateByOid(oid);
        if(state==1){
            resultInfo=new ResultInfo(null,true,"支付成功");
        }else{
            resultInfo=new ResultInfo(null,false,"支付失败");
        }
        return resultInfo;
    }

    @Override
    public Order findPayedOrderByOid(String oid) {
        Order payedOrder=null;
        //调用Dao
         payedOrder = orderDao.findOrderByOid(oid);
        return payedOrder;
    }

    @Override
    public Order findUnpayedOrderByOid(String oid) {
        Order unpayedOrder=null;
        unpayedOrder=orderDao.findOrderByOid(oid);
        return unpayedOrder;
    }
}
