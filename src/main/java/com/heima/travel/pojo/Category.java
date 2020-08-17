package com.heima.travel.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Category {
    private int cid;
    private String cname;
}
