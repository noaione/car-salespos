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

@WebServlet(name = "Sales", urlPatterns = {"/home/Sales"})
public class Sales extends HttpServlet {

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
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                request.getRequestDispatcher("/home/sales.jsp").include(request, response);
                List<CarSales> carSalesExist = carSalesFacade.findAllForSeller(userCtx);
                if (carSalesExist.isEmpty()) {
                    out.println("<p class=\"no-mod\">You haven't made any car listing yet!</p>");
                    return;
                }
                List<SalesHistory> carSalesBooked = salesHistoryFacade.findAllForUser(userCtx);
                if (carSalesBooked.isEmpty()) {
                    out.println("<p class=\"no-mod\">No one has rented your car listing yet!</p>");
                    return;
                }
                
                long overallSales = 0L;
                HashMap<CarModel, Integer> carUsages = new HashMap<CarModel, Integer>();
                for (int i = 0; i < carSalesBooked.size(); i++) {
                    SalesHistory carSale = carSalesBooked.get(i);
                    int price = carSale.getCarSales().getCarModel().getPrice();
                    overallSales += price;
                    CarModel car = carSale.getCarSales().getCarModel();
                    if (carUsages.containsKey(car)) {
                        carUsages.put(car, carUsages.get(car) + 1);
                    } else {
                        carUsages.put(car, 1);
                    }
                }
                for (int i = 0; i < carSalesExist.size(); i++) {
                    CarSales carSale = carSalesExist.get(i);
                    CarModel car = carSale.getCarModel();
                    if (!carUsages.containsKey(car)) {
                        carUsages.put(car, 0);
                    }
                }
                
                int maxCarCount = 0;
                CarModel mostFreqCar = null;
                for (HashMap.Entry<CarModel, Integer> entry : carUsages.entrySet()) {
                    int count = entry.getValue();
                    if (count > maxCarCount) {
                        maxCarCount = count;
                        mostFreqCar = entry.getKey();
                    }
                }

                NumberFormat nf = NumberFormat.getNumberInstance();
                String fmtOverallSales = nf.format(overallSales);
                
                out.println("<div class\"flex flex-col gap-1\">");
                    out.println("<h3 class=\"text-lg font-semibold\">Overall Sales</h3>");
                    out.println("<div class=\"flex flex-row\">");
                        out.println("<p class=\"font-light\">");
                            out.println("<span class=\"font-bold\">Total sales:</span>&nbsp;RM&nbsp;" + fmtOverallSales);
                        out.println("</p>");
                    out.println("</div>");
                    if (maxCarCount > 0) {
                    out.println("<div class=\"flex flex-row\">");
                        out.println("<p class=\"font-light\">");
                        String usageForm = (maxCarCount > 1) ? "usages" : "usage";
                            out.println("<span class=\"font-bold\">Most used car:</span>&nbsp;" + mostFreqCar.getName() + "&nbsp;<span class=\"font-bold\">(" + maxCarCount + " " + usageForm + ")</span>");
                        out.println("</p>");
                    out.println("</div>");
                    }
                out.println("</div>");
                out.println("<div class\"flex flex-col gap-1\">");
                    out.println("<h3 class=\"text-lg font-semibold mt-4\">Individual Sales</h3>");
                for (HashMap.Entry<CarModel, Integer> entry : carUsages.entrySet()) {
                    int count = entry.getValue();
                    int price = entry.getKey().getPrice();
                    int totalCurrentSale = price * count;
                    String fmtTotalCurrentSale = nf.format(totalCurrentSale);
                    String usageForm = (count > 1) ? "usages" : "usage";

                    out.println("<div class=\"flex flex-row\">");
                        out.println("<p class=\"font-light before:content-['•_']\">");
                            out.println("<span class=\"font-bold\">" + entry.getKey().getName() + "</span>:&nbsp;" + count + " " + usageForm + " (RM&nbsp;" + fmtTotalCurrentSale + ")");
                        out.println("</p>");
                    out.println("</div>");
                }
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
