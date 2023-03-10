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

@WebServlet(name = "Reports", urlPatterns = {"/home/Reports"})
public class Reports extends HttpServlet {

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
                request.getRequestDispatcher("/home/reports.jsp").include(request, response);
                List<User> userDb = userFacade.findAll();
                List<SalesHistory> salesHistory = salesHistoryFacade.findAll();
                List<CarSales> carSales = carSalesFacade.findAll();
                
                HashMap<User, Long> totalUserSales = new HashMap<User, Long>();
                HashMap<User, Integer> totalCars = new HashMap<User, Integer>();
                long overallSales = 0L;
                // the system assumes 10% commission price
                double actualSales = 0d;
                for (int i = 0; i < salesHistory.size(); i++) {
                    CarSales carSale = salesHistory.get(i).getCarSales();
                    User u = carSale.getSales();
                    long price = (long)carSale.getCarModel().getPrice();
                    if (totalUserSales.containsKey(u)) {
                        totalUserSales.put(u, totalUserSales.get(u) + price);
                    } else {
                        totalUserSales.put(u, price);
                    }
                    System.out.println((double)price);
                    double actPrice = (double)price * 0.1;
                    System.out.println(actPrice);
                    actualSales += actPrice;
                    overallSales += price;
                }
                for (int i = 0; i < carSales.size(); i++) {
                    CarSales carSale = carSales.get(i);
                    User u = carSale.getSales();
                    if (totalCars.containsKey(u)) {
                        totalCars.put(u, totalCars.get(u) + 1);
                    } else {
                        totalCars.put(u, 1);
                    }
                }

                NumberFormat nf = NumberFormat.getNumberInstance();
                String fmtOverallSales = nf.format(overallSales);
                String fmtActualSales = nf.format(actualSales);
                
                out.println("<div class\"flex flex-col gap-1\">");
                    out.println("<h3 class=\"text-lg font-semibold\">Overall Reports</h3>");
                    out.println("<p class=\"text-sm mb-2 font-light text-gray-100\">Actual sales is the 10% commission that the car rental takes</p>");
                    out.println("<div class=\"flex flex-row\">");
                        out.println("<p class=\"font-light\">");
                            out.println("<span class=\"font-bold\">Total gross sales:</span>&nbsp;RM&nbsp;" + fmtOverallSales);
                        out.println("</p>");
                    out.println("</div>");
                    out.println("<div class=\"flex flex-row\">");
                        out.println("<p class=\"font-light\">");
                            out.println("<span class=\"font-bold\">Total actual sales:</span>&nbsp;RM&nbsp;" + fmtActualSales);
                        out.println("</p>");
                    out.println("</div>");
                out.println("</div>");
                out.println("<div class\"flex flex-col gap-1\">");
                    out.println("<h3 class=\"text-lg font-semibold mt-4\">Individual Sales</h3>");
out.println("<hr class=\"border-gray-500 my-4\" />");
                out.println("<table class=\"table-sales\">");
                    out.println("<thead>");
                        out.println("<tr>");
                            out.println("<th class=\"table-h\">Username</th>");
                            out.println("<th class=\"table-h\">Total Gross Sales</th>");
                            out.println("<th class=\"table-h\">Total Actual Sales</th>");
                            out.println("<th class=\"table-h\">Total Cars</th>");
                        out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody class=\"bg-slate-900\">");
                for (int i = 0; i < userDb.size(); i++) {
                    User cu = userDb.get(i);
                    if (cu.isManager()) {
                        continue;
                    }
                    int totalC = totalCars.getOrDefault(cu, 0);
                    long totalSale = totalUserSales.getOrDefault(cu, 0L);
                    double commision = (double)totalSale * 0.1;
                    double actSale = totalSale - commision;
                        out.println("<tr>");
                            out.println("<td class=\"table-b\">" + cu.getUsername() + "</td>");
                            out.println("<td class=\"table-b\">RM " + nf.format(totalSale) + "</td>");
                            out.println("<td class=\"table-b\">RM " + nf.format(actSale) + "</td>");
                            out.println("<td class=\"table-b\">" + nf.format(totalC) + "</td>");
                        out.println("</tr>");
                }
                    out.println("</tbody>");
                out.println("</table>");
                out.println("</div>");
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
