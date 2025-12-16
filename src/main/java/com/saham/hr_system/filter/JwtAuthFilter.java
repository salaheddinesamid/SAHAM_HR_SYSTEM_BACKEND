package com.saham.hr_system.filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.auth.service.implementation.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtilities jwtUtilities;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().contains("/upload")
                || request.getContentType() != null
                && request.getContentType().startsWith("multipart/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = extractToken(request);

            if (token != null && jwtUtilities.validateToken(token)) {
                String email = jwtUtilities.extractUserName(token);
                UserDetails user = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            log.warn("JWT expired: {}", ex.getMessage());
            filterChain.doFilter(request, response); // ⬅ IMPORTANT
        } catch (Exception ex) {
            log.error("JWT authentication failed", ex);
            filterChain.doFilter(request, response); // ⬅ IMPORTANT
        }
    }

    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    /**     * Sends an error response in JSON format.
     *
     * @param response the HTTP response
     * @param status the HTTP status
     * @param message the error message
     * @param errorCode the error code
     * @throws IOException if an I/O error occurs
     */
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message, String errorCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);
        errorDetails.put("errorCode", errorCode);

        objectMapper.writeValue(response.getOutputStream(), errorDetails);
    }

}