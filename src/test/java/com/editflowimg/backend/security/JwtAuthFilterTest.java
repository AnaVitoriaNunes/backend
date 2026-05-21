package com.editflowimg.backend.security;

import com.editflowimg.backend.entity.UserEntity;
import com.editflowimg.backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveContinuarFiltroQuandoNaoExisteHeaderAuthorization() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveContinuarQuandoTokenForInvalido() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.isValid("token")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveAutenticarUsuarioQuandoTokenForValido() throws Exception {

        UUID userId = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(userId);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.isValid("token")).thenReturn(true);
        when(jwtUtil.extractUserId("token")).thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);

    }
}