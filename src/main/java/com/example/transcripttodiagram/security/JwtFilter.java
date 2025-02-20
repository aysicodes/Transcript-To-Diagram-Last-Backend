package com.example.transcripttodiagram.security;


import com.example.transcripttodiagram.model.Student;
import com.example.transcripttodiagram.service.AuthService;
import com.example.transcripttodiagram.service.StudentService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final StudentService studentService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {


        String path = request.getServletPath();
        if (path.equals("/api/register") || path.equals("/api/login")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = null;
        System.out.println("Auth header: " + authHeader);

        String email = null;
        if (email != null) {
            System.out.println("Authenticating user: " + email);
            Student student = studentService.findByEmail(email);
            System.out.println("Last login: " + student.getLastLogin());
        }
        authHeader = request.getHeader("Authorization");
        String token = null;
        email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email = jwtUtil.getEmailFromToken(token);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = authService.loadUserByUsername(email);

            if (userDetails == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid user");
                return;
            }

            Student student = studentService.findByEmail(email);
            if (student.getLastLogin().isBefore(LocalDateTime.now().minusMonths(3))) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Account inactive");
                return;
            }

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.warn("Invalid JWT token for user: " + email);
            }
        }

        chain.doFilter(request, response);
    }
}


//import com.example.transcripttodiagram.service.AuthService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final AuthService authService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String email = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//            email = jwtUtil.getEmailFromToken(token);
//        }
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = authService.loadUserByUsername(email);
//
//            if (jwtUtil.validateToken(token, userDetails)) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } else {
//                logger.warn("Invalid JWT token for user: " + email);
//            }
//        } else {
//            logger.warn("JWT token is missing or invalid.");
//        }
//        chain.doFilter(request, response);
//    }
//}



//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//        System.out.println("Authorization Header: " + authHeader); // 🔥 Проверяем заголовок
//
//        String token = null;
//        String email = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//            System.out.println("Extracted Token: " + token); // 🔥 Проверяем токен
//
//            email = jwtUtil.getEmailFromToken(token);
//            System.out.println("Extracted Email: " + email); // 🔥 Проверяем email
//        }
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = authService.loadUserByUsername(email);
//            System.out.println("UserDetails found: " + userDetails.getUsername()); // 🔥 Проверяем пользователя
//
//            if (jwtUtil.validateToken(token, userDetails)) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                System.out.println("JWT validated, user authenticated! ✅"); // ✅ Токен валиден
//            } else {
//                System.out.println("Invalid JWT token for user: " + email); // ❌ Ошибка валидации токена
//            }
//        } else {
//            System.out.println("JWT token is missing or invalid."); // ❌ Токен отсутствует
//        }
//        chain.doFilter(request, response);
//    }
//
//}