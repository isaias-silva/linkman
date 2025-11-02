package org.zk.linkman.repositories;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.zk.linkman.entities.LinkEntity;

import java.util.Optional;

@ApplicationScoped
public class LinkRepository implements PanacheRepository<LinkEntity> {

    public Optional<LinkEntity> findByOriginalUrl(String url) {
        return find("originalUrl=?1", url).firstResultOptional();
    }
    public Optional<LinkEntity> findByLink(String link){
        return find("link=?1", link).firstResultOptional();
    }
}
