package org.zk.linkman.encurtador.providers;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class ErrorMapper implements ExceptionMapper<ClientErrorException> {

    @Override
    public Response toResponse(final ClientErrorException e) {

        int status = e.getResponse().getStatus();

        return Response.status(status).entity(Map.of("status",status,"message",e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
}
