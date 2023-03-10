/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CarModel;
import model.CarModelFacade;
import model.CarSales;
import model.CarSalesFacade;
import model.SalesHistory;
import model.SalesHistoryFacade;
import model.User;
import model.UserFacade;

/**
 *
 * @author N4O
 */

@WebServlet(name = "Users", urlPatterns = {"/home/Users"})
public class Users extends HttpServlet {

    @EJB
    private UserFacade userFacade;
    @EJB
    private CarSalesFacade carSalesFacade;
    @EJB
    private CarModelFacade carModelFacade;
    @EJB
    private SalesHistoryFacade salesHistoryFacade;
    
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
        User userCtx = (User)request.getSession().getAttribute("userCtx");
        if (userCtx == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            if (userCtx.isUser()) {
                System.out.println("redirecting...");
                response.sendRedirect(request.getContextPath() + "/home/index.jsp");
                return;
            }
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                request.getRequestDispatcher("/home/users.jsp").include(request, response);
                List<User> userDb = userFacade.findAll();
                List<CarSales> carSales = carSalesFacade.findAll();
                List<SalesHistory> salesHistory = salesHistoryFacade.findAll();
                
                HashMap<User, Integer> totalCars = new HashMap<User, Integer>();
                HashMap<User, Integer> totalRenting = new HashMap<User, Integer>();
                for (int i = 0; i < carSales.size(); i++) {
                    CarSales carSale = carSales.get(i);
                    User u = carSale.getSales();
                    if (totalCars.containsKey(u)) {
                        totalCars.put(u, totalCars.get(u) + 1);
                    } else {
                        totalCars.put(u, 1);
                    }
                }
                for (int i = 0; i < salesHistory.size(); i++) {
                    SalesHistory carSale = salesHistory.get(i);
                    User u = carSale.getLoaner();
                    if (totalRenting.containsKey(u)) {
                        totalRenting.put(u, totalRenting.get(u) + 1);
                    } else {
                        totalRenting.put(u, 1);
                    }
                }
                NumberFormat nf = NumberFormat.getNumberInstance();
                
                out.println("<hr class=\"border-gray-500 my-4\" />");
                out.println("<table class=\"table-sales\">");
                    out.println("<thead>");
                        out.println("<tr>");
                            out.println("<th class=\"table-h\">Username</th>");
                            out.println("<th class=\"table-h\">Gender</th>");
                            out.println("<th class=\"table-h\">User Type</th>");
                            out.println("<th class=\"table-h\">Total Renting</th>");
                            out.println("<th class=\"table-h\">Total Cars</th>");
                        out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody class=\"bg-slate-900\">");
                for (int i = 0; i < userDb.size(); i++) {
                    User cu = userDb.get(i);
                    int totalR = totalRenting.getOrDefault(cu, 0);
                    int totalC = totalCars.getOrDefault(cu, 0);
                        out.println("<tr>");
                            out.println("<td class=\"table-b\">" + cu.getUsername() + "</td>");
                            out.println("<td class=\"table-b\">" + cu.getGender() + "</td>");
                            out.println("<td class=\"table-b\">" + cu.getType().asName() + "</td>");
                    if (cu.isManager()) {
                            out.println("<td class=\"table-b\">N/A</td>");
                            out.println("<td class=\"table-b\">N/A</td>");
                    } else {
                            out.println("<td class=\"table-b\">" + nf.format(totalR) + "</td>");
                            out.println("<td class=\"table-b\">" + nf.format(totalC) + "</td>");
                    }
                        out.println("</tr>");
                }
                    out.println("</tbody>");
                out.println("</table>");
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
