/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import model.ReviewDataFacade;
import model.SalesHistory;
import model.SalesHistoryFacade;
import model.User;
import model.UserFacade;

/**
 *
 * @author N4O
 */

@WebServlet(name = "Rentals", urlPatterns = {"/home/Rentals"})
public class Rentals extends HttpServlet {

    @EJB
    private ReviewDataFacade reviewDataFacade;

    @EJB
    private UserFacade userFacade;
    @EJB
    private CarSalesFacade carSalesFacade;
    @EJB
    private CarModelFacade carModelFacade;
    @EJB
    private SalesHistoryFacade salesHistoryFacade;

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

    private void writeActiveSales(PrintWriter out, String contextPath, User userCtx) {
        List<CarSales> carSalesBooked = carSalesFacade.findAllForUser(userCtx);
        ArrayList<CarSales> carSales = new ArrayList<CarSales>();
        for (int i = 0; i < carSalesBooked.size(); i++) {
            CarSales carSale = carSalesBooked.get(i);
            if (carSale.getStatus() == CarSalesStatus.PENDING_APPROVAL) {
                continue;
            }
            if (carSale.getStatus() != CarSalesStatus.AVAILABLE) {
                carSales.add(carSale);
            }
        }
        out.println("<h2 class=\"text-xl font-semibold mt-2 text-center\">Active</p>");
        if (carSales.size() < 1) {
            out.println("<p class=\"no-mod\">There is no active rental right now!</p>");
            return;
        }
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
            CarModel carModel = carSale.getCarModel();
            String action = contextPath + "/home/Rentals";
            out.println("<form id=\"" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\">");
            out.println("<tr>");
            out.println("<td class=\"table-b\">" + carModel.getName() + "</td>");
            out.println("<td class=\"table-b\">" + carModel.getPrice() + "</td>");
            out.println("<td class=\"table-b\">" + carSale.getSales().getUsername() + "</td>");
            out.println("<td class=\"table-b\">");
            out.println("<div class=\"flex flex-col items-center gap-1 justify-center\">");
            if (carSale.getStatus() == CarSalesStatus.BOOKED) {
                // action available
                out.println("<form id=\"act-pay-" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\" class=\"flex\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"purchase-action\" value=\"rent-car\">"
                    + "<input type=\"submit\"value=\"Pay\" class=\"table-btn-green\">"
                    + "</form>");
                out.println("<form id=\"act-cancel-" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\" class=\"flex\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"purchase-action\" value=\"cancel-car\">"
                    + "<input type=\"submit\"value=\"Cancel\" class=\"table-btn-red\">"
                    + "</form>");
            } else if (carSale.getStatus() == CarSalesStatus.PAID) {
                // action available
                out.println("<form id=\"act-return-" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\" class=\"flex\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"purchase-action\" value=\"return-car\">"
                    + "<input type=\"submit\"value=\"Return\" class=\"table-btn-green\">"
                    + "</form>");
            }
            out.println("</div>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</tbody>");
        out.println("</table>");
    }
    
    private void writeHistoryTable(PrintWriter out, String contextPath, User userCtx) {
        List<SalesHistory> historySales = salesHistoryFacade.findAllForUser(userCtx);
        out.println("<h2 class=\"text-xl font-semibold mt-2 text-center\">History</p>");
        if (historySales.size() < 1) {
            out.println("<p class=\"no-mod\">You haven't rented any car yet!</p>");
            return;
        }
        out.println("<hr class=\"border-gray-500 my-4\" />");
        out.println("<table class=\"table-sales\">");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th class=\"table-h\">Model</th>");
        out.println("<th class=\"table-h\">Price</th>");
        out.println("<th class=\"table-h\">Return Time</th>");
        out.println("<th class=\"table-h\">Seller</th>");
        out.println("<th class=\"table-h\">Action</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody class=\"bg-slate-900\">");
        for (int i = 0; i < historySales.size(); i++) {
            SalesHistory histSale = historySales.get(i);
            CarSales carSale = histSale.getCarSales();
            CarModel carModel = carSale.getCarModel();
            String action = contextPath + "/home/Rentals/Review";
            out.println("<tr>");
            out.println("<td class=\"table-b\">" + carModel.getName() + "</td>");
            out.println("<td class=\"table-b\">" + carModel.getPrice() + "</td>");
            long timestamp = histSale.getTimestamp();
            Instant ts = Instant.ofEpochSecond(timestamp);
            ZonedDateTime mytTimestamp = ts.atOffset(ZoneOffset.ofHours(8)).toZonedDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
            out.println("<td class=\"table-b\">" + mytTimestamp.format(formatter) + "</td>");
            out.println("<td class=\"table-b\">" + carSale.getSales().getUsername() + "</td>");
            out.println("<td class=\"table-b\">");
            out.println("<div class=\"flex flex-col items-center gap-1 justify-center\">");
            out.println("<form id=\"act-review-" + carSale.getId() + "\" action=\"" + action + "\" method=\"GET\" class=\"flex\">"
                + "<input type=\"hidden\" name=\"id\" value=\"" + histSale.getId() + "\">"
                + "<input type=\"submit\"value=\"Review\" class=\"table-btn-warn\">"
                + "</form>");
            out.println("</div>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</tbody>");
        out.println("</table>");
    }
    
    private void writeSalesTable(PrintWriter out, String contextPath, User userCtx) {
        writeActiveSales(out, contextPath, userCtx);
        writeHistoryTable(out, contextPath, userCtx);
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
            if (userCtx.isManager()) {
                System.out.println("redirecting...");
                response.sendRedirect(request.getContextPath() + "/home/index.jsp");
                return;
            }
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                request.getRequestDispatcher("/home/rentals.jsp").include(request, response);
                if (request.getMethod().equals("GET")) {
                    writeSalesTable(out, request.getContextPath(), userCtx);
                } else if (request.getMethod().equals("POST")) {
                    String postAction = request.getParameter("purchase-action");
                    String salesId = request.getParameter("salesid");
                    if (postAction == null) {
                        postAction = "";
                    }
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
                    if (!carSale.getOwner().getId().equals(userCtx.getId())) {
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "You are not the renter for this car!");
                        return;
                    }
                    if (postAction.trim().equals("rent-car")) {
                        if (carSale.getStatus() != CarSalesStatus.BOOKED) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            makeError(out, "The car you selected are not booked by you!");
                            return;
                        }
                        carSale.setStatus(CarSalesStatus.PAID);
                        carSalesFacade.edit(carSale);
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "Car rented and paid fully!", "text-emerald-400");
                    } else if (postAction.trim().equals("cancel-car")) {
                        if (carSale.getStatus() != CarSalesStatus.BOOKED) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            makeError(out, "The car you selected are not booked by you!");
                            return;
                        }
                        carSale.setStatus(CarSalesStatus.AVAILABLE);
                        carSale.setOwner(null);
                        carSalesFacade.edit(carSale);
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "You cancelled your car rent!", "text-emerald-400");
                    } else if (postAction.trim().equals("return-car")) {
                        if (carSale.getStatus() != CarSalesStatus.PAID) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            makeError(out, "You cannot return this car since you haven't paid it yet!");
                            return;
                        }
                        carSale.setStatus(CarSalesStatus.AVAILABLE);
                        carSale.setOwner(null);
                        carSalesFacade.edit(carSale);
                        SalesHistory shist = new SalesHistory();
                        shist.setCarSales(carSale);
                        // set the loaner to this user.
                        shist.setLoaner(userCtx);
                        shist.setTimestamp();
                        salesHistoryFacade.create(shist);
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "You returned the rented car!", "text-emerald-400");
                    } else {
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "Unknown action has been found?!");
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
