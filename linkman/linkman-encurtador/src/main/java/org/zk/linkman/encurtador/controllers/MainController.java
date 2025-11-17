package org.zk.linkman.encurtador.controllers;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.zk.linkman.encurtador.entities.LinkEntity;
import org.zk.linkman.encurtador.repositories.LinkRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static java.lang.System.getenv;

@ApplicationScoped
@Path("/")

public class MainController {
    @Inject
    private LinkRepository linkRepository;

    @GET()
    @CacheResult(cacheName = "links")
    @Path("l/{id}")

    public Response getUrl(@PathParam("id") String id) throws URISyntaxException {
        Optional<LinkEntity> link = linkRepository.findByUrl(id);
        if (link.isPresent()) {

            return Response.temporaryRedirect(URI.create(link.get().getOriginalUrl())).build();

        }
        return Response.status(404).build();
    }
}
