package org.zk.linkman.encurtador.services;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.zk.linkman.encurtador.dto.CreateUrlDto;
import org.zk.linkman.encurtador.entities.LinkEntity;
import org.zk.linkman.encurtador.entities.UserEntity;
import org.zk.linkman.encurtador.repositories.LinkRepository;
import org.zk.linkman.encurtador.tools.ValuesUtils;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LinkService {

    @Inject
    LinkRepository linkRepository;

    @Inject
    UserService userService;

    public LinkEntity create(Long creatorId, CreateUrlDto data) {

        UserEntity creator = userService.getUser(creatorId);

        if (creator.getLinks().stream().anyMatch(l -> l.getTitle().equals(data.title()) || l.getOriginalUrl().equals(data.url()))) {
            throw new BadRequestException("Link already save.");
        }

        LinkEntity linkEntity = new LinkEntity();
        linkEntity.setOriginalUrl(data.url());
        linkEntity.setTitle(data.title());
        linkEntity.setUrl(generateRandomValue());

        creator.getLinks().add(linkEntity);
        linkEntity.setUser(creator);

        creator.persist();

        return linkEntity;
    }

    public List<LinkEntity> getLinksByCreator(Long creator, int page, int count) {
        return linkRepository.findByUser(creator, page, count);
    }

    public LinkEntity get(Long id) {
        LinkEntity linkEntity = linkRepository.findById(id);
        if (linkEntity == null) throw new NotFoundException("Url not found.");
        return linkEntity;
    }

    public LinkEntity get(String url){
        return linkRepository.findByUrl(url);
    }

    public LinkEntity get(Long creatorId, Long id) {
        UserEntity creator = userService.getUser(creatorId);
        Optional<LinkEntity> linkEntity = creator.getLinks().stream().filter(l -> l.getId().equals(id)).findFirst();
        if (linkEntity.isEmpty()) throw new NotFoundException("Url not found.");

        return linkEntity.get();
    }

    public void delete(Long id) {
        linkRepository.deleteById(id);
    }

    public void delete(Long creatorId, Long id) {
        UserEntity creator = userService.getUser(creatorId);

        creator.getLinks().removeIf(l -> l.getId().equals(id));

        creator.persist();
    }

    private String generateRandomValue() {

        return ValuesUtils.generateRandomWord(9);
    }
}
