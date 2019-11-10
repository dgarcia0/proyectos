package com.practica2.servlets;

import beans.ApplicationContextHandler;
import data.User;
import data.UserData;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="UserTable",urlPatterns={"/userTable"})
public class UserTable extends HttpServlet{
    List list;

    @Override
    public void init() throws ServletException { }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setIntHeader("Refresh", 5);
        response.setContentType("text/html");
        setBody(response.getWriter());
        if(request.getSession(false) == null){
            response.sendRedirect("login?error=" + "ServletException");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserData users = (UserData) ApplicationContextHandler.context.getBean("userData");

        int cont = 0;
        for (Object o :this.list) {
            if(request.getParameter("button"+ cont) != null){
                users.remove(((User) o).getEmail());
                break;
            }
            cont++;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        setBody(out);
    }

    @Override
    public void destroy() { }

    private void setBody(PrintWriter out){
        UserData users = (UserData) ApplicationContextHandler.context.getBean("userData");
        List list = users.getAllUsers();
        this.list = list;

        out.print("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "   <a href=\"application\">Back</a>" +
                "<table>");

        int cont = 0;
        for (Object user: list) {
            out.print("<tr>\n" +
                    "    <td><img src=\"http://www.pvhc.net/img219/iyyqqmvtbgcczemdegnx.png\" ></td>\n" +
                    "    <td>"+ ((User) user).getName() +"</td>\n" +
                    "    <td>"+ ((User) user).getEmail() +"</td>\n" +
                        "<td>" +
                    "<form action=\"userTable\" method=\"POST\">" +
                    "<input name=\"button"+ cont +"\" type=\"submit\" value=\"remove\">" +
                    "</form>"+
                    "</td>" +
                    "</tr>");
            cont++;
        }

        out.print("</table>\n" +
                "</body>\n" +
                "</html>");
    }
}
