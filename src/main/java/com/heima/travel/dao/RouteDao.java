package com.heima.travel.dao;

import com.heima.travel.pojo.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface RouteDao {

    //获取导航
    @Select("select * from tab_category")
    List<Category> getCategory();
    //获取导航类
    @Select("select * from tab_category where cid=#{cid}")
    Category findCategoryByCid(int cid);

    //获取商家信息
    @Select("select * from tab_seller where sid=#{sid}")
    Seller findSellerBySid(int sid);

    //获取路线图片信息
    @Select("select * from tab_route_img where rid=#{rid}")
    List<RouteImg> findRouteImgByRid(int rid);

    //查询所有路线
    @Select("select count(1) from tab_route where cid=#{cid}")
    int findRouteByCid(int cid);

    //显示路线
    @Select("select * from tab_route where cid=#{cid} limit #{start},#{count}")
    List<Route> showRouteList(@Param("cid") int cid, @Param("start") int start, @Param("count") int count);

    @Results({
            @Result(id = true,column = "rid",property = "rid"),
            @Result(property = "category",one = @One(select="com.heima.travel.dao.RouteDao.findCategoryByCid",fetchType = FetchType.LAZY),column = "cid"),
            @Result(property = "seller",one=@One(select = "com.heima.travel.dao.RouteDao.findSellerBySid",fetchType = FetchType.LAZY),column="sid"),
            @Result(property = "routeImgList",many = @Many(select = "com.heima.travel.dao.RouteDao.findRouteImgByRid",fetchType = FetchType.LAZY),column = "rid")
        })
    @Select("select * from tab_route where rid=#{rid}")
    Route findRouteDetailByRid(String rid);

    //获取总搜索数
    @SelectProvider(type= Search.class,method="keyword4Search")
    int findRouteByName(String keyword);


    @Results({
            @Result(id = true, column = "rid", property = "rid"),
            @Result(property = "category",one=@One(select="com.heima.travel.dao.RouteDao.findCategoryByCid",fetchType = FetchType.LAZY),column = "cid"),
            @Result(property = "seller",one=@One(select = "com.heima.travel.dao.RouteDao.findSellerBySid",fetchType = FetchType.LAZY),column="sid"),
            @Result(property = "routeImgList",many = @Many(select = "com.heima.travel.dao.RouteDao.findRouteImgByRid",fetchType = FetchType.LAZY),column = "rid")
    })
    @SelectProvider(type=Search.class,method="showRouteList")
    List<Route> showRouteFindByName(@Param("keyword") String keyword,@Param("currentPage") int start, @Param("count") int count);
}
