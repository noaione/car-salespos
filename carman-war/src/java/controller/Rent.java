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

@WebServlet(name = "Rent", urlPatterns = {"/home/Rent"})
public class Rent extends HttpServlet {

    @EJB
    private UserFacade userFacade;
    @EJB
    private CarSalesFacade carSalesFacade;
    @EJB
    private CarModelFacade carModelFacade;

    private void makeError(PrintWriter out, String errText, String colorama) {
        // <div class="flex flex-col mx-auto items-center mt-2">
        //     <p class="text-red-400">An error has occurred</p>
        // </div>
        out.println("<div class=\"flex flex-col mx-auto items-center mt-4\">");
            out.println("<p class=\"" + colorama + "\">" + errText + "</p>");
        out.println("</div>");
    }
    
    private void makeError(PrintWriter out, String errText) {
        makeError(out, errText, "text-red-400");
    }
    
    private void writeSalesTable(PrintWriter out, String contextPath, User userCtx) {
        List<CarSales> carSales = carSalesFacade.findAllByStatusExceptUser(
                CarSalesStatus.AVAILABLE, userCtx
        );
        if (carSales.size() < 1) {
            out.println("<p class=\"no-mod\">There is no available car to rent!</p>");
            return;
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        out.println("<hr class=\"border-gray-500 my-4\" />");
        out.println("<table class=\"table-sales\">");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th class=\"table-h\">Model</th>");
        out.println("<th class=\"table-h\">Price</th>");
        out.println("<th class=\"table-h\">Seller</th>");
        out.println("<th class=\"table-h\">Action</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody class=\"bg-slate-900\">");
        for (int i = 0; i < carSales.size(); i++) {
            CarSales carSale = carSales.get(i);
            if (carSale.getSales().getId().equals(userCtx.getId())) {
                // do not sale to the same user because that's dumb, lmao.
                continue;
            }
            CarModel carModel = carSale.getCarModel();
            String action = contextPath + "/home/Rent";
            out.println("<form id=\"" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\">");
            out.println("<tr>");
            out.println("<td class=\"table-b\">" + carModel.getName() + "</td>");
            out.println("<td class=\"table-b\">RM " + nf.format(carModel.getPrice()) + "</td>");
            out.println("<td class=\"table-b\">" + carSale.getSales().getUsername() + "</td>");
            out.println("<td class=\"table-b\">");
            out.println("<div class=\"flex flex-col items-center gap-1 justify-center\">");
            out.println("<form id=\"act-book-" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\" class=\"flex\">"
                + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                + "<input type=\"submit\" value=\"Book\" class=\"table-btn-green\">"
                + "</form>");
            out.println("</div>");
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
                request.getRequestDispatcher("/home/rent.jsp").include(request, response);
                if (request.getMethod().equals("GET")) {
                    writeSalesTable(out, request.getContextPath(), userCtx);
                } else if (request.getMethod().equals("POST")) {
                    String salesId = request.getParameter("salesid");
                    if (salesId.trim().isEmpty()) {
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "No sales ID has been provided!");
                        return;
                    }
                    CarSales carSale = carSalesFacade.find(salesId);
                    if (carSale == null) {
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "Unknown sales ID has been provided!");
                        return;
                    }
                    if (carSale.getStatus() != CarSalesStatus.AVAILABLE) {
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "The car you selected has been booked by someone else!");
                        return;
                    }
                    carSale.setStatus(CarSalesStatus.BOOKED);
                    carSale.setOwner(userCtx);
                    try {
                        carSalesFacade.edit(carSale);
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "Car has been booked, please proceed to Rentals tab!", "text-emerald-400");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "An internal server error has occured, please contact Admin!");
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
