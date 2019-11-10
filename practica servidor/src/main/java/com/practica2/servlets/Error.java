package com.practica2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name="Error",urlPatterns={"/error"})
public class Error extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "Error/Exception Information";
        String error = "";

        if (statusCode != null) {
            error = "Status code: " + statusCode + "<br/>";
        } if (throwable != null) {
            error = "Exception Type: " + throwable.getClass( ).getName() + "<br/>";
        }
        out.println("<!doctype html public \"-//w3c//dtd html 4.0\"transitional//en\">\n" +
                "<html>\n" +
                "   <head>\n" +
                "       <title>" + title + "</title>\n" +
                "   </head>\n" +
                "   <body>\n" +
                        error +
                "   </body>\n" +
                "</html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}