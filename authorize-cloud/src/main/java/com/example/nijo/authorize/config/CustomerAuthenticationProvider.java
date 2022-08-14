package com.example.nijo.authorize.config;

import com.example.nijo.authorize.service.UserService;
import com.example.nijo.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义认证逻辑
 */
@Component
public class CustomerAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 验证登陆认证逻辑
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //用户名
        String name = authentication.getName();
        //密码
        String password = authentication.getCredentials().toString();
        //查找用户
        User oldUser = userService.findUserByUserName(name);
        //比对密码
        boolean loginResult = passwordEncoder.matches(password, oldUser.getPassword());
        if (loginResult) {
            //封装信息返回
            UserDetails userDetails = userService.loadUser(name);
            //注意这里password 是未加密 userDetails 中 password 已加密的 也可以传null
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        }
        throw new AuthenticationException("登陆失败") {
        };
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
