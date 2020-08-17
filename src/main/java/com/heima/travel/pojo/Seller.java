package com.heima.travel.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Seller {
    private int sid;
    private String sname;
    private String consphone;
    private String address;
}
