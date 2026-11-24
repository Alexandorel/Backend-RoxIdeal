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

    public TokenResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        User user = new User(
                request.firstName(), request.lastName(), request.email(),
                request.phone(), passwordEncoder.encode(request.password()), Role.CUSTOMER);
        return issueToken(repository.save(user));
    }

    public TokenResponse login(LoginRequest request) {
        Optional<User> user = repository.findByEmail(request.email());
        String hash = user.map(User::getPassword).orElse(dummyHash);
        boolean passwordMatches = passwordEncoder.matches(request.password(), hash);
        if (user.isEmpty() || !passwordMatches) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return issueToken(user.get());
    }

    private TokenResponse issueToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("roxideal")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofHours(12)))
                .subject(user.getEmail())
                .claim("roles", List.of(user.getRole().name()))
                .build();
        return new TokenResponse(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }
}
