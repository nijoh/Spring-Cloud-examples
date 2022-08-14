package com.example.nijo.authorize.config;

import com.alibaba.fastjson.JSON;
import com.example.nijo.authorize.service.UserService;
import com.example.nijo.common.entity.RespEntity;
import com.example.nijo.common.enums.RespEnum;
import com.example.nijo.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Token过滤器
 * desc:验证请求Token合法性
 */
@Component
public class TokenPerRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //  获取Authorization属性
        String token = request.getHeader("Authorization");
        Map<String, String> result = new HashMap<>();
        if (StringUtils.isNotEmpty(token)) {
            //解析Token获取载荷
            try {
                Claims claims = JwtUtil.parseJWT(token);
                //获取用户信息 在创建Token Subject是传入json  这里可以获取你json值 Subject是可以任何，怎么set 就怎么get
                String userName = claims.getSubject();
                UserDetails userDetails = userService.loadUser(userName);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //放入上下文
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (ExpiredJwtException exception) {
                //Token过期
                System.out.println("Token过期");
                WriteJSON(request, response, new RespEntity(RespEnum.USER_SESSION_INVALID));
                //这里也可以转发，在转发URL做JSON返回处理 不用WriteJSON
                // request.getRequestDispatcher("/url").forward(request,response);
                return;
            } catch (Exception e) {
                //非法Token
                e.printStackTrace();
                System.out.println("非法Token");
                WriteJSON(request, response, new RespEntity(RespEnum.TOKEN_VALIDATE_FAILED));
                return;
            }
        }
        //放行
        filterChain.doFilter(request, response);
    }

    private void WriteJSON(HttpServletRequest request,
                           HttpServletResponse response,
                           Object obj) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        //输出JSON
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(obj));
        out.flush();
        out.close();
    }
}
