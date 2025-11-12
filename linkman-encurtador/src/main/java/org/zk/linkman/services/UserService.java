package org.zk.linkman.services;

import static java.lang.System.getenv;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.zk.linkman.constants.QueueActions;
import org.zk.linkman.constants.Rules;
import org.zk.linkman.dto.CreateUserDto;
import org.zk.linkman.dto.QueueMessage;
import org.zk.linkman.dto.UpdateUserDto;
import org.zk.linkman.entities.UserEntity;
import org.zk.linkman.repositories.UserRepository;
import org.zk.linkman.services.infra.QueueService;
import org.zk.linkman.tools.HashUtils;
import org.zk.linkman.tools.ValuesUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped

public class UserService {
    @Inject
    private UserRepository userRepository;
    @Inject
    private QueueService queueService;

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

        user.setRules(Set.of(Rules.USER));
        user.setLinks(List.of());

        queueService.send(getenv("MAIL_QUEUE"), String.format("user-%s", user.getId()),
                new QueueMessage<>(QueueActions.CREATE_ACCOUNT, user.dto()));

        user.persist();

        return user;
    }


    public UserEntity updateUser(Long id, UpdateUserDto dto) throws Exception {
        UserEntity user = getUser(id);
        if(isEqualsUpdates(dto, user)){
            throw new BadRequestException("no changes to data");
        }

        ValuesUtils.setIf(dto.name(), dto.name() != null, user::setName);
        ValuesUtils.setIf(dto.password(), dto.password() != null, password ->
            user.setPassword(HashUtils.hashPassword(dto.password())));

        if (dto.mail() != null && !dto.mail().equals(user.getMail())) {
            queueService.send(getenv("MAIL_QUEUE"),  String.format("user-%s", user.getId()), new QueueMessage<>(QueueActions.EMAIL_CHANGE, user.dto()));
            ValuesUtils.setIf(dto.name(), dto.name() != null, user::setName);
        }

        user.persist();

        queueService.send(getenv("MAIL_QUEUE"), String.format("user-%s", user.getId()), new QueueMessage<>(QueueActions.UPDATE_ACCOUNT, user.dto()));

        return user;
    }


    public void deleteUser(Long id) {
        UserEntity userEntity = getUser(id);

        userRepository.delete(userEntity);
    }

    private boolean isEqualsUpdates(final UpdateUserDto dto, final UserEntity user) {
        return user.getMail().equals(dto.mail()) && user.getName().equals(dto.name()) && HashUtils.checkPassword(dto.password(),
                user.getPassword());
    }

    private boolean emailInUse(String email) {
        Optional<UserEntity> exists = userRepository.findUserByEmail(email);
        return exists.isPresent();
    }

}