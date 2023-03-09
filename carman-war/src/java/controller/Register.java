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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String username = request.getParameter("username");
            if (username.trim().isEmpty()) {
                request.getRequestDispatcher("register.jsp").include(request, response);
                out.println("<br><br><br> Username cannot be empty!");
                return;
            }
            String password = request.getParameter("password");
            String genderStr = request.getParameter("gender");
            if (genderStr.trim().isEmpty()) {
                request.getRequestDispatcher("register.jsp").include(request, response);
                out.println("<br><br><br> Gender cannot be empty!");
                return;
            }
            char gender = genderStr.charAt(0);
            if (gender != 'M' && gender != 'F' && gender != 'O') {
                request.getRequestDispatcher("register.jsp").include(request, response);
                out.println("<br><br><br>Gender must be either 'M', 'F', or 'O");
                return;
            }
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

                request.getRequestDispatcher("register.jsp").include(request, response);
                out.println("<br>Registered! You can try logging in now!");
            } catch (ExistingUsernameError unfe) {
                request.getRequestDispatcher("register.jsp").include(request, response);
                out.println("<br><br><br>Sorry, the username provided has already been registered!");
            } catch (InvalidPasswordError pwe) {
                request.getRequestDispatcher("register.jsp").include(request, response);
                out.println("<br><br><br>Sorry, " + pwe.getAccountId());
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
