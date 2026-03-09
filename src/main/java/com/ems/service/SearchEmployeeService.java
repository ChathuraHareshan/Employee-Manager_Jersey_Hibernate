package com.ems.service;

import com.ems.dto.EmployeeDTO;
import com.ems.entity.Employee;
import com.ems.util.AppUtil;
import com.ems.util.HibernateUtil;
import com.google.gson.JsonObject;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class SearchEmployeeService {

    public String searchEmployee(String type, String value) {

        JsonObject responseObject = new JsonObject();
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        try {

            String qry;

            switch (type) {

                case "Name":
                    qry = "FROM Employee WHERE fname LIKE :value OR lname LIKE :value ORDER BY id DESC";
                    break;

                case "Email":
                    qry = "FROM Employee WHERE email LIKE :value ORDER BY id DESC";
                    break;

                case "Mobile":
                    qry = "FROM Employee WHERE mobile LIKE :value ORDER BY id DESC";
                    break;

                case "NIC":
                    qry = "FROM Employee WHERE nic LIKE :value ORDER BY id DESC";
                    break;

                case "Position":
                    qry = "FROM Employee WHERE position LIKE :value ORDER BY id DESC";
                    break;

                case "Department":
                    qry = "FROM Employee WHERE department.departmentName LIKE :value ORDER BY id DESC";
                    break;

                case "Hire Date":
                    qry = "FROM Employee WHERE hireDate LIKE :value ORDER BY id DESC";
                    break;

                case "ID":
                    qry = "FROM Employee WHERE id = :value ORDER BY id DESC";
                    break;

                default:
                    qry = "FROM Employee ORDER BY id DESC";
            }

            List<Employee> employees;

            if (type.equals("ID")) {
                try {
                    int idValue = Integer.parseInt(value);
                    employees = session.createQuery(qry, Employee.class)
                            .setParameter("value", idValue)
                            .getResultList();
                } catch (NumberFormatException e) {
                    employees = new ArrayList<>();
                }
            } else {
                employees = session.createQuery(qry, Employee.class)
                        .setParameter("value", "%" + value + "%")
                        .getResultList();
            }

            for (Employee employee : employees) {

                EmployeeDTO dto = new EmployeeDTO();

                dto.setId(employee.getId());
                dto.setFname(employee.getFname());
                dto.setLname(employee.getLname());
                dto.setEmail(employee.getEmail());
                dto.setMobile(employee.getMobile());
                dto.setNic(employee.getNic());
                dto.setDepartmentName(employee.getDepartment().getDepartmentName());
                dto.setDepartmentId(employee.getDepartment().getId());
                dto.setPosition(employee.getPosition());
                dto.setHireDate(String.valueOf(employee.getHireDate()));
                dto.setSalary(employee.getSalary());
                dto.setStatus(employee.getStatus());

                employeeDTOList.add(dto);
            }

        } finally {
            session.close();
        }

        responseObject.add("employeeList", AppUtil.GSON.toJsonTree(employeeDTOList));
        return AppUtil.GSON.toJson(responseObject);

    }
}