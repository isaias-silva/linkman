package org.zk.linkman.encurtador.services;

import static java.lang.System.getenv;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.zk.linkman.encurtador.constants.QueueActions;
import org.zk.linkman.encurtador.constants.Rules;
import org.zk.linkman.encurtador.dto.CreateUserDto;
import org.zk.linkman.encurtador.dto.UpdateUserDto;
import org.zk.linkman.encurtador.dto.ValidateCodeDto;
import org.zk.linkman.encurtador.entities.UserEntity;
import org.zk.linkman.encurtador.repositories.UserRepository;
import org.zk.linkman.encurtador.tools.HashUtils;
import org.zk.linkman.encurtador.tools.ValuesUtils;
import org.zk.linkman.commons.services.QueueService;
import org.zk.linkman.commons.dto.QueueMessage;

@ApplicationScoped

public class UserService {
    @Inject
    private UserRepository userRepository;
    @Inject
    private QueueService queueService;
    @Inject
    private CacheService cacheService;

    public UserEntity getUser(Long id) {
        Optional<UserEntity> user = userRepository.findByIdOptional(id);
        if (user.isPresent())
            return user.get();
        throw new NotFoundException("User not found");
    }


    public UserEntity createUser(CreateUserDto dto) throws Exception {

        if (emailInUse(dto.mail())) {
            throw new BadRequestException("User email already in use.");
        }
        UserEntity user = new UserEntity();
        user.setMail(dto.mail());
        user.setName(dto.name());
        user.setPassword(HashUtils.hashPassword(dto.password()));

        user.setRules(Set.of(Rules.UNVERIFIED));
        user.setLinks(List.of());


        user.persist();

        Integer code = cacheService.getCode(user.getId().intValue());

        ValidateCodeDto validateCodeDto = new ValidateCodeDto(code, user.getMail());

        queueService.send(getenv("MAIL_QUEUE"), String.format("user-%s", user.getId()),
                new QueueMessage<>(QueueActions.CREATE_ACCOUNT, validateCodeDto));

        return user;
    }


    public UserEntity updateUser(Long id, UpdateUserDto dto) throws Exception {
        UserEntity user = getUser(id);


        ValuesUtils.setIf(dto.name(), dto.name() != null, user::setName);
        ValuesUtils.setIf(dto.password(), dto.password() != null, password ->
                user.setPassword(HashUtils.hashPassword(dto.password())));


        if (dto.mail() != null && !dto.mail().equals(user.getMail())) {

            user.setMail(dto.mail());
            user.setRules(Set.of(Rules.UNVERIFIED));

            Integer code = cacheService.getCode(user.getId().intValue());

            ValidateCodeDto validateCodeDto = new ValidateCodeDto(code, user.getMail());

            queueService.send(getenv("MAIL_QUEUE"),
                    String.format("user-%s", user.getId()),
                    new QueueMessage<>(QueueActions.EMAIL_VALIDATE, validateCodeDto));

        }

        user.persist();
        return user;
    }


    public void validateUser(Long id, Integer code) {
        UserEntity user = getUser(id);

        if (!user.getRules().contains(Rules.UNVERIFIED)) {
            throw new BadRequestException("user already verified.");
        }
        Integer realCode = cacheService.getCode(id.intValue());

        if (!Objects.equals(code, realCode)) {
            throw new BadRequestException("code invalid");
        }

        user.getRules().remove(Rules.UNVERIFIED);
        user.persist();
        cacheService.clearCode(id.intValue());

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