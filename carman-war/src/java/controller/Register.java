/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import errors.ExistingUsernameError;
import errors.InvalidPasswordError;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import model.UserFacade;
import model.UserType;

/**
 *
 * @author N4O
 */

@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    @EJB
    private UserFacade userFacade;

    private String checkPassword(String password) {
        password = password.trim();
        if (password.isEmpty()) {
            return "Password cannot be empty!";
        }
        if (password.length() < 6) {
            return "Password must be more than six and less than 60";
        }
        if (password.length() > 60) {
            return "Password must be more than six and less than 60";
        }
        return null;
    }
    
    private void makeError(PrintWriter out, String errText, String colorama) {
        // <div class="flex flex-col mx-auto items-center mt-2">
        //     <p class="text-red-400">An error has occurred</p>
        // </div>
        out.println("<div class=\"flex flex-col mx-auto items-center mt-2\">");
            out.println("<p class=\"" + colorama + "\">" + errText + "</p>");
        out.println("</div>");
    }
    
    private void makeError(PrintWriter out, String errText) {
        makeError(out, errText, "text-red-400");
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getMethod().equals("GET")) {
            response.sendError(405, "Only POST Method are allowed in here");
            return;
        }
        System.out.println(request.getPathInfo());
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String utypeRaw = request.getParameter("utype");
            if (utypeRaw == null) utypeRaw = "customer";
            UserType typeAct = UserType.CUSTOMER;
            switch (utypeRaw.toLowerCase()) {
                case "admin":
                    typeAct = UserType.MANAGER;
                    break;
                default:
                    typeAct = UserType.CUSTOMER;
                    break;
            }
            String dispatcher = "register.jsp";
            if (typeAct == UserType.MANAGER) {
                dispatcher = "registeradmin.jsp";
            }
            String username = request.getParameter("username");
            if (username == null) {
                username = "";
            }
            if (username.trim().isEmpty()) {
                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "Username cannot be empty!");
                return;
            }
            String password = request.getParameter("password");
            String genderStr = request.getParameter("gender");
            if (genderStr == null) {
                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "Gender cannot be empty!");
                return;
            }
            if (genderStr.trim().isEmpty()) {
                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "Gender cannot be empty!");
                return;
            }
            char gender = genderStr.charAt(0);
            if (gender != 'M' && gender != 'F' && gender != 'O') {
                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "Gender must be either 'M', 'F', or 'O'");
                return;
            }
            try {
                String passwdErr = checkPassword(password);
                if (passwdErr != null) {
                    throw new InvalidPasswordError(passwdErr);
                }
                User found = userFacade.findByUsername(username);
                if (found != null) {
                    throw new ExistingUsernameError(username);
                }
                
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setGender(gender);
                newUser.setType(typeAct);
                userFacade.create(newUser);

                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "Registered! You can try logging in now!", "text-green-400");
            } catch (ExistingUsernameError unfe) {
                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "Sorry, the username provided has already been registered!");
            } catch (InvalidPasswordError pwe) {
                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "Sorry, "  + pwe.getAccountId());
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
