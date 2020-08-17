package com.heima.travel.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RouteImg {
    private int rgid;
    private int rid;
    private String bigPic;
    private String smallPic;
}
