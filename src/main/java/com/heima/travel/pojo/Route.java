package com.heima.travel.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@JsonIgnoreProperties({"handler"})
public class Route {
    private int rid;
    private String rname;
    private double price;
    private String routeIntroduce;
    private int rflag;
    private String rdate;
    private int isThemeTour;
    private int num;
    private int cid;
    private String rimage;
    private int sid;
    private String sourceId;

    private Category category;
    private Seller seller;
    private List<RouteImg> routeImgList;
//    private List<OrderItem> orderItems;



}
