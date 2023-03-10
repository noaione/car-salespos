/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import errors.InvalidPasswordError;
import errors.UserNotFoundError;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import model.UserFacade;

/**
 *
 * @author N4O
 */

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    @EJB
    private UserFacade userFacade;
    
    private void makeError(PrintWriter out, String errText) {
        // <div class="flex flex-col mx-auto items-center mt-2">
        //     <p class="text-red-400">An error has occurred</p>
        // </div>
        out.println("<div class=\"flex flex-col mx-auto items-center mt-2\">");
            out.println("<p class=\"text-red-400\">" + errText + "</p>");
        out.println("</div>");
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
            String password = request.getParameter("password");
            try {
                User found = userFacade.findByUsername(username);
                if (found == null) {
                    throw new UserNotFoundError(username);
                }
                if (!found.getPassword().equals(password)) {
                    throw new InvalidPasswordError(username);
                }
                
                HttpSession sesi = request.getSession();
                sesi.setAttribute("userCtx", found);
                response.sendRedirect(request.getContextPath() + "/home/index.jsp");
            } catch (UserNotFoundError unfe) {
                request.getRequestDispatcher("login.jsp").include(request, response);
                makeError(out, "Sorry, the username provided cannot be found!");
            } catch (InvalidPasswordError e) {
                request.getRequestDispatcher("login.jsp").include(request, response);
                makeError(out, "Sorry, the password provided is invalid!");
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
