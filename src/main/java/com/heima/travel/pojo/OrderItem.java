package com.heima.travel.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@Data
public class OrderItem {
    private int itemid;
    private Timestamp itemtime;
    private int state;
    private int num;
    private double subtotal;
    private int rid;
    private String oid;

    private Route route;
}
