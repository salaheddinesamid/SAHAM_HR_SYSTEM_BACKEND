package com.saham.hr_system.config;

import com.saham.hr_system.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class is responsible for configuring the security settings of the application, including authentication and authorization rules.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;

    /**
     * This method configures the security filter chain for the application, defining the authentication and authorization rules for different endpoints.
     * It also sets up CORS configuration and exception handling for unauthorized access attempts.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests(auth -> auth

                        // Loan Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/loans/requests/hr/**").hasAuthority("HR")
                        .requestMatchers("/api/v1/loans/requests/subordinates/**").hasAnyAuthority("MANAGER")
                        .requestMatchers("/api/v1/loans/apply").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/loans/requests/employee/get-all").hasAuthority("EMPLOYEE")
                        // Leave Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/leaves/apply").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/leaves/requests/get").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/leaves/medical-certificate/upload").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/leaves/requests/subordinates/**").hasAuthority("MANAGER")
                        .requestMatchers("/api/v1/leaves/requests/hr/**").hasAuthority("HR")
                        .requestMatchers("/api/v1/leaves/cancel-request").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/leaves/employee-leaves/get_all").hasAnyAuthority("EMPLOYEE", "MANAGER")
                        .requestMatchers("/api/v1/leaves/cancel").hasAuthority("HR")
                        // Employee Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/employees/new").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/employees/update/**").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/employees/update/profile-picture").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/employees/get_all").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/employees/balances/get_all").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/employees/get").hasAnyAuthority("ADMIN","EMPLOYEE","HR","MANAGER")
                        .requestMatchers("/api/v1/employees/subordinates").permitAll()
                        .requestMatchers("/api/v1/employees/get_all").permitAll()
                        .requestMatchers("/api/v1/employees/new").permitAll()
                        .requestMatchers("/api/v1/employees/profile-picture").hasAuthority("EMPLOYEE")
                        // Document Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/documents/request").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/documents/requests/approve-request").hasAuthority("HR")
                        .requestMatchers("/api/v1/documents/requests/reject-request").hasAuthority("HR")
                        // Absence Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/absences/new").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/absences/requests/subordinates/**").hasAuthority("MANAGER")
                        .requestMatchers("/api/v1/absences/requests/hr/**").hasAuthority("HR")
                        .requestMatchers("/api/v1/absences/employee-absences/get_all").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/absences/requests/subordinates/get_all").hasAuthority("MANAGER")
                        .requestMatchers("/api/v1/absences/requests/hr/get_all").hasAuthority("HR")
                        // Payroll Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/payrolls/upload").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/payrolls/history/get_all").hasAnyAuthority("HR", "ADMIN")
                        .requestMatchers("/api/v1/payrolls/overview").hasAnyAuthority("EMPLOYEE", "HR", "ADMIN")
                        // Holidays Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/holidays/get_all").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/holidays/update").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/holidays/new").hasAuthority("ADMIN")
                        // Expense Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/expenses/new").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/expenses/get-all").hasAuthority("EMPLOYEE")
                        // Authentication Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/auth").permitAll() // this endpoint is open for every one to do the authentication
                        .requestMatchers("/api/v1/auth/setup-password").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/auth/forgot-password").permitAll()
                        .requestMatchers("/api/v1/auth/re-activate-account").hasAuthority("ADMIN")
                        // File Download Controller endpoints authorization configuration
                        // Analytics Controller endpoints authorization configuration
                        .requestMatchers("/api/v1/analytics/**").permitAll()

                        //Configuration of Documents Upload/Download endpoints authorization
                        .requestMatchers("/api/v1/files/download").permitAll()


                        .requestMatchers("/api/v1/holidays/**").permitAll()
                        /*
                         Analytics Configuration
                         */
                        .requestMatchers("/api/v1/analytics/**").permitAll()
                        /*
                          Configuration of Payroll Management endpoints authorization
                         */
                        .requestMatchers("/websocket").permitAll()
                        // Any other endpoint requires authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        httpSecurityExceptionHandlingConfigurer ->
                                httpSecurityExceptionHandlingConfigurer
                                        .authenticationEntryPoint((request, response, authException) ->
                                                response.sendError(
                                                        response.SC_UNAUTHORIZED,
                                                        "Unauthorized: " + authException.getMessage()
                                                )
                                        )
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }




    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:3000",
                                "http://192.168.15.27:3000",
                                "http://localhost:3001",
                                "http://192.168.15.27:3001",
                                "http://hr.saham.local:3000",
                                "http://hr.saham.local:3001"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true);
            }
        };
    }

}
