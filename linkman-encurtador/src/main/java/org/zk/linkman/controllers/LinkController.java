package org.zk.linkman.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import org.zk.linkman.constants.Rules;
import org.zk.linkman.dto.CreateUrlDto;
import org.zk.linkman.entities.LinkEntity;
import org.zk.linkman.services.LinkService;

@ApplicationScoped
@Path("/link")
@RolesAllowed(Rules.USER)
public class LinkController {

    @Inject
    private LinkService linkService;

    @GET()
    public LinkEntity getLink(@QueryParam("id") Long id) {
        return linkService.get(id);
    }

    @POST()
    @Transactional()
    public LinkEntity createLink(@Valid CreateUrlDto body) {
        return linkService.create(body);
    }

    @DELETE()
    @Transactional()
    public void deleteLink(@QueryParam("id") Long id) {
        linkService.delete(id);
    }

}
