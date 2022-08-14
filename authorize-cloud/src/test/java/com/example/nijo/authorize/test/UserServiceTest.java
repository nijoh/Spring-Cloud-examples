package com.example.nijo.authorize.test;

import com.alibaba.fastjson.JSON;
import com.example.nijo.authorize.pojo.UserInfo;
import com.example.nijo.authorize.service.UserService;
import com.example.nijo.common.dto.LoginUserDTO;
import com.example.nijo.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void saveUserTest(){
        User user = User.builder().userName("nijo").email("nijo_h@163.com").password("nijo").build();
        int i = userService.saveUser(user);
        System.out.println("saveUserTest success:"+i);
    }

    @Test
    public void loginTest(){
        LoginUserDTO build = LoginUserDTO.builder().userName("nijo").password("nijo").build();
        UserInfo login = userService.login(build);
        System.out.println(JSON.toJSONString(login));
    }
}
