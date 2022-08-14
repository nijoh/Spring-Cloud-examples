package com.example.nijo.authorize.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 配置类
 * 1.加入编码解码器
 * 2.自定义登陆认证 authenticationManager.authenticate会进入它
 * 3.请求过滤和授权配置
 * 4.加入AuthenticationManager管理器
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomerAuthenticationProvider customerAuthenticationProvider;

    private TokenPerRequestFilter tokenPerRequestFilter;
    /**
     * 加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义登陆认证器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customerAuthenticationProvider);
        //自定义获取用户信息
        //auth.userDetailsService(null);
    }

    /**
     * 基于Session配置
     * */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //所有请求验证接口并关闭CSRF、放行/login 其余全部请求拦截
//        http.authorizeRequests().antMatchers("/login").permitAll()
//                .anyRequest().authenticated()
//                .and().logout().permitAll() //允许所有用户登出
//                .and().httpBasic()
//                .and().csrf().disable();
//    }

    /**
     * 基于JWT配置
     * 关闭sesiso
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //所有请求验证接口并关闭CSRF、放行/login 其余全部请求拦截
        http.authorizeRequests().antMatchers("/login").anonymous()
                .anyRequest().authenticated()
                .and().logout().permitAll() //允许所有用户登出
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//关闭Session
                .and()
                .addFilterBefore(tokenPerRequestFilter, UsernamePasswordAuthenticationFilter.class)//在UsernamePasswordAuthenticationFilter之前验证
                .csrf().disable();
    }

    @Autowired
    public void setCustomerAuthenticationProvider(CustomerAuthenticationProvider customerAuthenticationProvider) {
        this.customerAuthenticationProvider = customerAuthenticationProvider;
    }

    @Autowired
    public void setTokenPerRequestFilter(TokenPerRequestFilter tokenPerRequestFilter){
        this.tokenPerRequestFilter=tokenPerRequestFilter;
    }

    /**
     * 注入AuthenticationManager管理器
     **/
    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
