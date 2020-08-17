package com.heima.travel.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@Component
public class User {
    private int uid;
    private String username;
    private String password;
    private String telephone;
    private String nickname;
    private int sex;
    private String birthday;
    private String email;
    private String pic;

    private List<Address> addresses;
    private List<Order> orders;
}
