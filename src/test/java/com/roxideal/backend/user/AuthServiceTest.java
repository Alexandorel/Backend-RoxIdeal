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
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    static final Duration TTL = Duration.ofHours(12);

    @Mock
    UserRepository repository;

    PasswordEncoder passwordEncoder = spy(new BCryptPasswordEncoder());
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

        AuthResult response = service.register(
                new RegisterRequest("Ana", "Pop", "ana@mail.com", "0700", "parola123"), TTL);

        Jwt jwt = jwtDecoder.decode(response.token());
        assertEquals("ana@mail.com", jwt.getSubject());
        assertEquals(List.of("CUSTOMER"), jwt.getClaimAsStringList("roles"));
    }

    @Test
    void register_duplicateEmail_throws() {
        when(repository.existsByEmail("ana@mail.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> service.register(
                new RegisterRequest("Ana", "Pop", "ana@mail.com", "0700", "parola123"), TTL));
    }

    @Test
    void register_normalizesEmail() {
        when(repository.existsByEmail("ana@mail.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthResult response = service.register(
                new RegisterRequest("Ana", "Pop", "  Ana@Mail.COM ", "0700", "parola123"), TTL);

        assertEquals("ana@mail.com", jwtDecoder.decode(response.token()).getSubject());
    }

    @Test
    void login_emailIsCaseInsensitive() {
        User user = new User("Ana", "Pop", "ana@mail.com", "0700",
                passwordEncoder.encode("parola123"), Role.CUSTOMER);
        when(repository.findByEmail("ana@mail.com")).thenReturn(Optional.of(user));

        AuthResult response = service.login(new LoginRequest(" ANA@mail.com", "parola123"), TTL);

        assertEquals("ana@mail.com", jwtDecoder.decode(response.token()).getSubject());
    }

    @Test
    void login_wrongPassword_throws() {
        User user = new User("Ana", "Pop", "ana@mail.com", "0700",
                passwordEncoder.encode("parola123"), Role.CUSTOMER);
        when(repository.findByEmail("ana@mail.com")).thenReturn(Optional.of(user));

        assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginRequest("ana@mail.com", "gresit"), TTL));
    }

    @Test
    void login_unknownEmail_stillChecksPasswordWithBcrypt() {
        when(repository.findByEmail("nimeni@mail.com")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginRequest("nimeni@mail.com", "parola123"), TTL));
        verify(passwordEncoder).matches(eq("parola123"), anyString());
    }

    @Test
    void login_correctPassword_issuesTokenWithRole() {
        User admin = new User("Admin", "RoxIdeal", "admin@mail.com", "-",
                passwordEncoder.encode("parola123"), Role.ADMIN);
        when(repository.findByEmail("admin@mail.com")).thenReturn(Optional.of(admin));

        AuthResult response = service.login(new LoginRequest("admin@mail.com", "parola123"), TTL);

        Jwt jwt = jwtDecoder.decode(response.token());
        assertEquals("admin@mail.com", jwt.getSubject());
        assertEquals(List.of("ADMIN"), jwt.getClaimAsStringList("roles"));
        assertEquals(Role.ADMIN, response.user().role());
    }

    @Test
    void login_tokenExpiresAfterRequestedTtl() {
        User user = new User("Ana", "Pop", "ana@mail.com", "0700",
                passwordEncoder.encode("parola123"), Role.CUSTOMER);
        when(repository.findByEmail("ana@mail.com")).thenReturn(Optional.of(user));

        AuthResult response = service.login(new LoginRequest("ana@mail.com", "parola123"), Duration.ofHours(2));

        Jwt jwt = jwtDecoder.decode(response.token());
        assertEquals(Duration.ofHours(2), Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()));
    }
}
