package br.com.treinaweb.springbootapi.security;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
public class AdminSecurityConfig {

    private final AdminServerProperties adminServer;

    public AdminSecurityConfig(AdminServerProperties adminServer) {
        this.adminServer = adminServer;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

        http
            .securityMatcher(this.adminServer.path("/**"), "/actuator/**")
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(this.adminServer.path("/assets/**")).permitAll()
                .requestMatchers(this.adminServer.path("/login")).permitAll()
                .requestMatchers(this.adminServer.path("/instances/**")).permitAll()
                .requestMatchers("/actuator/health", "/actuator/info", "/actuator/sbom", "/actuator/sbom/**").permitAll()
                .requestMatchers("/actuator/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage(this.adminServer.path("/login"))
                .successHandler(successHandler)
            )
            .logout(logout -> logout.logoutUrl(this.adminServer.path("/logout")))
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
