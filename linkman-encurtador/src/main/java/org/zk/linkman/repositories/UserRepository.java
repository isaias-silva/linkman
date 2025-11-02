package org.zk.linkman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.zk.linkman.entities.UserEntity;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

    public Optional<UserEntity> findUserByEmail(String mail){
        return find("mail=?1",mail).firstResultOptional();
    }
}
