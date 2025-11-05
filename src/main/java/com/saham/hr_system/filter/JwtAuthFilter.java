package com.saham.hr_system.filter;

import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.service.implementation.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtilities jwtUtilities;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = "";
        // Extract request cookies:
        if(request.getCookies() != null){
            var cookies = request.getCookies();
            for(var cookie : cookies){
                if(cookie.getName().equals("jwt_token")){
                    token = cookie.getValue(); // Assign token to a variable:
                }
            }
        }

        // Validate if the token exists and valid:
        if(token != null && jwtUtilities.validateToken(token)){
            // Fetch user details:
            String email = jwtUtilities.extractUserName(token);
            UserDetails employee = userDetailsService.loadUserByUsername(email);

            if(employee != null && employee.isAccountNonExpired() && employee.isAccountNonLocked()){
                // Create authentication object:
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(employee, null, employee.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
