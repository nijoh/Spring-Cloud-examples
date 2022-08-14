package com.example.nijo.authorize.service;

import com.example.nijo.authorize.pojo.UserInfo;
import com.example.nijo.common.dto.LoginUserDTO;
import com.example.nijo.common.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    int saveUser(User user);

    UserInfo login(LoginUserDTO userDTO);

    User findUserByUserName(String userName);

    UserDetails loadUser(String username);

}
