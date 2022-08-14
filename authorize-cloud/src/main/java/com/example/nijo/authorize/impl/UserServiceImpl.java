package com.example.nijo.authorize.impl;

import com.example.nijo.authorize.mapper.UserMapper;
import com.example.nijo.authorize.pojo.UserInfo;
import com.example.nijo.authorize.service.UserService;
import com.example.nijo.common.dto.LoginUserDTO;
import com.example.nijo.common.entity.User;
import com.example.nijo.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public int saveUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        int result = userMapper.saveUser(user);
        return result;
    }

    public UserInfo login(LoginUserDTO userDTO) {
        //使用security框架自带的验证token生成器  也可以自定义。
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDTO.getUserName(), userDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        //放入Security上下文
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        //获取登陆信息
        UserInfo userInfo = (UserInfo) authenticate.getPrincipal();
        //生成Token
        String jwt = JwtUtil.createJWT(userInfo.getUsername(), null);
        userInfo.setToken(jwt);
        return userInfo;
    }

    public User findUserByUserName(String userName) {
        return userMapper.findUserByUserName(userName);
    }

    @Override
    public UserDetails loadUser(String username) {
        return this.loadUserByUsername(username);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername start>>>>");
        User user = this.findUserByUserName(username);
        if (Objects.isNull(user)) {
            System.out.println("未查询到User用户");
        }
        //可以自定义实现封装返回参数 implements UserDetails 即可 这里使用框架自带Users
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("sys:menu:add");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        //这里封装UserInfo继承User 权限自定义了
        UserInfo userInfo = new UserInfo(username, user.getPassword(), authorities);
        return userInfo;
    }
}
