package com.heima.travel.dao;

import com.heima.travel.pojo.Address;
import com.heima.travel.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserDao {

    //根据用户名查询
    User findByname(String username);

    //根据用户名和密码查询
    User findByTotal(@Param("username") String username,@Param("password") String password);

    //根据手机号码查询
    User findByTel(String telephone);

    void add(User user);

    //更新用户信息
    void update(@Param("telephone") String telephone,@Param("user") User user);

    //添加地址信息
    @Insert("insert into tab_address values(null,#{uid},#{contact},#{address},#{telephone},#{isdefault})")
    int addAddress(Address address);

    //查询地址
    @Select("select * from tab_address where uid=#{uid}")
    List<Address> findAddressByUid(int uid);

    //删除地址
    @Delete("delete from tab_address where aid=#{aid}")
    void deleteAddress(int aid);

    //更新地址
    @Update("update tab_address set contact=#{contact},address=#{address},telephone=#{telephone} where aid=#{aid}")
    int updateAddress(Address address);

    //取消所有默认
    @Update("update tab_address set isdefault=0 where uid=#{uid}")
    int cancleDefault(int uid);

    //设置默认
    @Update("update tab_address set isdefault=1 where aid=#{aid}")
    int setDefault(int aid);
}
