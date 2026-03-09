package com.ems.service;

import com.ems.dto.DepartmentDTO;
import com.ems.dto.EmployeeDTO;
import com.ems.dto.UserDTO;
import com.ems.entity.Department;
import com.ems.entity.Employee;
import com.ems.entity.User;
import com.ems.util.AppUtil;
import com.ems.util.HibernateUtil;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContentService {
    public static String getAllDepartment() {

        JsonObject responseObject = new JsonObject();
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        List<DepartmentDTO> departmentDTOList = new ArrayList<>();

        try{

            List<Department> departments = hibernateSession.createQuery(
                            "FROM Department", Department.class)
                    .getResultList();

            for(Department department : departments){

                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(department.getId());
                departmentDTO.setDepartmentName(department.getDepartmentName());

                departmentDTOList.add(departmentDTO);


            }

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }finally {
            hibernateSession.close();
        }


        responseObject.add("departmentList", AppUtil.GSON.toJsonTree(departmentDTOList));
        return AppUtil.GSON.toJson(responseObject);

    }

    public static String getEmployeeById(int id) {

        JsonObject responseObject = new JsonObject();

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        List<EmployeeDTO> employeeDTOList = new ArrayList<>();


        Employee employee = hibernateSession.get(Employee.class , id);

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId(employee.getId());
        employeeDTO.setFname(employee.getFname());
        employeeDTO.setLname(employee.getLname());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setMobile(employee.getMobile());
        employeeDTO.setNic(employee.getNic());
        employeeDTO.setPosition(employee.getPosition());
        employeeDTO.setDepartmentId(employee.getDepartment().getId());
        employeeDTO.setDepartmentName(employee.getDepartment().getDepartmentName());

        java.sql.Date sqlDate = java.sql.Date.valueOf(employee.getHireDate());

        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(utilDate);
        employeeDTO.setHireDate(formattedDate);

        employeeDTO.setSalary(employee.getSalary());
        employeeDTO.setStatus(employee.getStatus());

        employeeDTOList.add(employeeDTO);

        responseObject.add("employee", AppUtil.GSON.toJsonTree(employeeDTOList));

        return AppUtil.GSON.toJson(responseObject);


    }

    public String getCurrentUser(HttpServletRequest request) {

        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        try{

            HttpSession httpSession = request.getSession(false);

            List<UserDTO> userDTOList = new ArrayList<>();

            if(httpSession == null){
                message = "No active user";

            }

            User currentUser = (User) httpSession.getAttribute("admin");

            if(currentUser == null){
                currentUser = (User) httpSession.getAttribute("user");
            }

            if(currentUser == null){
                message = "User not found.";
            }

            UserDTO userDTO = new UserDTO();

            userDTO.setFname(currentUser.getFname());
            userDTO.setLname(currentUser.getLname());
            userDTO.setEmail(currentUser.getEmail());
            userDTO.setRole(currentUser.getRole());

            userDTOList.add(userDTO);


            status = true;
            message = "success";

            responseObject.addProperty("status",status);
            responseObject.addProperty("message", message);


            responseObject.add("user", AppUtil.GSON.toJsonTree(userDTOList));

            return AppUtil.GSON.toJson(responseObject);



        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }


    }
}
