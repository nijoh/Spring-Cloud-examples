package com.example.nijo.authorize.controller;

import com.example.nijo.authorize.pojo.UserInfo;
import com.example.nijo.authorize.service.UserService;
import com.example.nijo.common.dto.LoginUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserInfo login(@RequestBody Map<String, String> params){
        String userName = params.get("username");
        String password = params.get("password");
        return userService.login(LoginUserDTO.builder().userName(userName).password(password).build());
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
