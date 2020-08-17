package com.heima.travel.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Data
public class Cart {
    private int uid;//用户id
    private int total;//商品总数量
    private double totalPrice;//总价格
    private List<Commodity> cart;//商品车

    public int getTotal(){
        int totalnum=0;
        for (Commodity commodity : cart) {
            int num = commodity.getNum();
            totalnum=totalnum+num;
        }
        return totalnum;
    }
    public double getTotalPrice(){
        double totalCost=0;
        for (Commodity commodity : cart) {
            double cost=commodity.getSubCost();
            totalCost=totalCost+cost;
        }
        return totalCost;
    }
    //限制访问
    private void setTotal(int total){}
    //限制访问
    private void setTotalPrice(double totalPrice){}

}
