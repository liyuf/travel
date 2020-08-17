package com.heima.travel.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heima.travel.dao.RouteDao;
import com.heima.travel.pojo.*;
import com.heima.travel.service.RouteService;
import com.heima.travel.util.JedisUtil;
import com.heima.travel.util.MybatisUtil;
import com.heima.travel.util.ObjectToStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired private Commodity commodity;
    @Autowired private Cart cartObj;
    @Autowired private PageBean pageBean;
    @Autowired private Cart cart;
    @Autowired private Cart selectedCart;
    private RouteDao routeDao=MybatisUtil.getProxyObject(RouteDao.class);

    //导航列表
    @Override
    public List<Category> getCategory() {
        List<Category> list=null;
        //从redis中获取分类信息
        Jedis jedis = JedisUtil.getResource();
        String routeCategory = jedis.get("routeCategory");
        //判断
        if(StringUtils.isNotEmpty(routeCategory)){
            //将json的字符串直接返回
            try {
                list = ObjectToStringUtil.jsonToObj(routeCategory, List.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            list=routeDao.getCategory();
            try {
                //放入到redis缓存中
                String json = ObjectToStringUtil.objToJson(list);
                jedis.set("routeCategory",json);
                jedis.close();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    //旅游线路
    @Override
    public PageBean findRouteByCid(int cid,int currentPage,int count) {
        pageBean.setCount(count);
        pageBean.setCurrentPage(currentPage);
        //获取路线的总条数
        int total=routeDao.findRouteByCid(cid);
        //计算总页数
        int totalPages=total/pageBean.getCount();
        //显示路线
        int start=(pageBean.getCurrentPage()-1)*pageBean.getCount();
        List<Route> routeList=routeDao.showRouteList(cid,start,count);
        //封装信息
        pageBean.setTotal(total);
        pageBean.setTotalPages(totalPages);
        pageBean.setList(routeList);
        return pageBean;
    }

    //线路详情
    @Override
    public Route findRouteDetail(String rid) {
        Route routeDetail=null;
        routeDetail=routeDao.findRouteDetailByRid(rid);
        return routeDetail;
    }

    //添加路线
    @Override
    public ResultInfo addCommodity(int uid, int rid,int num) {
        ResultInfo resultInfo=null;
        Commodity choiced=commodity;//当前选中的商品
        choiced.setNum(num);//选中的数量
        String uidStr=String.valueOf(uid);
        //从redis中获取购物车信息
        Jedis jedis = JedisUtil.getResource();
//        判断是否有购物车
        if(jedis.exists(uidStr)){
            String cartStr = jedis.get(uidStr);
            //转换成列表
            try {
                Cart cartObj=ObjectToStringUtil.jsonToObj(cartStr,Cart.class);
                //获取购物车
                List<Commodity> cart = cartObj.getCart();//商品列表
                boolean flag=false;//是否存在
                for (Commodity commodity : cart) {
                    if(commodity.getRoute().getRid()==rid){
                        //获取商品信息
                        Route route = routeDao.findRouteDetailByRid(String.valueOf(rid));
                        choiced.setRoute(route);
                        //更新购物车
                        Integer count = commodity.getNum();
                        count=count+num;
                        commodity.setNum(count);
                        flag=true;
                        break;
                    }
                }
                if(flag==false){
                    //从数据库中获取此商品
                    Route route = routeDao.findRouteDetailByRid(String.valueOf(rid));
                    //当前的商品
                    choiced.setRoute(route);
                    //存放在map中
                    commodity.setRoute(route);
                    commodity.setNum(num);
                    cart.add(commodity);
                }
                cartObj.setCart(cart);
                //将cart重新放入到redis中
                cartStr = ObjectToStringUtil.objToJson(cartObj);
                jedis.set(uidStr,cartStr);
                jedis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //从数据库中获取此商品
            Route route = routeDao.findRouteDetailByRid(String.valueOf(rid));
            //当前选中的路线信息
            choiced.setRoute(route);
            cartObj.setUid(uid);
            commodity.setRoute(route);
            commodity.setNum(num);
            List<Commodity> cart=new ArrayList<>();
            //放入购物车
            cart.add(commodity);
            cartObj.setCart(cart);
            //转成json
            try {
                String cartStr=ObjectToStringUtil.objToJson(cartObj);
                jedis.set(uidStr,cartStr);
                jedis.close();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        //更新成功
        resultInfo=new ResultInfo(choiced,true,"加入购物车成功");
        return resultInfo;
    }

    //商品回显
    @Override
    public Route getRoute(int rid) {
        Route route=null;
        route  = routeDao.findRouteDetailByRid(String.valueOf(rid));
        return route;
    }

    //分页显示
    @Override
    public PageBean findRouteByName(String keyword, int currentPage, int count) {
        //显示的结果起始条数
        int start=(currentPage-1)*count;
        //获取所有的结果
        int total=routeDao.findRouteByName(keyword);
        //总页数
        int totalPages=(int)Math.ceil(total*1.0/count);
        //显示路线
        List<Route> list=routeDao.showRouteFindByName(keyword,start,count);
        //封装数据
        pageBean.setList(list);
        pageBean.setCount(count);
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotal(total);
        pageBean.setTotalPages(totalPages);
        return pageBean;
    }

    //显示购物车
    @Override
    public Cart showCart(int uid) {
        String uidStr=String.valueOf(uid);
        //查询redis中的购物车数据
        Jedis jedis = JedisUtil.getResource();
        if(jedis.exists(uidStr)){
            String cartStr = jedis.get(uidStr);
            //发序列化购物车
            try {
                cart = ObjectToStringUtil.jsonToObj(cartStr, Cart.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jedis.close();
        return cart;
    }

    //删除购物车商品
    @Override
    public void deleteCommodity(int uid,int rid) {
        String uidStr=String.valueOf(uid);
        //从redis中获取购物车信息
        Jedis jedis = JedisUtil.getResource();
        String cartStr = jedis.get(uidStr);
        //反序列化
        try {
            Cart cart = ObjectToStringUtil.jsonToObj(cartStr, Cart.class);
            List<Commodity> cartList = cart.getCart();
            Iterator<Commodity> iterator = cartList.iterator();
            while (iterator.hasNext()){
                Commodity commodity = iterator.next();
                if (commodity.getRoute().getRid()==rid){
                    iterator.remove();
                    break;
                }
            }
            //更新购物车
            cart.setCart(cartList);
            //序列化
            cartStr = ObjectToStringUtil.objToJson(cart);
            //存放到redis中
            jedis.set(uidStr,cartStr);
            jedis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //显示已选中的购物车商品
    @Override
    public Cart selectedCart(int uid, Integer[] rids) {
        String uidStr=String.valueOf(uid);
        //转化rids数组为列表
        List<Integer>  ridList= new ArrayList<>();
        Collections.addAll(ridList,rids);
        //存放商品的容器
        List<Commodity> commodityList = new ArrayList<>();
        //从redis中获取
        //获取redis连接
        Jedis jedis = JedisUtil.getResource();
        //判断jedis
        if(jedis.exists(uidStr)){
            String cartStr = jedis.get(uidStr);
            //发序列化为cart
            try {
                Cart cart = ObjectToStringUtil.jsonToObj(cartStr, Cart.class);
                //获取商品列表
                List<Commodity> commodities = cart.getCart();
                //遍历
                for (Commodity commodity : commodities) {
                    //获取迭代器
                    Iterator<Integer> iterator = ridList.iterator();
                    int routeId = commodity.getRid();
                    //查询
                    while(iterator.hasNext()){
                        Integer rid = iterator.next();
                        int ridInt=rid;
                        if(routeId==rid){
                            //获得后过滤
                            iterator.remove();
                            commodityList.add(commodity);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                jedis.close();
            }
        }
        //封装cart
        selectedCart.setCart(commodityList);
        selectedCart.setUid(uid);
        return selectedCart;
    }





}
