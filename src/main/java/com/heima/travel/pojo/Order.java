package com.heima.travel.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@Data
public class Order {
    private String oid;
    private Timestamp ordertime;
    private double total;
    private int state;
    private String address;
    private String contact;
    private String telephone;
    private int uid;

    private List<OrderItem> orderItems;

    public double getTotal(){
        double total=0.0;
        //遍历orderItems
        if(orderItems!=null){
            for (OrderItem orderItem : orderItems) {
                total=total+orderItem.getSubtotal();
            }
        }
        return total;
    }
    private void setTotal(double total){}
}
