package org.zk.linkman.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.zk.linkman.entities.LinkEntity;
import org.zk.linkman.repositories.LinkRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@ApplicationScoped
@Path("/")

public class MainController {
    @Inject
    private LinkRepository linkRepository;

    @GET()
    @Path("l/{id}")
    public Response getUrl(@PathParam("id") String id) throws URISyntaxException {
        Optional<LinkEntity> link = linkRepository.findByUrl(id);
        if (link.isPresent())
            return Response.temporaryRedirect(URI.create(link.get().getOriginalUrl())).build();

        return Response.status(404).build();
    }
}
