package com.example.nijo.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;


public class JwtUtil {
    //私钥
    private static String SECRET_KEY = "NiJoSecret";
    //过期时间30S
    private static Long EXPIRATION_TIME = 60*1000L;
    //签名
    private static String ISS = "MostSnails";

    /**
     * 生成JWT带超时间
     *
     * @param subject   有效载荷 （主题-JSON格式字符串）
     * @param ttlMillis 有效时间 null 默认使用EXPIRATION_TIME变量值
     * @return String Token
     */
    public static String createJWT(String subject, Long ttlMillis) {
        //签发时间
        long issuedAt = System.currentTimeMillis();
        //过期时间 = 签发时间 + 定义过期时间
        long expirationTime = (Objects.isNull(ttlMillis) || ttlMillis == 0 ? EXPIRATION_TIME : ttlMillis) + issuedAt;
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey key = generalKey();
        return Jwts.builder().setIssuer(ISS)//签名
                .setIssuedAt(new Date(issuedAt))//签发时间
                .signWith(signatureAlgorithm, key)//加密方式HS256和私钥加密后AES
                .setExpiration(new Date(expirationTime))//过期时间
                .setSubject(subject)
                .compact();
    }

    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析Token获取有效载荷（即主题）
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String token) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
