package org.zk.linkman.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.zk.linkman.constants.Rules;
import org.zk.linkman.dto.CreateUrlDto;
import org.zk.linkman.dto.LinkDto;
import org.zk.linkman.entities.LinkEntity;
import org.zk.linkman.services.LinkService;

import java.util.Map;

@ApplicationScoped
@Path("/link")
@RolesAllowed(Rules.USER)
public class LinkController {

    @Inject
    private LinkService linkService;

    @Inject
    private JsonWebToken jwt;

    @GET()
    @RolesAllowed(Rules.ADMIN)
    public LinkDto getLink(@QueryParam("id") Long id) {
        return linkService.get(id).dto();
    }

    @GET()
    @Path("my")
    public LinkDto getMyLink(@QueryParam("id") Long id) {
        return linkService.get(getCreatorId(), id).dto();
    }

    @POST()
    @Transactional()
    public Response createLink(@Valid CreateUrlDto body) {
        LinkDto link = linkService.create(getCreatorId(), body).dto();
        return Response.status(201).entity(Map.of("link", link, "message", "link created")).build();
    }

    @DELETE()
    @Transactional()
    @RolesAllowed(Rules.ADMIN)
    public Response deleteLink(@QueryParam("id") Long id) {

        linkService.delete(id);
        return Response.status(200).entity(Map.of("message", "link deleted")).build();

    }

    @DELETE()
    @Transactional()
    @Path("my")
    public Response deleteMyLink(@QueryParam("id") Long id) {

        linkService.delete(getCreatorId(), id);
        return Response.status(200).entity(Map.of("message", "link deleted")).build();


    }

    private long getCreatorId() {
        return Long.parseLong(jwt.getSubject());
    }

}
