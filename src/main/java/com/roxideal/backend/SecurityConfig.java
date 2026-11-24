package com.roxideal.backend;

import com.roxideal.backend.user.AuthCookie;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.util.WebUtils;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/finishes/**").permitAll()
                        .requestMatchers("/api/products/**").hasRole("ADMIN")
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
                        .requestMatchers("/api/finishes/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth
                        .bearerTokenResolver(headerOrCookieTokenResolver())
                        .authenticationEntryPoint(clearCookieEntryPoint())
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private BearerTokenResolver headerOrCookieTokenResolver() {
        BearerTokenResolver header = new DefaultBearerTokenResolver();
        return request -> {
            String token = header.resolve(request);
            if (token != null) {
                return token;
            }
            Cookie cookie = WebUtils.getCookie(request, AuthCookie.NAME);
            return cookie == null ? null : cookie.getValue();
        };
    }

    private AuthenticationEntryPoint clearCookieEntryPoint() {
        AuthenticationEntryPoint standard = new BearerTokenAuthenticationEntryPoint();
        return (request, response, exception) -> {
            if (WebUtils.getCookie(request, AuthCookie.NAME) != null) {
                response.addHeader(HttpHeaders.SET_COOKIE, AuthCookie.clear().toString());
            }
            standard.commence(request, response, exception);
        };
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("roles");
        authorities.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }
}
