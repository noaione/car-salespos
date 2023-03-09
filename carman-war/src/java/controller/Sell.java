/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "Sell", urlPatterns = {"/home/Sell"})
public class Sell extends HttpServlet {

    @EJB
    private UserFacade userFacade;
    @EJB
    private CarSalesFacade carSalesFacade;
    @EJB
    private CarModelFacade carModelFacade;
    
    private void writeSalesTable(PrintWriter out, String contextPath, User userCtx) {
        List<CarSales> carSales = carSalesFacade.findAllForSeller(userCtx);
        out.println("<h3>Sold cars</h3>");
        if (carSales.size() < 1) {
            out.println("<p>You have not sale any car yet!</p>");
            return;
        }
        out.println("<table class=\"car-table\">");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th class=\"car-table\">Model</th>");
        out.println("<th class=\"car-table\">Price</th>");
        out.println("<th class=\"car-table\">Status</th>");
        out.println("<th class=\"car-table\">Action</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");
        for (int i = 0; i < carSales.size(); i++) {
            CarSales carSale = carSales.get(i);
            CarModel carModel = carSale.getCarModel();
            String action = contextPath + "/home/Sell";
            out.println("<tr>");
            out.println("<td class=\"car-table\">" + carModel.getName() + "</td>");
            out.println("<td class=\"car-table\">" + carModel.getPrice() + "</td>");
            String carStatus = carSale.getStatus().name().toLowerCase();
            carStatus = carStatus.substring(0, 1).toUpperCase() + carStatus.substring(1);
            out.println("<td class=\"car-table\">" + carStatus + "</td>");
            if (carSale.getStatus() != CarSalesStatus.AVAILABLE) {
                // no action can be done, it's been sold :)
                out.println("<td class=\"car-table\">No action</td>");
            } else {
                // action available
                out.println("<td class=\"car-table\">");
                out.println("<form id=\"" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"sell-action\" value=\"delete-car\">"
                    + "<input type=\"submit\"value=\"Remove\">"
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
                request.getRequestDispatcher("/home/sell.jsp").include(request, response);
                if (request.getMethod().equals("GET")) {
                    writeSalesTable(out, request.getContextPath(), userCtx);
                } else if (request.getMethod().equals("POST")) {
                    String postAction = request.getParameter("sell-action");
                    if (postAction == null) {
                        postAction = "";
                    }
                    if (postAction.trim().equals("create-new")) {
                        String carName = request.getParameter("name");
                        String carPriceStr = request.getParameter("price");
                        int carPrice;
                        try {
                            carPrice = Integer.parseInt(carPriceStr);
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            out.println("<br><br><p>You provided a wrong number!");
                            return;
                        }
                        if (carName == null) {
                            out.println("<br><br><p>You provided an empty name!");
                        }
                        CarModel carModel = new CarModel();
                        carModel.setName(carName);
                        carModel.setPrice(carPrice);
                        CarSales carSale = carModel.initSales(userCtx);
                        carSalesFacade.create(carSale);
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        out.println("<br><br><p>New listing created!</p>");
                    } else if (postAction.trim().equals("delete-car")) {
                        String salesId = request.getParameter("salesid");
                        if (salesId.trim().isEmpty()) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            out.println("<br><br><p>No sales ID has been provided!</p>");
                            return;
                        }
                        CarSales carSale = carSalesFacade.find(salesId);
                        if (carSale == null) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            out.println("<br><br><p>Unknown sales ID has been provided!</p>");
                            return;
                        }
                        if (carSale.getStatus() != CarSalesStatus.AVAILABLE) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            out.println("<br><br><p>The car you selected cannot be removed anymore!</p>");
                            return;
                        }
                        if (!carSale.getSales().getId().equals(userCtx.getId())) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            out.println("<br><br><p>You are not the seller for this car!</p>");
                        }
                        carSalesFacade.remove(carSale);
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        out.println("<br><br><p>Car removed!</p>");
                    } else {
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        out.println("<br><br><p>Unknown action has been found?!</p>");
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
