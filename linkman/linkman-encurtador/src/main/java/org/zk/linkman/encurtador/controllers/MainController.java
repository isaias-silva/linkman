package org.zk.linkman.encurtador.controllers;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.zk.linkman.encurtador.dto.LinkDto;
import org.zk.linkman.encurtador.entities.LinkEntity;
import org.zk.linkman.encurtador.repositories.LinkRepository;
import org.zk.linkman.encurtador.services.CacheService;
import org.zk.linkman.encurtador.services.LinkService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static java.lang.System.getenv;

@ApplicationScoped
@Path("/")

public class MainController {
    @Inject
    private CacheService cacheService;

    @GET()
    @Path("l/{url}")

    public Response getUrl(@PathParam("url") String url) throws URISyntaxException {
        LinkDto link = cacheService.getCacheLink(url);
        return Response.temporaryRedirect(URI.create(link.originalUrl())).build();
    }
}
