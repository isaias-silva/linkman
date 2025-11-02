package org.zk.linkman.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.zk.linkman.dto.CreateUrlDto;
import org.zk.linkman.entities.LinkEntity;
import org.zk.linkman.repositories.LinkRepository;

import java.util.Optional;
import java.util.UUID;


@ApplicationScoped
public class LinkService {

    @Inject
    LinkRepository linkRepository;

    public LinkEntity create(CreateUrlDto data) {
        Optional<LinkEntity> existsEntity = linkRepository.findByOriginalUrl(data.url());
        if (existsEntity.isPresent()) {
            throw new BadRequestException("Link already saved.");
        }
        LinkEntity linkEntity = new LinkEntity();

        linkEntity.setOriginalUrl(data.url());
        linkEntity.setTitle(data.title());
        linkEntity.setLink(generateRandomValue());

        linkEntity.persist();

        return linkEntity;
    }

    public LinkEntity get(Long id) {
        LinkEntity linkEntity = linkRepository.findById(id);
        if (linkEntity == null) throw new NotFoundException("Url not found.");
        return linkEntity;
    }

    public void delete(Long id) {
        linkRepository.deleteById(id);
    }

    private String generateRandomValue() {
        return UUID.randomUUID().toString();
    }
}
