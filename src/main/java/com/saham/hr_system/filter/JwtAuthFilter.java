package com.saham.hr_system.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.exception.ExpiredJwtTokenException;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.auth.service.implementation.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        try{
            log.info("Incoming request to: {}", request.getRequestURI());
            log.info("Extracting the token...");
            String token = "";
            // Extract request cookies:
            if(request.getCookies() != null){
                var cookies = request.getCookies();
                for(var cookie : cookies){
                    if(cookie.getName().equals("accessToken")){
                        token = cookie.getValue(); // Assign token to a variable:
                        //System.out.println(token);
                        log.info("Token extracted from cookies: {}", token);
                    }
                }
            }
            // Extract token from Authorization header if not found in cookies:
            if(request.getHeader("Authorization") != null && request.getHeader("Authorization").startsWith("Bearer ")){
                token = request.getHeader("Authorization")
                        .substring(7);
                log.info("Token extracted from Authorization Header: {}", token);

            }

            // Validate if the token exists and valid:
            if(token != null && jwtUtilities.validateToken(token)){
                log.info("Extracting the user details from token...");
                // Fetch user details:
                String email = jwtUtilities.extractUserName(token);
                UserDetails employee = userDetailsService.loadUserByUsername(email);

                if(employee != null && employee.isAccountNonExpired() && employee.isAccountNonLocked()){
                    log.info("User details extracted successfully for email: {}", email);
                    log.info("The user has the following authorities: {}", employee.getAuthorities());
                    // Create authentication object:
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(employee, null, employee.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException ex) {
            //log.error("JWT Token expired: {}", ex.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token has expired", "JWT_EXPIRED");
        } /*catch (TokenExpiredException ex) {
            log.error("JWT Token expired: {}", ex.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, ex.getMessage(), "JWT_EXPIRED");
        } catch (UserAccountLockedException ex) {
            log.error("User account locked: {}", ex.getMessage());
            sendErrorResponse(response, HttpStatus.FORBIDDEN, ex.getMessage(), "ACCOUNT_LOCKED");
        }*/
        catch (UserNotFoundException ex) {
            log.error("User not found: {}", ex.getMessage());
            sendErrorResponse(response, HttpStatus.NOT_FOUND, "User not found", "USER_NOT_FOUND");
        } catch (ExpiredJwtTokenException ex){
        }
        catch (Exception ex) {
            log.error("Authentication error: {}", ex.getMessage());
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Authentication failed", "AUTH_ERROR");
        }
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