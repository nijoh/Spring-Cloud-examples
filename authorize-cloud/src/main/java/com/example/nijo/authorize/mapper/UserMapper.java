package com.example.nijo.authorize.mapper;

import com.example.nijo.common.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert(value = "insert into user(user_name,email,password) values(#{userName},#{email},#{password})")
    int saveUser(User user);

    @Select(value = "select id,user_name,email,password from user where user_name = #{userName}")
    User findUserByUserName(String userName);
}
