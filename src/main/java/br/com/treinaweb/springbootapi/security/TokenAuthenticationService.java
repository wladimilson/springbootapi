package br.com.treinaweb.springbootapi.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/// Serviço responsável pela criação e assinatura de tokens JWT.
/// Utiliza as chaves RSA configuradas na aplicação para emitir tokens seguros e assinados.
@Service
public class TokenAuthenticationService {
    public static final int JWT_TOKEN_VALIDITY_HOURS = 2;

    private final JwtEncoder jwtEncoder;

    public TokenAuthenticationService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /// Gera um token JWT para um usuário autenticado.
    /// O token inclui o nome do usuário como `sub`, suas autoridades no `scope`
    /// e uma data de validade configurada por `JWT_TOKEN_VALIDITY_HOURS`.
    ///
    /// @param authentication Informações do usuário autenticado no contexto de segurança.
    /// @return Uma string contendo o token JWT codificado e assinado.
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(JWT_TOKEN_VALIDITY_HOURS, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}