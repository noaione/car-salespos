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
import model.ReviewData;
import model.ReviewDataFacade;
import model.SalesHistory;
import model.SalesHistoryFacade;
import model.User;
import model.UserFacade;

/**
 *
 * @author N4O
 */

@WebServlet(name = "Lent", urlPatterns = {"/home/Lent"})
public class Lent extends HttpServlet {

    @EJB
    private UserFacade userFacade;
    @EJB
    private CarSalesFacade carSalesFacade;
    @EJB
    private CarModelFacade carModelFacade;
    @EJB
    private SalesHistoryFacade salesHistoryFacade;
    @EJB
    private ReviewDataFacade reviewDataFacade;

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
        List<CarSales> carSales = carSalesFacade.findAllForSeller(userCtx);
        out.println("<h3 class=\"text-center text-xl font-semibold mt-4\">Loaned car</h3>");
        if (carSales.size() < 1) {
            out.println("<p class=\"no-mod\">You have not provided any car yet!</p>");
            return;
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        out.println("<hr class=\"border-gray-500 my-4\" />");
        out.println("<table class=\"table-sales\">");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th class=\"table-h\">Model</th>");
        out.println("<th class=\"table-h\">Price</th>");
        out.println("<th class=\"table-h\">Buyer</th>");
        out.println("<th class=\"table-h\">Status</th>");
        out.println("<th class=\"table-h\">Action</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody class=\"bg-slate-900\">");
        for (int i = 0; i < carSales.size(); i++) {
            CarSales carSale = carSales.get(i);
            CarModel carModel = carSale.getCarModel();
            String action = contextPath + "/home/Lent";
            out.println("<tr>");
            out.println("<td class=\"table-b\">" + carModel.getName() + "</td>");
            out.println("<td class=\"table-b\">RM " + nf.format(carModel.getPrice()) + "</td>");
            if (carSale.getOwner() != null) {
                out.println("<td class=\"table-b\">" + carSale.getOwner().getUsername() + "</td>");
            } else {
                out.println("<td class=\"table-b\">None</td>");
            }
            String carStatus = carSale.getStatus().name().toLowerCase();
            carStatus = carStatus.substring(0, 1).toUpperCase() + carStatus.substring(1);
            carStatus = carStatus.replace('_', ' ');
            out.println("<td class=\"table-b\">" + carStatus + "</td>");
            if (carSale.getStatus() != CarSalesStatus.AVAILABLE && carSale.getStatus() != CarSalesStatus.PENDING_APPROVAL) {
                // no action can be done, it's been sold :)
                out.println("<td class=\"table-b text-center\">No action</td>");
            } else {
                // action available
                out.println("<td class=\"table-b\">");
                out.println("<div class=\"flex flex-col items-center gap-1 justify-center\">");
                out.println("<form id=\"act-remove-" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\" class=\"flex\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"sell-action\" value=\"delete-car\">"
                    + "<input type=\"submit\"value=\"Remove\" class=\"table-btn-red\">"
                    + "</form>");
                if (carSale.getStatus() == CarSalesStatus.AVAILABLE || carSale.getStatus() == CarSalesStatus.PENDING_APPROVAL) {
                    out.println("<form id=\"act-edit-" + carSale.getId() + "\" action=\"" + action + "/Edit" + "\" method=\"GET\" class=\"flex\">"
                        + "<input type=\"hidden\" name=\"id\" value=\"" + carSale.getId() + "\">"
                        + "<input type=\"submit\"value=\"Edit\" class=\"table-btn-warn\">"
                        + "</form>");
                }
                out.println("</div>");
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
                request.getRequestDispatcher("/home/lent.jsp").include(request, response);
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
                            makeError(out, "You provided a wrong number!");
                            return;
                        }
                        if (carName == null) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            makeError(out, "You provided an empty car model name!");
                            return;
                        }
                        CarModel carModel = new CarModel();
                        carModel.setName(carName);
                        carModel.setPrice(carPrice);
                        CarSales carSale = carModel.initSales(userCtx);
                        carSalesFacade.create(carSale);
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        makeError(out, "New listing created!", "text-emerald-400");
                    } else if (postAction.trim().equals("delete-car")) {
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
                        if (carSale.getStatus() != CarSalesStatus.AVAILABLE && carSale.getStatus() != CarSalesStatus.PENDING_APPROVAL) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            makeError(out, "The car you selected cannot be removed anymore!");
                            return;
                        }
                        if (!carSale.getSales().getId().equals(userCtx.getId())) {
                            writeSalesTable(out, request.getContextPath(), userCtx);
                            makeError(out, "You are not the loaner for this car!");
                            return;
                        }
                        List<SalesHistory> allSales = salesHistoryFacade.findAll();
                        System.out.println("Removing sales history data...");
                        for (int i = 0; i < allSales.size(); i++) {
                            SalesHistory sales = allSales.get(i);
                            if (sales.getCarSales().getId().equals(carSale.getId())) {
                                ReviewData rd = reviewDataFacade.findForHistory(sales);
                                if (rd != null) {
                                    reviewDataFacade.remove(rd);
                                }
                                salesHistoryFacade.remove(sales);
                            }
                        }
                        CarModel carMod = carSale.getCarModel();
                        System.out.println("Removing car sales data...");
                        carSalesFacade.remove(carSale);
                        System.out.println("Removing car model data...");
                        carModelFacade.remove(carMod);
                        writeSalesTable(out, request.getContextPath(), userCtx);
                        out.println("<br><br><p>Car removed!</p>");
                        makeError(out, "Car listing removed!", "text-emerald-400");
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
