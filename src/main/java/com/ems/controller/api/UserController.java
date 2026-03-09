package com.ems.controller.api;

import com.ems.dto.UserDTO;
import com.ems.service.ContentService;
import com.ems.service.UserService;
import com.ems.util.AppUtil;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
public class UserController {

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String jsonData, @Context HttpServletRequest request){
        UserDTO userDTO = AppUtil.GSON.fromJson(jsonData, UserDTO.class);
        String responseJson = new UserService().loginUser(userDTO, request);
        return Response.ok().entity(responseJson).build();
    }

    @Path("/current")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUser(@Context HttpServletRequest request){
        String responseJson = new ContentService().getCurrentUser(request);
        return Response.ok().entity(responseJson).build();
    }

    @Path("/logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            responseObject.addProperty("status", true);
            responseObject.addProperty("message", "Logged out successfully");

        } catch (Exception e) {
            e.printStackTrace();
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Logout failed: " + e.getMessage());
        }

        return Response.ok().entity(AppUtil.GSON.toJson(responseObject)).build();
    }




}
