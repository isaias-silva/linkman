package org.zk.linkman.encurtador.services;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.zk.linkman.encurtador.dto.LinkDto;
import org.zk.linkman.encurtador.entities.LinkEntity;
import org.zk.linkman.encurtador.tools.ValuesUtils;

@ApplicationScoped
public class CacheService {
    @Inject
    LinkService linkService;

    @CacheResult(cacheName = "users-code")
    public Integer getCode(Integer id) {

        return ValuesUtils.generateRandomCode(6);

    }

    @CacheInvalidate(cacheName = "users-code")
    public void clearCode(Integer id) {
    }

    @CacheResult(cacheName = "links")
    public LinkDto getCacheLink(String url) {
        LinkEntity linkEntity = linkService.get(url);
        if (linkEntity != null) {
            return linkEntity.dto();
        }
        throw new NotFoundException();
    }


}
