package com.heima.travel.pojo;

import org.apache.ibatis.annotations.Param;

public class Search {

    //关键字查询
    public String keyword4Search(String keyword){
        String sql="select count(*) from tab_route where rname like concat(\"%\",\""+keyword+"\",\"%\")";
        return sql;
    }

    //路线列表查询
    public String showRouteList(@Param("keyword") String keyword, @Param("currentPage") int currentPage,@Param("count") int count){
        String sql="select * from tab_route where rname like concat(\"%\",\""+keyword+"\",\"%\") limit "+currentPage+","+count;
        return sql;
    }

    //地址查询
    public String findAddressByUid(String uid){
        String sql="select * from tab_address where uid="+uid;
        return sql;
    }

    //订单查询
    public String findOrderByUid(String uid){
        String sql="select * from tab_order where uid="+uid;
        return sql;
    }

}
