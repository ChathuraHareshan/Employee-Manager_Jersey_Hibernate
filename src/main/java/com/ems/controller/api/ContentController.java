package com.ems.controller.api;

import com.ems.annotation.IsAdmin;
import com.ems.service.ContentService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/content")
public class ContentController {

    @Path("/loadDepartments")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadAllDepartments(){
        String responseJson = ContentService.getAllDepartment();
        return Response.ok().entity(responseJson).build();
    }

    @IsAdmin
    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getEmployeeById(@PathParam("id") int id){
        String responseJson = ContentService.getEmployeeById(id);
        return Response.ok().entity(responseJson).build();
    }

}
