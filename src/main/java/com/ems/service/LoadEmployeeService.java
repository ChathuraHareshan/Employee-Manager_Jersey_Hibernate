package com.ems.service;

import com.ems.dto.EmployeeDTO;
import com.ems.entity.Employee;
import com.ems.util.AppUtil;
import com.ems.util.HibernateUtil;
import com.google.gson.JsonObject;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class LoadEmployeeService {
    public String loadAllEmployee() {

        JsonObject responseObject = new JsonObject();
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        try{

            List<Employee> employees = hibernateSession.createQuery(
                            "FROM Employee order by id desc", Employee.class)
                    .getResultList();

            for(Employee employee : employees){

                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setId(employee.getId());
                employeeDTO.setFname(employee.getFname());
                employeeDTO.setLname(employee.getLname());
                employeeDTO.setEmail(employee.getEmail());
                employeeDTO.setMobile(employee.getMobile());
                employeeDTO.setNic(employee.getNic());
                employeeDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
                employeeDTO.setDepartmentId(employee.getDepartment().getId());
                employeeDTO.setPosition(employee.getPosition());
                employeeDTO.setHireDate(String.valueOf(employee.getHireDate()));
                employeeDTO.setSalary(employee.getSalary());
                employeeDTO.setStatus(employee.getStatus());

                employeeDTOList.add(employeeDTO);

            }

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }finally {
            hibernateSession.close();
        }


        responseObject.add("employeeList", AppUtil.GSON.toJsonTree(employeeDTOList));
        return AppUtil.GSON.toJson(responseObject);
    }
}
