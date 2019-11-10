package com.practica2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="Login",urlPatterns={"/login"})
public class Login extends HttpServlet {

    @Override
    public void init() throws ServletException {
    }

    // Creamos el login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Login";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
        String error = "";
        String email = "Email:<input type = \"text\" name = \"user_email\" value=\"\"/>\n";

        Cookie[] cookies = request.getCookies();

        if (request.getParameterMap().containsKey("error")) {
            error = "<h4>Exception: " + request.getParameter("error") + "</h4>\n";
        }

        if (cookies != null) {
            for (int i=0;i<cookies.length;i++) {
                if (cookies[i].getName().equals("user_email")) {
                    email = "Email:<input type = \"text\" name = \"user_email\" value=\"" + cookies[i].getValue() + "\"/>\n";
                }
            }
        }

            out.println(docType +
                    "<html>\n" +
                    "   <head>\n" +
                    "       <title>" + title + "</title>\n" +
                    "   </head>\n" +
                    "   <body>\n" +
                    "       <form action = \"application\" method = \"POST\">\n" +
                    email +
                    "           <br />\n" +
                    "           Password:<input type = \"password\" name = \"user_password\" />\n" +
                    "           <br />\n" +
                    "           Remember email (1 week):<input type=\"checkbox\" value=\"week\" name=\"remember_user_email\">\n" +
                    "           <br />\n" +
                    "           <input type = \"submit\" value = \"Submit\"/>\n" +
                    "       </form>\n" +
                    error +
                    "   </body>\n" +
                    "</html>");
    }


    @Override
    public void destroy() {
    }
}
