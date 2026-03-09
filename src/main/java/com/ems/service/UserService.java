package com.ems.service;

import com.ems.dto.UserDTO;
import com.ems.entity.User;
import com.ems.util.AppUtil;
import com.ems.util.HibernateUtil;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;

public class UserService {
    public String loginUser(UserDTO userDTO, HttpServletRequest request) {

        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";


        if (userDTO.getEmail() == null) {
            message = "Email is required";
        } else if (userDTO.getEmail().isBlank()) {
            message = "Email address cannot be empty";
        }else if (userDTO.getPassword() == null) {
            message = "Password is required";
        } else if (userDTO.getPassword().isBlank()) {
            message = "Password address cannot be empty";
        } else {
            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            User singleUser = hibernateSession.createNamedQuery("User.getByEmail", User.class)
                    .setParameter("email", userDTO.getEmail())
                    .getSingleResultOrNull();

            if (singleUser == null) {
                message = "Authentication Failed.";
            } else {
                if (!singleUser.getPassword().equals(userDTO.getPassword())) {
                    message = "Something went wrong. Please Check your login credentials";
                } else {

                    if (singleUser.getStatus() != 1) {
                        message = "Authentication Failed.";
                    } else {

                        if(singleUser.getRole().equals("USER")){

                            HttpSession httpSession = request.getSession();
                            httpSession.setAttribute("user", singleUser);

                        }else{
                            HttpSession httpSession = request.getSession();
                            httpSession.setAttribute("admin", singleUser);
                        }


                        status = true;
                        message = "Login Successful";
                    }
                }
            }

            hibernateSession.close();
        }


        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);

    }
}
