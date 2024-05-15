package com.liuyun.common;

import com.auth0.jwt.JWT;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.liuyun.pojo.User;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;

/**
 * @PackageName com.liuyun.common
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/6 17:39
 * @Version 1.0
 */
@Component
public class Token {
    public String createToken(User user){
        // 指定token过期时间为3000秒
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3000);
        String token = JWT.create()
                .withHeader(new HashMap<>())  // Header
                .withClaim("userName", user.getUserName())  // Payload
                .withExpiresAt(calendar.getTime())  // 过期时间
                .sign(Algorithm.HMAC256("LOVE_YOMIYA"));  // 签名用的secret
        System.out.println(token);
        return token;
    }
    public String resolveToken(String token){
        // 创建解析对象，使用的算法和secret要与创建token时保持一致
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("LOVE_YOMIYA")).build();
        // 解析指定的token
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        // 获取解析后的token中的payload信息
        //Claim userId = decodedJWT.getClaim("userId");
        Claim userName = decodedJWT.getClaim("userName");
        //System.out.println(userId.asInt());
        System.out.println(userName.asString());
        System.out.println(decodedJWT.getExpiresAt());    // 输出超时时间
        return userName.asString();
    }
    public boolean isValidToken(String token) {//检测是否有效
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("LOVE_YOMIYA")).build();
            jwtVerifier.verify(token);
            return true; // Token is valid
        } catch (Exception e) { // Token is invalid or expired
            return false;
        }
    }



}
