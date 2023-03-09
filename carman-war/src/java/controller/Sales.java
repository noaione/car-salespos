/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
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
import model.CarSalesStatus;
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
    
    private void writeSalesTable(PrintWriter out, String contextPath, User userCtx) {
        List<CarSales> carSalesBooked = carSalesFacade.findAllForUser(userCtx);
        ArrayList<CarSales> carSales = new ArrayList<CarSales>();
        for (int i = 0; i < carSalesBooked.size(); i++) {
            CarSales carSale = carSalesBooked.get(i);
            if (carSale.getStatus() != CarSalesStatus.AVAILABLE) {
                carSales.add(carSale);
            }
        }
        if (carSales.size() < 1) {
            out.println("<p>You haven't bought any car yet!</p>");
            return;
        }
        out.println("<table class=\"car-table\">");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th class=\"car-table\">Model</th>");
        out.println("<th class=\"car-table\">Price</th>");
        out.println("<th class=\"car-table\">Seller</th>");
        out.println("<th class=\"car-table\">Action</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");
        for (int i = 0; i < carSales.size(); i++) {
            CarSales carSale = carSales.get(i);
            CarModel carModel = carSale.getCarModel();
            String action = contextPath + "/home/Purchases";
            out.println("<form id=\"" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\">");
            out.println("<tr>");
            out.println("<td class=\"car-table\">" + carModel.getName() + "</td>");
            out.println("<td class=\"car-table\">" + carModel.getPrice() + "</td>");
            out.println("<td class=\"car-table\">" + carSale.getSales().getUsername() + "</td>");
            out.println("<td class=\"car-table\">");
            if (carSale.getStatus() == CarSalesStatus.BOOKED) {
                // 
                out.println("<form id=\"" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"purchase-action\" value=\"purchase-car\">"
                    + "<input type=\"submit\"value=\"Purchase\">"
                    + "</form>");
                out.println("<form id=\"" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"purchase-action\" value=\"cancel-car\">"
                    + "<input type=\"submit\"value=\"Cancel\">"
                    + "</form>");
            } else if (carSale.getStatus() == CarSalesStatus.PAID) {
                // action available
                out.println("No action");
            }
            out.println("</td>");
            out.println("</tr>");
            out.println("</form>");
        }
        out.println("</tbody>");
        out.println("</table>");
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
        User userCtx = (User)request.getSession().getAttribute("userCtx");
        if (userCtx == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                request.getRequestDispatcher("/home/sales.jsp").include(request, response);
                List<CarSales> carSalesBooked = carSalesFacade.findAllForSeller(userCtx);
                if (carSalesBooked.isEmpty()) {
                    out.println("<p>You haven't sold any car yet!</p>");
                    return;
                }
                
                long overallSales = 0L; // paid only
                long expectedSales = 0L; // paid + booked
                long waitingSales = 0L; // booked only
                for (int i = 0; i < carSalesBooked.size(); i++) {
                    CarSales carSale = carSalesBooked.get(i);
                    int price = carSale.getCarModel().getPrice();
                    if (carSale.getStatus() == CarSalesStatus.PAID) {
                        overallSales += price;
                        expectedSales += price;
                    }
                    if (carSale.getStatus() == CarSalesStatus.BOOKED) {
                        expectedSales += price;
                        waitingSales += price;
                    }
                }
                
                NumberFormat nf = NumberFormat.getNumberInstance();
                String fmtOverall = nf.format(overallSales);
                String fmtExpected = nf.format(expectedSales);
                String fmtWaiting = nf.format(waitingSales);
                
                out.println("<div class\"flex flex-col\">");
                    out.println("<h3>Overall</h3>");
                    out.println("<div class=\"flex flex-row\">");
                        out.println("<p><strong>Total sales:</strong>&nbsp;RM&nbsp;</p>");
                        out.println("<p>" + fmtOverall + "</p>");
                    out.println("</div>");
                    out.println("<div class=\"flex flex-row\">");
                        out.println("<p><strong>Expected total:</strong>&nbsp;RM&nbsp;</p>");
                        out.println("<p>" + fmtExpected + "</p>");
                    out.println("</div>");
                    out.println("<div class=\"flex flex-row\">");
                        out.println("<p><strong>Waiting payment:</strong>&nbsp;RM&nbsp;</p>");
                        out.println("<p>" + fmtWaiting + "</p>");
                    out.println("</div>");
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
