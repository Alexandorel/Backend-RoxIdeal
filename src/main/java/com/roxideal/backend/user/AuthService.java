package com.roxideal.backend.user;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final String dummyHash;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.dummyHash = passwordEncoder.encode(UUID.randomUUID().toString());
    }

    public AuthResult register(RegisterRequest request, Duration ttl) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        User user = new User(
                request.firstName(), request.lastName(), request.email(),
                request.phone(), passwordEncoder.encode(request.password()), Role.CUSTOMER);
        return authResult(repository.save(user), ttl);
    }

    public AuthResult login(LoginRequest request, Duration ttl) {
        Optional<User> user = repository.findByEmail(request.email());
        String hash = user.map(User::getPassword).orElse(dummyHash);
        boolean passwordMatches = passwordEncoder.matches(request.password(), hash);
        if (user.isEmpty() || !passwordMatches) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return authResult(user.get(), ttl);
    }

    private AuthResult authResult(User user, Duration ttl) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("roxideal")
                .issuedAt(now)
                .expiresAt(now.plus(ttl))
                .subject(user.getEmail())
                .claim("roles", List.of(user.getRole().name()))
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new AuthResult(UserDto.from(user), token);
    }
}
