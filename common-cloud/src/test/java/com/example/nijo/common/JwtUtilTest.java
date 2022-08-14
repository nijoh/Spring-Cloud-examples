package com.example.nijo.common;


import com.example.nijo.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;

public class JwtUtilTest {

    public static void main(String[] args) {
        try {
            String jwt = JwtUtil.createJWT("123", null);
            Claims claims = JwtUtil.parseJWT(jwt);
            System.out.println("Token:"+jwt);
            System.out.println("过期时间："+claims.getExpiration());
            System.out.println("签发人："+claims.getIssuer());
            System.out.println("有效载荷："+claims.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
