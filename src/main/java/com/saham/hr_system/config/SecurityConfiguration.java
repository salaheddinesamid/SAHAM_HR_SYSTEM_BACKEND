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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;

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
                        .requestMatchers("/api/v1/auth/**").permitAll() // this endpoint is open for every one to do the authentication
                        .requestMatchers("/api/v1/employees/**").hasAnyAuthority("ADMIN","EMPLOYEE","HR","MANAGER")
                        .requestMatchers("/api/v1/leaves/apply").hasAnyAuthority("EMPLOYEE","MANAGER","HR")
                        .requestMatchers("/api/v1/leaves/requests/subordinates/**").hasAuthority("MANAGER") // The manager of a team is the only one who can see the leave requests of his subordinates, approved and reject.
                        .requestMatchers("/api/v1/leaves/requests/hr/**").hasAuthority("HR")
                        .requestMatchers("/api/v1/leaves/**").hasAnyAuthority("HR","MANAGER","EMPLOYEE")
                        .requestMatchers("/api/v1/absences/**").hasAnyAuthority("MANAGER","EMPLOYEE")
                        // Any other endpoint requires authentication
                        .anyRequest().authenticated()
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
                                "http://YOUR_SERVER_IP",
                                "http://YOUR_SERVER_IP:YOUR_FRONTEND_PORT",
                                "https://YOUR_DOMAIN"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
