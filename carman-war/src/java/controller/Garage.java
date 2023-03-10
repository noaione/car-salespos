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
import model.SalesHistory;
import model.SalesHistoryFacade;
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
    
    private void writeSalesTable(PrintWriter out) {
        List<CarSales> carSalesTemp = carSalesFacade.findAll();
        ArrayList<CarSales> carSales = new ArrayList<CarSales>();
        for (int i = 0; i < carSalesTemp.size(); i++) {
            CarSales carSale = carSalesTemp.get(i);
            if (carSale.getStatus() == CarSalesStatus.PENDING_APPROVAL) {
                continue;
            }
            carSales.add(carSale);
        }
        if (carSales.size() < 1) {
            out.println("<p class=\"no-mod\">There is no registered rental cars!</p>");
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
        out.println("<th class=\"table-h\">Current Rental</th>");
        out.println("<th class=\"table-h\">Status</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody class=\"bg-slate-900\">");
        for (int i = 0; i < carSales.size(); i++) {
            CarSales carSale = carSales.get(i);
            CarModel carModel = carSale.getCarModel();
            out.println("<tr>");
            out.println("<td class=\"table-b\">" + carModel.getName() + "</td>");
            out.println("<td class=\"table-b\">RM " + nf.format(carModel.getPrice()) + "</td>");
            out.println("<td class=\"table-b\">" + carSale.getSales().getUsername() +"</td>");
            if (carSale.getOwner() != null) {
                out.println("<td class=\"table-b\">" + carSale.getOwner().getUsername() + "</td>");
            } else {
                out.println("<td class=\"table-b\">None</td>");
            }
            String carStatus = carSale.getStatus().name().toLowerCase();
            carStatus = carStatus.substring(0, 1).toUpperCase() + carStatus.substring(1);
            carStatus = carStatus.replace('_', ' ');
            out.println("<td class=\"table-b\">" + carStatus + "</td>");
            out.println("</tr>");
        }
        out.println("</tbody>");
        out.println("</table>");
    }

    private void writePendingCar(PrintWriter out, String contextPath) {
        List<CarSales> carSales = carSalesFacade.findAllByStatus(CarSalesStatus.PENDING_APPROVAL);
        out.println("<div class=\"flex flex-col mx-auto items-center mt-4\">");
        out.println("<h1 id=\"pending\" class=\"text-center text-xl font-semibold mt-2\">Pending Approval</h1>");
        out.println("<p class=\"mt-2\"><a class=\"nav-link\" href=\"#garages\">Garages</a></p>");
        out.println("</div>");
        if (carSales.size() < 1) {
            out.println("<p class=\"no-mod\">There is no pending approval right now</p>");
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
            CarModel carModel = carSale.getCarModel();
            String action = contextPath + "/home/Garage";
            out.println("<tr>");
            out.println("<td class=\"table-b\">" + carModel.getName() + "</td>");
            out.println("<td class=\"table-b\">RM " + nf.format(carModel.getPrice()) + "</td>");
            out.println("<td class=\"table-b\">" + carSale.getSales().getUsername() + "</td>");
            out.println("<td class=\"table-b\">");
            out.println("<div class=\"flex flex-col items-center gap-1 justify-center\">");
            if (carSale.getStatus() != CarSalesStatus.PENDING_APPROVAL) {
                // no action can be done
                out.println("<td class=\"table-b\">No action</td>");
            } else {
                // action available
                out.println("<form id=\"act-approve-" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\" class=\"flex\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"garage-action\" value=\"approve-car\">"
                    + "<input type=\"submit\"value=\"Approve\" class=\"table-btn-green\">"
                    + "</form>");
                out.println("<form id=\"act-reject-" + carSale.getId() + "\" action=\"" + action + "\" method=\"POST\" class=\"flex\">"
                    + "<input type=\"hidden\" name=\"salesid\" value=\"" + carSale.getId() + "\">"
                    + "<input type=\"hidden\" name=\"garage-action\" value=\"reject-car\">"
                    + "<input type=\"submit\"value=\"Reject\" class=\"table-btn-red\">"
                    + "</form>");
            }
            out.println("</div>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</tbody>");
        out.println("</table>");
    }
    
    private void writeTableView(PrintWriter out, String ctxPath) {
        writeSalesTable(out);
        writePendingCar(out, ctxPath);
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
                    writeTableView(out, request.getContextPath());
                } else if (request.getMethod().equals("POST")) {
                    String salesId = request.getParameter("salesid");
                    if (salesId.trim().isEmpty()) {
                        writeTableView(out, request.getContextPath());
                        makeError(out, "No sales ID has been provided!");
                        return;
                    }
                    CarSales carSale = carSalesFacade.find(salesId);
                    if (carSale == null) {
                        writeTableView(out, request.getContextPath());
                        makeError(out, "Unknown sales ID has been provided!");
                        return;
                    }
                    if (carSale.getStatus() != CarSalesStatus.PENDING_APPROVAL) {
                        writeTableView(out, request.getContextPath());
                        makeError(out, "The selected car is already approved!");
                        return;
                    }
                    String garageAct = request.getParameter("garage-action");
                    if (garageAct == null) {
                        garageAct = "";
                    }
                    if (garageAct.equals("approve-car")) {
                        // approve car
                        carSale.setStatus(CarSalesStatus.AVAILABLE);
                        carSalesFacade.edit(carSale);
                        writeTableView(out, request.getContextPath());
                        makeError(out, "Successfully approved car!", "text-emerald-400");
                    } else if (garageAct.equals("reject-car")) {
                        // reject car
                        List<SalesHistory> allSales = salesHistoryFacade.findAll();
                        System.out.println("Removing sales history data...");
                        for (int i = 0; i < allSales.size(); i++) {
                            SalesHistory sales = allSales.get(i);
                            if (sales.getCarSales().getId().equals(carSale.getId())) {
                                salesHistoryFacade.remove(sales);
                            }
                        }
                        CarModel carMod = carSale.getCarModel();
                        System.out.println("Removing car sales data...");
                        carSalesFacade.remove(carSale);
                        System.out.println("Removing car model data...");
                        carModelFacade.remove(carMod);
                        writeTableView(out, request.getContextPath());
                        makeError(out, "Rejected car, removed all references", "text-emerald-400");
                    } else {
                        writeTableView(out, request.getContextPath());
                        makeError(out, "Unknown sales ID has been provided!");
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
