package com.ems.service;

import com.ems.entity.Employee;
import com.ems.util.AppUtil;
import com.ems.util.HibernateUtil;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RemoveEmployee {
    public String removeEmployee(int id) {

        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        try {

                Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = hibernateSession.beginTransaction();

                try {
                    Employee employee = hibernateSession.find(Employee.class, id);
                    if (employee != null) {
                            hibernateSession.remove(employee);
                            status = true;
                            message = "Employee removed.";

                    } else {
                        message = "Employee not found";
                    }

                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                    throw e;
                } finally {
                    hibernateSession.close();
                }

        } catch (Exception e) {
            message = "Error removing : " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);

    }
}
