package com.heima.travel.dao;

import com.heima.travel.pojo.Order;
import com.heima.travel.pojo.OrderItem;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface OrderDao {

    //查询订单总数目
    @Select("select count(*) from tab_order where uid=#{uid} and state=#{state}")
    int findTotalCountByUid(@Param("uid") int uid,@Param("state") int state);

    //显示订单
    @Results({
            @Result(column = "oid",property = "oid",id = true),
            @Result(property = "orderItems",many = @Many(select = "com.heima.travel.dao.OrderDao.findOrderItemByOid"),column = "oid")
    })
    @Select("select * from tab_order where uid=#{uid} and state=#{state} limit #{start},#{count}")
    List<Order> findOrderByUid(@Param("uid") int uid,@Param("start") int start,@Param("count") int count,@Param("state") int state);
    //添加订单
    @Insert("insert into tab_order values(#{oid},#{ordertime},0,#{state},#{address},#{contact},#{telephone},#{uid})")
    void addOrder(Order order);
    //添加订单详情
    @Insert("insert into tab_orderitem values(null,#{itemtime},#{state},#{num},#{subtotal},#{rid},#{oid})")
    void addOrderItem(OrderItem item);

    //查询订单
    @Results({
            @Result(column = "oid",property = "oid",id = true),
            @Result(property = "orderItems",many = @Many(select = "com.heima.travel.dao.OrderDao.findOrderItemByOid"),column = "oid")
    })
    @Select("select * from tab_order where oid=#{oid}")
    Order findOrderByOid(String oid);

    //查询订单详情
    @Results({
            @Result(column = "rid",property = "rid",id = true),
            @Result(property = "route",one=@One(select = "com.heima.travel.dao.RouteDao.findRouteDetailByRid"),column = "rid")
    })
    @Select("select * from tab_orderitem where oid=#{oid}")
    OrderItem findOrderItemByOid(String oid);

    //修改订单状态
    @Update("update tab_order set state=1 where oid=#{oid}")
    void updateOrderStateByOid(String oid);

    @Select("select state from tab_order where oid=#{oid}")
    int findOrderStateByOid(String oid);
}
