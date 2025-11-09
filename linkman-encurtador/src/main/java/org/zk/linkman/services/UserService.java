package org.zk.linkman.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.zk.linkman.constants.Rules;
import org.zk.linkman.dto.CreateUserDto;
import org.zk.linkman.dto.UpdateUserDto;
import org.zk.linkman.entities.UserEntity;
import org.zk.linkman.repositories.UserRepository;
import org.zk.linkman.tools.HashUtils;
import org.zk.linkman.tools.ValuesUtils;

import java.util.Optional;
import java.util.Set;

@ApplicationScoped

public class UserService {
    @Inject
    private UserRepository userRepository;

    public UserEntity getUser(Long id) {
        Optional<UserEntity> user = userRepository.findByIdOptional(id);
        if (user.isPresent())
            return user.get();
        throw new NotFoundException("User not found");
    }

    public UserEntity createUser(CreateUserDto dto) {

        if (emailInUse(dto.mail())) {
            throw new BadRequestException("User email already in use.");
        }
        UserEntity user = new UserEntity();
        user.setMail(dto.mail());
        user.setName(dto.name());
        user.setPassword(HashUtils.hashPassword(dto.password()));

        user.setRules(Set.of(Rules.USER));

        user.persist();

        return user;
    }

    public UserEntity updateUser(Long id, UpdateUserDto dto) {
        UserEntity user = getUser(id);

        ValuesUtils.setIf(dto.mail(), dto.mail() != null && !emailInUse(dto.mail()), user::setMail);
        ValuesUtils.setIf(dto.name(), dto.name() != null, user::setName);
        ValuesUtils.setIf(dto.password(), dto.password() != null, (password) -> {
            user.setPassword(HashUtils.hashPassword(dto.password()));
        });

        user.persist();

        return user;
    }

    public void deleteUser(Long id) {
        UserEntity userEntity = getUser(id);

        userRepository.delete(userEntity);
    }

    private boolean emailInUse(String email) {
        Optional<UserEntity> exists = userRepository.findUserByEmail(email);
        return exists.isPresent();
    }

}
