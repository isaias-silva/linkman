package org.zk.linkman.encurtador.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.zk.linkman.commons.constants.Rules;
import org.zk.linkman.encurtador.dto.*;
import org.zk.linkman.encurtador.entities.UserEntity;
import org.zk.linkman.encurtador.services.AuthService;
import org.zk.linkman.encurtador.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
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

        return Response.status(201).entity(Map.of("token", token, "message", "account created", "data", user.dto())).build();
    }

    @POST()
    @Path("auth")
    public Response login(@Valid LoginDto dto) {
        String token = authService.login(dto);
        return Response.status(200).entity(Map.of("token", token, "message", "login successful.")).build();
    }

    @POST()
    @RolesAllowed(Rules.UNVERIFIED)
    @Transactional
    @Path("validate")
    public Response validate(@QueryParam("code") Integer code) {

        userService.validateUser(getId(), code);
        return Response.status(200).entity(Map.of("message", "user validated.")).build();
    }

    private long getId() {
        return Long.parseLong(jwt.getSubject());
    }

    @GET()
    @RolesAllowed({Rules.USER, Rules.UNVERIFIED})
    @Path("me")
    public UserDto get() {

        return userService.getUser(getId()).dto();
    }

    @GET()
    @RolesAllowed({Rules.USER})
    @Path("profile")
    public Response getProfile() {

        String url = userService.getProfile(getId());

        return Response.status(200).entity(Map.of("url", url)).build();
    }

    @PUT()
    @RolesAllowed({Rules.USER})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("profile")
    public Response createFile(@RestForm("file") FileUpload file) throws IOException {
        userService.uploadProfile(getId(), Files.readAllBytes(file.filePath()));
       return Response.status(201).entity(Map.of("message","updated profile")).build();
    }


    @PUT()
    @Path("update")
    @RolesAllowed(Rules.USER)
    public Response update(@Valid UpdateUserDto dto) throws Exception {
        Long id = getId();
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