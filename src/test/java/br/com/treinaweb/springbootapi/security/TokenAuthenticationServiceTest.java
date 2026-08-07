package br.com.treinaweb.springbootapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TokenAuthenticationServiceTest {

    private TokenAuthenticationService tokenAuthenticationService;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenAuthenticationService = new TokenAuthenticationService(jwtEncoder);
    }

    @Test
    void testGenerateToken() {
        when(authentication.getName()).thenReturn("testuser");
        GrantedAuthority authority = () -> "ROLE_USER";
        // Use of raw type in Mockito when mocking generic types can be tricky, but here it's okay for authorities.
        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of(authority));
        
        when(jwt.getTokenValue()).thenReturn("mocked-token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = tokenAuthenticationService.generateToken(authentication);

        assertEquals("mocked-token", token);
    }
}
