package com.ems.controller.api;

import com.ems.annotation.IsAdmin;
import com.ems.dto.EmployeeDTO;
import com.ems.service.*;
import com.ems.util.AppUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/employee")
public class EmployeeController {

    @Path("/loadEmployee")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadAllEmployee(){
        String responseJson = new LoadEmployeeService().loadAllEmployee();
        return Response.ok().entity(responseJson).build();
    }

    @IsAdmin
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewEmployee(String jsonData){
        EmployeeDTO employeeDTO = AppUtil.GSON.fromJson(jsonData, EmployeeDTO.class);
        String responseJson = new AddEmployeeService().addNewEmployee(employeeDTO);
        return Response.ok().entity(responseJson).build();
    }


    @IsAdmin
    @Path("/edit")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response EditEmployee(String jsonData){
        EmployeeDTO employeeDTO = AppUtil.GSON.fromJson(jsonData, EmployeeDTO.class);
        String responseJson = new EditEmployeeService().editEmployee(employeeDTO);
        return Response.ok().entity(responseJson).build();
    }


    @IsAdmin
    @Path("/remove/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeEmployee(@PathParam("id") int id){
        String responseJson = new RemoveEmployee().removeEmployee(id);
        return Response.ok().entity(responseJson).build();
    }


    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEmployee(@QueryParam("type") String type, @QueryParam("value") String value){
        String responseJson  = new SearchEmployeeService().searchEmployee(type, value);
        return Response.ok().entity(responseJson).build();
    }

}
