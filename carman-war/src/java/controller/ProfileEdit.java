/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import errors.ExistingUsernameError;
import errors.InvalidPasswordError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import model.UserFacade;
import model.UserType;

/**
 *
 * @author N4O
 */

@WebServlet(name = "ProfileEdit", urlPatterns = {"/home/Profile"})
public class ProfileEdit extends HttpServlet {

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

    private String validateUsername(String username) {
        username = username.trim();
        Pattern ptr = Pattern.compile("\\s");
        Matcher mtch = ptr.matcher(username);
        boolean hasWhitespace = mtch.find();
        if (username.length() > 60) {
            return "Username is way too long, maximum is 60";
        }
        if (hasWhitespace) {
            return "Username cannot include whitespace";
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
        String dispatcher = "/home/profile.jsp";
        try (PrintWriter out = response.getWriter()) {
            String uuid = request.getParameter("uuid");
            if (uuid == null) {
                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "Unknown user ID provided!");
                return;
            }
            User user = userFacade.find(uuid);
            if (user == null) {
                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "Unknown user ID provided!");
                return;
            }
            String username = request.getParameter("username");
            if (username == null) {
                username = "";
            }
            Boolean anyChangeMade = false;
            if (!username.trim().isEmpty()) {
                User found = userFacade.findByUsername(username);
                if (found != null) {
                    request.getRequestDispatcher(dispatcher).include(request, response);
                    makeError(out, "Username already registered!");
                    return;
                }
                String usernameErr = validateUsername(username);
                if (usernameErr != null) {
                    request.getRequestDispatcher(dispatcher).include(request, response);
                    makeError(out, usernameErr);
                    return;
                }
                user.setUsername(username);
                anyChangeMade = true;
            }
            String genderStr = request.getParameter("gender");
            if (genderStr == null) {
                genderStr = "";
            }
            if (!genderStr.isEmpty()) {
                char gender = genderStr.charAt(0);
                if (!genderStr.isEmpty() && (gender != 'M' && gender != 'F' && gender != 'O')) {
                    request.getRequestDispatcher(dispatcher).include(request, response);
                    makeError(out, "Gender must be either 'M', 'F', or 'O'");
                    return;
                }
                user.setGender(gender);
                anyChangeMade = true;
            }
            try {
                String password = request.getParameter("newpassword");
                if (password != null) {
                    String oldPassword = request.getParameter("oldpassword");
                    if (oldPassword == null) {
                        throw new InvalidPasswordError("You must enter the old password when setting a new one!");
                    }
                    if (!oldPassword.equals(password)) {
                        throw new InvalidPasswordError("The old password is not the same as the one in database!");
                    }
                    String passwdErr = checkPassword(password);
                    if (passwdErr != null) {
                        throw new InvalidPasswordError(passwdErr);
                    }
                    user.setPassword(password);
                    anyChangeMade = true;
                }
                
                if (!anyChangeMade) {
                    request.getRequestDispatcher(dispatcher).include(request, response);
                    makeError(out, "There is no change applied!");
                    return;
                }
                
                userFacade.edit(user);
                // done, set the new request
                HttpSession sesi = request.getSession(false);
                if (sesi == null) {
                    // bruh moment
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                    return;
                }
                
                sesi.setAttribute("userCtx", user);

                request.getRequestDispatcher(dispatcher).include(request, response);
                makeError(out, "User information is now updated!", "text-emerald-400");
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
