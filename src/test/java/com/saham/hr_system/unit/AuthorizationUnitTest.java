package com.saham.hr_system.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.filter.JwtAuthFilter;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.auth.service.implementation.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    private JwtUtilities jwtUtilities;
    private UserDetailsServiceImpl userDetailsService;
    private JwtAuthFilter jwtAuthFilter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jwtUtilities = mock(JwtUtilities.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);
        jwtAuthFilter = new JwtAuthFilter(jwtUtilities, userDetailsService, objectMapper);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateUserWhenValidTokenProvided() throws Exception {
        // GIVEN a mock request with Authorization header
        String token = "valid.jwt.token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // Mock JWT utils behavior
        when(jwtUtilities.validateToken(token)).thenReturn(true);
        when(jwtUtilities.extractUserName(token)).thenReturn("manager@saham.com");

        // Mock a user from user details service
        UserDetails userDetails = new User(
                "manager@saham.com",
                "password",
                List.of(() -> "MANAGER")
        );

        // loads the user details
        when(userDetailsService.loadUserByUsername("manager@saham.com"))
                .thenReturn(userDetails);

        // WHEN
        jwtAuthFilter.doFilter(request, response, filterChain);

        // THEN, VERIFY authentication is set in security context
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken)
                        SecurityContextHolder.getContext().getAuthentication();

        assertEquals("manager@saham.com", auth.getName());
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("MANAGER")));

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {
        String token = "invalid.token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtUtilities.validateToken(token)).thenReturn(false);

        jwtAuthFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldExtractTokenFromCookies() throws Exception {
        String token = "cookie.jwt.token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new jakarta.servlet.http.Cookie("accessToken", token));

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtUtilities.validateToken(token)).thenReturn(true);
        when(jwtUtilities.extractUserName(token)).thenReturn("manager@saham.com");

        UserDetails userDetails = new User(
                "manager@saham.com",
                "password",
                List.of(() -> "MANAGER")
        );

        when(userDetailsService.loadUserByUsername("manager@saham.com"))
                .thenReturn(userDetails);

        jwtAuthFilter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
