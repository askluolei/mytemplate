package com.luolei.template.common.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 0:17
 */
@ConfigurationProperties(prefix = "template.jwt")
@Component
@Getter
@Setter
public class TokenGenerator {

    public static final String RANDOM_KEY = "random";

    private static final String TEMP_TOKEN_TYPE_KEY = "type";
    private static final String TEMP_TOKEN_TYPE = "temporary";

    private static final String TOKEN_TYPE_KEY = "type";
    private static final String TOKEN_TYPE = "accessToken";

    private String secret;
    private long expire;
    private long rememberExpire;

    /**
     * 生成 accessToken
     */
    public String generateToken(long userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        return Jwts.builder()
                .setSubject(userId+"")
                .claim(TOKEN_TYPE_KEY, TOKEN_TYPE)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成 accessToken
     * 添加随机数
     */
    public String generateToken(long userId, long random) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        return Jwts.builder()
                .setSubject(userId+"")
                .claim(TOKEN_TYPE_KEY, TOKEN_TYPE)
                .claim(RANDOM_KEY, random)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成 refreshToken
     */
    public String generateRefreshToken(long userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + rememberExpire * 1000);
        return Jwts.builder()
                .setSubject(userId + "")
                .claim(TOKEN_TYPE_KEY, TOKEN_TYPE)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成 refreshToken
     * 添加随机数
     */
    public String generateRefreshToken(long userId, long random) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + rememberExpire * 1000);
        return Jwts.builder()
                .setSubject(userId + "")
                .claim(TOKEN_TYPE_KEY, TOKEN_TYPE)
                .claim(RANDOM_KEY, random)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成临时token
     * 指定token的有效期
     * @param expireTime 有效期 单位 秒
     * @return
     */
    public String genTempToken(long expireTime) {
        return genTempToken(expireTime, TimeUnit.SECONDS);
    }

    /**
     * 生成临时token
     * 指定token的有效期
     * @param expireTime
     * @param timeUnit
     * @return
     */
    public String genTempToken(long expireTime, TimeUnit timeUnit) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret)
                .claim(TEMP_TOKEN_TYPE_KEY, TEMP_TOKEN_TYPE)
                .setSubject("template")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeUnit.toMillis(expireTime)))
                .compact();
    }

    /**
     * 校验临时token
     * @param token
     */
    public void checkTempToken(String token) {
        Jwts.parser().setSigningKey(secret)
                .require(TEMP_TOKEN_TYPE_KEY, TEMP_TOKEN_TYPE)
                .parseClaimsJws(token);
    }

    public long getUserID(String token) {
        return Long.parseLong(getClaimByToken(token).getSubject());
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .require(TOKEN_TYPE_KEY, TOKEN_TYPE)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }
}
