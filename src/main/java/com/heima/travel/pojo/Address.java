package com.heima.travel.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Address {
    private int aid;
    private int uid;
    private String contact;
    private String address;
    private String telephone;
    private int isdefault;
}
