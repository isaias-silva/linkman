package org.zk.linkman.encurtador.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import org.zk.linkman.encurtador.entities.LinkEntity;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LinkRepository implements PanacheRepository<LinkEntity> {

    public Optional<LinkEntity> findByOriginalUrl(String url) {
        return find("originalUrl=?1", url).firstResultOptional();
    }


    public LinkEntity findByUrl(String link) {
        return find("url=?1", link).firstResult();
    }

    public List<LinkEntity> findByUser(Long creatorId, int page, int count) {

        return find("user.id", creatorId).page(Page.of(Math.max(0, page - 1), Math.max(1, count))).stream().toList();
    }
}
