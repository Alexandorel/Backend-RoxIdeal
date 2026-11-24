package com.roxideal.backend.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Duration ANDROID_TTL = Duration.ofHours(12);
    private static final Duration WEB_TTL = Duration.ofHours(2);

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
        return new TokenResponse(service.register(request, ANDROID_TTL).token());
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return new TokenResponse(service.login(request, ANDROID_TTL).token());
    }

    @GetMapping("/csrf")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void csrf(CsrfToken token) {
        token.getToken();
    }

    @PostMapping("/web/register")
    public ResponseEntity<UserDto> webRegister(@Valid @RequestBody RegisterRequest request) {
        return withCookie(HttpStatus.CREATED, service.register(request, WEB_TTL));
    }

    @PostMapping("/web/login")
    public ResponseEntity<UserDto> webLogin(@Valid @RequestBody LoginRequest request) {
        return withCookie(HttpStatus.OK, service.login(request, WEB_TTL));
    }

    private ResponseEntity<UserDto> withCookie(HttpStatus status, AuthResult result) {
        return ResponseEntity.status(status)
                .header(HttpHeaders.SET_COOKIE, AuthCookie.create(result.token(), WEB_TTL).toString())
                .body(result.user());
    }
}
