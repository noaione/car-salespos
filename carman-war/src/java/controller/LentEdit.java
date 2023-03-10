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
import model.SalesHistory;
import model.SalesHistoryFacade;
import model.User;
import model.UserFacade;

/**
 *
 * @author N4O
 */

@WebServlet(name = "LentEdit", urlPatterns = {"/home/Lent/Edit"})
public class LentEdit extends HttpServlet {

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
                request.getRequestDispatcher("/home/lentedit.jsp").include(request, response);
                if (request.getMethod().equals("GET")) {
                    String salesId = request.getParameter("id");
                    if (salesId == null) {
                        makeError(out, "ID not provided!");
                        return;
                    }
                    CarSales carSales = carSalesFacade.find(salesId);
                    if (carSales == null) {
                        makeError(out, "Unknown ID provided!");
                        return;
                    }
                    if (carSales.getStatus() != CarSalesStatus.AVAILABLE && carSales.getStatus() != CarSalesStatus.PENDING_APPROVAL) {
                        makeError(out, "Sorry, you cannot edit the car when it's being booked.");
                        return;
                    }
                    // write form
                    String ctxPath = request.getContextPath() + "/home/Lent/Edit";
                    out.println("<form action=\"" + ctxPath + "\" method=\"POST\" class=\"flex flex-col gap-1\">");
                        out.println("<div class=\"flex flex-col gap-1 align-middle justify-start\">");
                            out.println("<div class=\"flex\"><label>Car Model</label></div>");
                            out.println("<input type=\"text\" name=\"name\" class=\"form-input bg-gray-700 text-white rounded-md\"  placeholder=\"Enter car model\" value=\"" + carSales.getCarModel().getName() + "\" />");
                        out.println("</div>");
                        out.println("<div class=\"flex flex-col gap-1 align-middle justify-start\">");
                            out.println("<div class=\"flex\"><label>Price (In RM)</label></div>");
                            out.println("<input type=\"number\" name=\"price\" min=\"1\" class=\"form-input bg-gray-700 text-white rounded-md\"  placeholder=\"1\" value=\"" + carSales.getCarModel().getPrice() + "\" />");
                        out.println("</div>");
                        out.println("<div class=\"flex flex-col gap-1 align-middle justify-start mt-2\">");
                            out.println("<input type=\"hidden\" name=\"id\" value=\"" + salesId + "\">");
                            out.println("<input type=\"submit\" value=\"Edit\" class=\"px-2 py-2 rounded-md bg-emerald-500 text-white hover:bg-emerald-600 transition\" />");
                        out.println("</div>");
                    out.println("</form>");
                } else if (request.getMethod().equals("POST")) {
                    String carName = request.getParameter("name");
                    String carPriceStr = request.getParameter("price");
                    String salesId = request.getParameter("id");
                    if (salesId == null) {
                        makeError(out, "ID not provided!");
                        return;
                    }
                    CarSales carSales = carSalesFacade.find(salesId);
                    if (carSales == null) {
                        makeError(out, "Unknown ID provided!");
                        return;
                    }
                    int carPrice;
                    try {
                        carPrice = Integer.parseInt(carPriceStr);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        makeError(out, "You provided a wrong number!");
                        return;
                    }
                    if (carName == null) {
                        makeError(out, "You provided an empty car model name!");
                        return;
                    }
                    carName = carName.trim();
                    CarModel carMod = carSales.getCarModel();
                    Boolean anyChange = false;
                    if (!carName.equals(carMod.getName())) {
                        anyChange = true;
                        carMod.setName(carName);
                    }
                    if (carPrice != carMod.getPrice()) {
                        anyChange = true;
                        carMod.setPrice(carPrice);
                    }
                    if (!anyChange) {
                        makeError(out, "You didn't provide anything new to the data!");
                        return;
                    }
                    carModelFacade.edit(carMod);
                    makeError(out, "Listing edited!", "text-emerald-400");
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
