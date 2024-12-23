//package com.example.demo.util;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JsonWebTokenInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private JsonWebTokenUtility jsonWebTokenUtility;
// 
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String method = request.getMethod();
//        if ("OPTIONS".equals(method)) {
//            return true;
//        }
//        String auth = request.getHeader("Authorization");
//        if (auth != null && auth.startsWith("Bearer ")) {
//            auth = auth.substring(7);
//            try {
//                String subject = jsonWebTokenUtility.validateToken(auth);
//                if (subject != null && !subject.isEmpty()) {
//                    // JWT 驗證成功
//                    return true;
//                } else {
//                    System.out.println("JWT validation failed: Subject is null or empty.");
//                }
//            } catch (Exception e) {
//                System.out.println("JWT validation exception: " + e.getMessage());
//            }
//        } else {
//            System.out.println("Authorization header is missing or invalid: " + auth);
//        }
//        // JWT 驗證失敗
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.setContentType("application/json");
//        response.getWriter().write("{\"error\":\"Invalid or missing token\"}");
//        return false;
//    }
//}