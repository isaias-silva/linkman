package org.zk.linkman.services;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.zk.linkman.dto.LoginDto;
import org.zk.linkman.entities.UserEntity;
import org.zk.linkman.repositories.UserRepository;
import org.zk.linkman.tools.HashUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;

@ApplicationScoped
public class AuthService {
    @Inject
    private UserRepository userRepository;

    public String login(LoginDto dto) {

        Optional<UserEntity> userEntity = userRepository.findUserByEmail(dto.mail());
        if (userEntity.isEmpty()) {
            throw new NotFoundException("Invalid credentials.");
        }
        UserEntity user = userEntity.get();

        if(!HashUtils.checkPassword(dto.password(),user.getPassword())){
            throw new UnauthorizedException("Invalid credentials.");
        }
        return generateToken(user);

    }

    public String generateToken(UserEntity user) {
        return Jwt.issuer("http://auth-linkman").subject(user.getId().toString())
                .groups(new HashSet<>(user.getRules())).expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .sign();
    }
}
