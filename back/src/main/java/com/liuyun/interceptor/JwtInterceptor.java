package com.liuyun.interceptor;

import com.liuyun.common.Token;
import com.liuyun.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private Token token;
    @Autowired
    private UserService userService; // 假设你有一个UserService来处理用户相关的业务逻辑

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String authHeader = request.getHeader("authorization");
        System.out.println("authHeader"+authHeader);
        Token tokenUtil = new Token();
        if (authHeader != null  ) {
            // 判断token是否有效
            boolean isValid = tokenUtil.isValidToken(authHeader);
            System.out.println("Is token valid? " + isValid);
            if(!isValid){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token is not valid");
                return false;
            }
            // 解析token
            String userName = tokenUtil.resolveToken(authHeader);
            System.out.println("User name from token: " + userName);

            if (userService.queryByUserName(userName)!=null) {
                // 用户存在，继续处理请求
                return true;
            } else  {
                // 用户不存在，返回未授权状态
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return false;
            }
        } else {
            System.out.println("没有token");
            // 没有提供token，返回未授权状态
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header not provided");
            return false;
        }
    }
}
