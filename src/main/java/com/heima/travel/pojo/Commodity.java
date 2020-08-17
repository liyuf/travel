package com.heima.travel.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Commodity {
    private int rid;
    private int num;//数量
    private double subCost;//小计金额
    private Route route;//路线

    public double getSubCost(){
       return route.getPrice()*num;
    }

    public int getRid(){
        return route.getRid();
    }
    //限制访问
    private void setSubCost(double subCost){}
    private void setRid(int rid){}
}
