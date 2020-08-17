package com.heima.travel.service;

import com.heima.travel.pojo.*;

import java.util.List;

public interface RouteService {
    List<Category> getCategory();

    PageBean findRouteByCid(int cid,int currentPage,int count);

    Route findRouteDetail(String rid);

    ResultInfo addCommodity(int uid, int rid,int num);

    Route getRoute(int rid);

    PageBean findRouteByName(String keyword, int currentPage, int count);

    Cart showCart(int uid);

    void deleteCommodity(int uid,int rid);

    Cart selectedCart(int uid, Integer[] rids);
}
