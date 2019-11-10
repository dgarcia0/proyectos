package com.practica2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name="Logout",urlPatterns={"/logout"})
public class Logout extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession(false) != null){
            HttpSession session=request.getSession(false);
            session.invalidate();
            request.removeAttribute("user_email");
            request.removeAttribute("user_password");
            response.sendRedirect("login");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.removeAttribute("user_email");
            request.removeAttribute("user_password");
    }

}
