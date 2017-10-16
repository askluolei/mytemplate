package com.luolei.template.common.security;

import java.util.Date;

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
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public long getUserID(String token) {
        return Long.parseLong(getClaimByToken(token).getSubject());
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }
}
