package com.practica2.servlets;

import beans.ApplicationContextHandler;
import data.User;
import data.UserData;
import data.UserNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet(name="Application",urlPatterns={"/application"})
@MultipartConfig
public class Application extends HttpServlet {
    private User user;

    @Override
    public void init() throws ServletException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserData users = (UserData) ApplicationContextHandler.context.getBean("userData");
        if(this.user == null) {
            String userEmail = request.getParameter("user_email");
            String week = request.getParameter("remember_user_email");

            try {
                this.user = users.getUserByEmail(userEmail);
            } catch (UserNotFoundException e) {
                this.user = null;
            }

            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession();
                session.setAttribute("userID",this.user);
                session.setMaxInactiveInterval(60*15);
            } else {
                System.out.println("Session alredy created");
            }

            if (week != null) {
                // Creamos una cookie (key,value) que dure una semana
                Cookie email = new Cookie("user_email", request.getParameter("user_email"));
                email.setMaxAge(7 * 24 * 60 * 60);
                response.addCookie(email);
            }
            printHtml(response.getWriter());
        }

        //Nuevo usuario
        if(request.getParameter("email") != null) {
            User newUser = (User) ApplicationContextHandler.context.getBean("user");
            newUser.setEmail(request.getParameter("email"));
            newUser.setName(request.getParameter("name"));
            newUser.setPasswd(request.getParameter("passwd"));
            users.add(newUser);
            printHtml(response.getWriter());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getSession(false) != null){
            printHtml(resp.getWriter());
        } else {
            resp.sendRedirect("login?error=" + "ServletException");
        }
    }

    private void printHtml(PrintWriter out){
        out.println(
                "<html>" +
                        "<head>" +
                        "   <title>Application</title>" +
                        "</head>" +
                        "<body>" +
                        "   <h1>Welcome " + this.user.getName() + "!</h1>" +
                        "   <h3>Add user</h3>" +
                        "   <a href=\"userTable\" name=\"add\">User list</a>" +
                        "\"<form action=\"logout\" method=\"get\">\n" +
                        "                                <input type=\"submit\" value=\"Sign out\">\n" +
                        "                                </form>" +
                        "<form action=\"application\" method=\"POST\">\n" +
                        "   Username: <input type=\"text\" name=\"name\"> <br/>" +
                        "   Mail: <input type=\"text\" name=\"email\"> <br/>" +
                        "   Password: <input type=\"password\" name=\"passwd\"> <br/>" +
                        "       <input type=\"file\" name=\"file\" enctype = \"multipart/form-data\">\n" +
                        "       <input type=\"submit\" value=\"add\">\n" +
                        "    </form>" +
                        "</body>" +
                        "</html>"
        );
    }
}