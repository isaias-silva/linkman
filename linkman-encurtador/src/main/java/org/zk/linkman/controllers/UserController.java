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
import org.zk.linkman.dto.CreateUserDto;
import org.zk.linkman.dto.LoginDto;
import org.zk.linkman.dto.UpdateUserDto;
import org.zk.linkman.dto.UserDto;
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

    @Inject
    private JsonWebToken jwt;

    @POST
    @Transactional
    public Response register(@Valid CreateUserDto dto) throws Exception {
        UserEntity user = userService.createUser(dto);

        String token = authService.generateToken(user);

        return Response.status(201).entity(Map.of("token",token,"message","account created","data",user.dto())).build();
    }

    @POST()
    @Path("auth")
    public Response login(@Valid LoginDto dto) {
        String token = authService.login(dto);
        return Response.status(200).entity(Map.of("token", token, "message", "login successful.")).build();
    }

    @GET()
    @RolesAllowed(Rules.USER)
    @Path("me")
    public UserDto get() {
        Long id = Long.parseLong(jwt.getSubject());
        return userService.getUser(id).dto();
    }

    @PUT()
    @Path("update")
    @RolesAllowed(Rules.USER)
    public Response update(@Valid UpdateUserDto dto) throws Exception {
        Long id = Long.parseLong(jwt.getSubject());
        UserEntity updated = userService.updateUser(id, dto);
        return Response.status(200).entity(Map.of("message", "user updated", "data", updated)).build();
    }

    @DELETE()
    @RolesAllowed(Rules.ADMIN)
    public Response delete(@QueryParam("id") Long id) {
        userService.deleteUser(id);
        return Response.status(200).entity(Map.of("message", "user deleted")).build();
    }

}