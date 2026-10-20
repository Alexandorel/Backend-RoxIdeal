package com.roxideal.backend.user;

import com.roxideal.backend.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.security.KeyPair;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository repository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    JwtDecoder jwtDecoder;
    AuthService service;

    @BeforeEach
    void setUp() throws Exception {
        JwtConfig config = new JwtConfig();
        KeyPair keyPair = config.rsaKeyPair();
        jwtDecoder = config.jwtDecoder(keyPair);
        service = new AuthService(repository, passwordEncoder, config.jwtEncoder(keyPair));
    }

    @Test
    void register_createsCustomerAndIssuesToken() {
        when(repository.existsByEmail("ana@mail.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        TokenResponse response = service.register(
                new RegisterRequest("Ana", "Pop", "ana@mail.com", "0700", "parola123"));

        Jwt jwt = jwtDecoder.decode(response.token());
        assertEquals("ana@mail.com", jwt.getSubject());
        assertEquals(List.of("CUSTOMER"), jwt.getClaimAsStringList("roles"));
    }

    @Test
    void register_duplicateEmail_throws() {
        when(repository.existsByEmail("ana@mail.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> service.register(
                new RegisterRequest("Ana", "Pop", "ana@mail.com", "0700", "parola123")));
    }

    @Test
    void login_wrongPassword_throws() {
        User user = new User("Ana", "Pop", "ana@mail.com", "0700",
                passwordEncoder.encode("parola123"), Role.CUSTOMER);
        when(repository.findByEmail("ana@mail.com")).thenReturn(Optional.of(user));

        assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginRequest("ana@mail.com", "gresit")));
    }

    @Test
    void login_correctPassword_issuesTokenWithRole() {
        User admin = new User("Admin", "RoxIdeal", "admin@mail.com", "-",
                passwordEncoder.encode("parola123"), Role.ADMIN);
        when(repository.findByEmail("admin@mail.com")).thenReturn(Optional.of(admin));

        TokenResponse response = service.login(new LoginRequest("admin@mail.com", "parola123"));

        Jwt jwt = jwtDecoder.decode(response.token());
        assertEquals("admin@mail.com", jwt.getSubject());
        assertEquals(List.of("ADMIN"), jwt.getClaimAsStringList("roles"));
    }
}
