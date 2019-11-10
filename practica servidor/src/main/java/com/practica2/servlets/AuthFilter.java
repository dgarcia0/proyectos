package com.practica2.servlets;

import beans.ApplicationContextHandler;
import login.LoginUser;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/login"})
public class AuthFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        LoginUser login = (LoginUser) ApplicationContextHandler.context.getBean("loginUser");
        String password=req.getParameter("user_password");
        String userEmail=req.getParameter("user_email");

        if (login.authenticate(userEmail, password)) {
            chain.doFilter(req, resp);
        } else {
            RequestDispatcher rd=req.getRequestDispatcher("login");
            rd.include(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {
    }

}
