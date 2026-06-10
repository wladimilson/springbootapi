package br.com.treinaweb.springbootapi.controller;

import java.util.Map;

import br.com.treinaweb.springbootapi.security.TokenAuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/// Controlador responsável pela autenticação de usuários na API.
/// Gerencia o processo de login e emissão de tokens JWT para acesso a rotas protegidas.
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenAuthenticationService tokenAuthenticationService;

    public AuthController(AuthenticationManager authenticationManager, TokenAuthenticationService tokenAuthenticationService) {
        this.authenticationManager = authenticationManager;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    /// Autentica um usuário e retorna um token JWT.
    /// @param loginRequest Objeto contendo o `username` e `password` do usuário.
    /// @return Um mapa contendo o token de acesso sob a chave "token".
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenAuthenticationService.generateToken(authentication);
        return ResponseEntity.ok(Map.of("token", token));
    }
}

