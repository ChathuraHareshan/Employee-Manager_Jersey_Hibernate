package com.ems.service;

import com.ems.dto.EmployeeDTO;
import com.ems.entity.Department;
import com.ems.entity.Employee;
import com.ems.util.AppUtil;
import com.ems.util.HibernateUtil;
import com.ems.validation.Validator;
import com.google.gson.JsonObject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EditEmployeeService {
    public String editEmployee(EmployeeDTO employeeDTO) {

        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";


        if(employeeDTO.getId() == 0){
            message = "Select valid Employee";
        }else if(employeeDTO.getFname().isBlank() || employeeDTO.getFname() == null){
            message = "First Name is Required.";
        }else if(employeeDTO.getLname().isBlank() || employeeDTO.getLname() == null){
            message = "Last Name is Required.";
        }else if(employeeDTO.getNic().isBlank() || employeeDTO.getNic() == null){
            message = "NIC is Required.";
        }else if(!employeeDTO.getNic().matches(Validator.NIC_VALIDATION)){
            message = "Add Correct NIC";
        }else if(employeeDTO.getEmail().isBlank() || employeeDTO.getEmail() == null){
            message = "Email is Required.";
        }else if(!employeeDTO.getEmail().matches(Validator.EMAIL_VALIDATION)){
            message = "Add Correct Email";
        }else if(employeeDTO.getMobile().isBlank() || employeeDTO.getMobile() == null){
            message = "Mobile is Required.";
        }else if(!employeeDTO.getMobile().matches(Validator.MOBILE_VALIDATION)){
            message = "Add Correct Mobile Number";
        }else if(employeeDTO.getPosition().isBlank() || employeeDTO.getPosition() == null){
            message = "Position is Required.";
        }else if(employeeDTO.getDepartmentId() == 0){
            message = "Department is Required.";
        }else if(employeeDTO.getHireDate() == null || employeeDTO.getHireDate().equals("")){
            message = "Hire Date is Required.";
        }else if(employeeDTO.getSalary() <= 0){
            message = "Salary is Required.";
        }else {

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();


            Employee editEmployee = hibernateSession.get(Employee.class, employeeDTO.getId());


            Transaction transaction = null;
            try {

                editEmployee.setFname(employeeDTO.getFname());
                editEmployee.setLname(employeeDTO.getLname());
                editEmployee.setEmail(employeeDTO.getEmail());
                editEmployee.setNic(employeeDTO.getNic());
                editEmployee.setMobile(employeeDTO.getMobile());
                editEmployee.setPosition(employeeDTO.getPosition());

                Department department = hibernateSession.get(Department.class, employeeDTO.getDepartmentId());

                editEmployee.setDepartment(department);
                editEmployee.setHireDate(employeeDTO.getHireDate());
                editEmployee.setSalary(employeeDTO.getSalary());
                editEmployee.setStatus(employeeDTO.getStatus());

                transaction = hibernateSession.beginTransaction();

                status = true;
                message = "success";
                hibernateSession.merge(editEmployee);
                transaction.commit();

            } catch (HibernateException e) {
                transaction.rollback();
                throw new RuntimeException(e.getMessage());
            } finally {
                hibernateSession.close();
            }

        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);

    }
}
