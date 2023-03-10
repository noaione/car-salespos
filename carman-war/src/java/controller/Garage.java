/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
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

@WebServlet(name = "Garage", urlPatterns = {"/home/Garage"})
public class Garage extends HttpServlet {

    @EJB
    private UserFacade userFacade;
    @EJB
    private CarSalesFacade carSalesFacade;
    @EJB
    private CarModelFacade carModelFacade;
    
    private void writeSalesTable(PrintWriter out) {
        List<CarSales> carSales = carSalesFacade.findAll();
        if (carSales.size() < 1) {
            out.println("<p>There is no available car to buy!</p>");
            return;
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        out.println("<table class=\"car-table\">");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th class=\"car-table\">Model</th>");
        out.println("<th class=\"car-table\">Price</th>");
        out.println("<th class=\"car-table\">Seller</th>");
        out.println("<th class=\"car-table\">Status</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");
        for (int i = 0; i < carSales.size(); i++) {
            CarSales carSale = carSales.get(i);
            CarModel carModel = carSale.getCarModel();
            out.println("<tr>");
            out.println("<td class=\"car-table\">" + carModel.getName() + "</td>");
            out.println("<td class=\"car-table\">RM " + nf.format(carModel.getPrice()) + "</td>");
            out.println("<td class=\"car-table\">" + carSale.getSales().getUsername() + "</td>");
            String carStatus = carSale.getStatus().name().toLowerCase();
            carStatus = carStatus.substring(0, 1).toUpperCase() + carStatus.substring(1);
            carStatus = carStatus.replace('_', ' ');
            out.println("<td class=\"car-table\">" + carStatus + "</td>");
            out.println("</tr>");
        }
        out.println("</tbody>");
        out.println("</table>");
    }

    private void writePendingCar(PrintWriter out, String contextPath, User userCtx) {
        List<CarSales> carSales = carSalesFacade.findAllByStatus(CarSalesStatus.PENDING_APPROVAL);
        if (carSales.size() < 1) {
            out.println("<p>There is no available car to buy!</p>");
            return;
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
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
            String action = contextPath + "/home/Lent";
            out.println("<tr>");
            out.println("<td class=\"car-table\">" + carModel.getName() + "</td>");
            out.println("<td class=\"car-table\">RM " + nf.format(carModel.getPrice()) + "</td>");
            out.println("<td class=\"car-table\">" + carSale.getSales().getUsername() + "</td>");
            if (carSale.getStatus() != CarSalesStatus.PENDING_APPROVAL) {
                // no action can be done, it's been sold :)
                out.println("<td class=\"car-table\">No action</td>");
            } else {
                // action available
                out.println("<td class=\"car-table\">");
                out.println("<form id=\"" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"garage-action\" value=\"approve-car\">"
                    + "<input type=\"submit\"value=\"Approve\">"
                    + "</form>");
                out.println("<form id=\"" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"garage-action\" value=\"reject-car\">"
                    + "<input type=\"submit\"value=\"Reject\">"
                    + "</form>");
                out.println("</td>");
            }
            out.println("</tr>");
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
                request.getRequestDispatcher("/home/garage.jsp").include(request, response);
                if (request.getMethod().equals("GET")) {
                    writeSalesTable(out);
                } else if (request.getMethod().equals("POST")) {
                    writeSalesTable(out);
                    String salesId = request.getParameter("salesid");
                    if (salesId.trim().isEmpty()) {
                        out.println("<br><br><p>No sales ID has been provided!</p>");
                        return;
                    }
                    CarSales carSale = carSalesFacade.find(salesId);
                    if (carSale == null) {
                        out.println("<br><br><p>Unknown sales ID has been provided!</p>");
                        return;
                    }
                    if (carSale.getStatus() != CarSalesStatus.AVAILABLE) {
                        out.println("<br><br><p>The car you selected has been booked by someone else!</p>");
                        return;
                    }
                    carSale.setStatus(CarSalesStatus.BOOKED);
                    carSale.setOwner(userCtx);
                    try {
                        carSalesFacade.edit(carSale);
                        out.println("<br><br><p>Car has been booked, please proceed to Purchases tab</p>");   
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        out.println("<br><br><p>An internal server error has occured, please contact Admin!</p>");
                    }
                }
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
