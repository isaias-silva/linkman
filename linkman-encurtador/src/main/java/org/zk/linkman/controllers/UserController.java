package org.zk.linkman.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.zk.linkman.dto.CreateUserDto;
import org.zk.linkman.dto.LoginDto;
import org.zk.linkman.dto.UpdateUserDto;
import org.zk.linkman.entities.UserEntity;
import org.zk.linkman.services.AuthService;
import org.zk.linkman.services.UserService;

import java.util.Map;

@ApplicationScoped
@Path("/user")
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private AuthService authService;


    @POST
    @Transactional
    public UserEntity register(@Valid CreateUserDto dto) {

        return userService.createUser(dto);
    }

    @POST()
    @Path("auth")
    public Response login(@Valid LoginDto dto) {
        String jwt = authService.login(dto);
        return Response.status(200).entity(Map.of("token", jwt, "message", "login successful.")).build();
    }
}
