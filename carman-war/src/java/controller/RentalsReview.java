/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import errors.ExistingUsernameError;
import errors.InvalidPasswordError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CarModelFacade;
import model.CarSalesFacade;
import model.ReviewData;
import model.ReviewDataFacade;
import model.SalesHistory;
import model.SalesHistoryFacade;
import model.User;
import model.UserFacade;
import model.UserType;

/**
 *
 * @author N4O
 */

@WebServlet(name = "RentalsReview", urlPatterns = {"/home/Rentals/Review"})
public class RentalsReview extends HttpServlet {

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
        out.println("<div class=\"flex flex-col mx-auto items-center mt-2\">");
            out.println("<p class=\"" + colorama + "\">" + errText + "</p>");
        out.println("</div>");
    }
    
    private void makeError(PrintWriter out, String errText) {
        makeError(out, errText, "text-red-400");
    }
    
    private void createReviewBox(PrintWriter out, SalesHistory saleHist, String contextPath, String reviewMode) {
        ReviewData review = reviewDataFacade.findForHistory(saleHist);
        String selectReview = null;
        int selectRate = -1;
        if (review != null) {
            if (reviewMode.toLowerCase().equals("customer")) {
                selectReview = review.getUserReview();
                selectRate = review.getUserRate();
            } else if (reviewMode.toLowerCase().equals("seller")) {
                selectReview = review.getSellerReview();
                selectRate = review.getSellerRate();
            }
        }
        String action = contextPath + "/home/Rentals/Review";
        out.println("<form class=\"flex flex-col\" method=\"POST\" action=\"" + action + "\">");
            out.println("<p class=\"font-light text-gray-300 text-center mt-1 mb-2\">You are reviewing as <span class=\"font-bold text-glow\">" + reviewMode + "</span></p>");
            out.println("<div class=\"flex flex-col gap-1\">");
                out.println("<p class=\"font-medium text-lg\">Rating</p>");
                out.println("<div class=\"flex flex-row gap-2\">");
                    out.println("<div class=\"flex flex-row items-center gap-1\">");
                        out.println("<label>1★</label>");
                        out.print("<input type=\"radio\" class=\"form-radio rounded text-red-500\" value=\"1\" name=\"rating\"");
        if (selectRate == 1) {
                        out.print(" checked");
        }
                        out.println(" />");
                    out.println("</div>");
                    out.println("<div class=\"flex flex-row items-center gap-1\">");
                        out.println("<label>2★</label>");
                        out.print("<input type=\"radio\" class=\"form-radio rounded text-orange-500\" value=\"2\" name=\"rating\"");
        if (selectRate == 2) {
                        out.print(" checked");
        }
                        out.println(" />");
                    out.println("</div>");
                    out.println("<div class=\"flex flex-row items-center gap-1\">");
                        out.println("<label>3★</label>");
                        out.print("<input type=\"radio\" class=\"form-radio rounded text-yellow-500\" value=\"3\" name=\"rating\"");
        if (selectRate == 3) {
                        out.print(" checked");
        }
                        out.println(" />");
                    out.println("</div>");
                    out.println("<div class=\"flex flex-row items-center gap-1\">");
                        out.println("<label>4★</label>");
                        out.print("<input type=\"radio\" class=\"form-radio rounded text-blue-500\" value=\"4\" name=\"rating\"");
        if (selectRate == 4) {
                        out.print(" checked");
        }
                        out.println(" />");
                    out.println("</div>");
                    out.println("<div class=\"flex flex-row items-center gap-1\">");
                        out.println("<label>5★</label>");
                        out.print("<input type=\"radio\" class=\"form-radio rounded text-green-500\" value=\"5\" name=\"rating\"");
        if (selectRate == 5) {
                        out.print(" checked");
        }
                        out.println(" />");
                    out.println("</div>");
                out.println("</div>");
            out.println("</div>");
            out.println("<div class=\"flex flex-col mt-4\">");
                out.println("<p class=\"font-medium text-lg\">Review</p>");
                out.print("<textarea class=\"form-textarea rounded-md bg-gray-700 text-white mt-2\" rows=\"5\" name=\"reviewbox\" placeholder=\"Enter your review here...\">");
        if (selectReview != null) {
                out.print(selectReview);
        }
                out.println("</textarea>");
            out.println("</div>");
            out.println("<div class=\"flex flex-col mt-4\">");
                out.println("<input type=\"hidden\" name=\"histId\" value=\"" + saleHist.getId() + "\"/>");
                out.println("<input type=\"submit\" value=\"Submit\" class=\"rounded-md bg-emerald-600 py-2 hover:opacity-90 transition\" />");
            out.println("</div>");
        out.println("</form>");
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
                request.getRequestDispatcher("/home/rentalsreview.jsp").include(request, response);
                if (request.getMethod().equals("GET")) {
                    String salesId = request.getParameter("id");
                    if (salesId == null) {
                        makeError(out, "ID not provided!");
                        return;
                    }
                    SalesHistory histSale = salesHistoryFacade.find(salesId);
                    if (histSale == null) {
                        makeError(out, "Unknown ID provided!");
                        return;
                    }
                    if (histSale.getLoaner().getId().equals(userCtx.getId())) {
                        // this is customer
                        createReviewBox(out, histSale, request.getContextPath(), "Customer");
                    } else if (histSale.getCarSales().getSales().getId().equals(userCtx.getId())) {
                        // seller rview
                        createReviewBox(out, histSale, request.getContextPath(), "Seller");
                    } else {
                        makeError(out, "You are not allowed to review this sale!");
                    }
                } else  {
                    // POST
                    String salesId = request.getParameter("histId");
                    if (salesId == null) {
                        makeError(out, "ID not provided!");
                        return;
                    }
                    SalesHistory histSale = salesHistoryFacade.find(salesId);
                    if (histSale == null) {
                        makeError(out, "Unknown ID provided!");
                        return;
                    }
                    ReviewData rd = reviewDataFacade.findForHistory(histSale);
                    String rating = request.getParameter("rating");
                    String review = request.getParameter("reviewbox");
                    if (rating == null) {
                        createReviewBox(out, histSale, request.getContextPath(), "Customer");
                        makeError(out, "Please provide a rating");
                        return;
                    }
                    int ratingInt;
                    try {
                        ratingInt = Integer.parseInt(rating);
                    } catch (NumberFormatException ex) {
                        createReviewBox(out, histSale, request.getContextPath(), "Customer");
                        makeError(out, "Please provide a valid rating number!");
                        return;
                    }
                    if (ratingInt < 1 || ratingInt > 5) {
                        createReviewBox(out, histSale, request.getContextPath(), "Customer");
                        makeError(out, "Please provide a rating between 1 to 5!");
                        return;
                    }
                    if (review == null) {
                        review = "";
                    }
                    boolean createInstead = false;
                    if (rd == null) {
                        rd = new ReviewData();
                        rd.setSaleHistory(histSale);
                        createInstead = true;
                    }
                    if (histSale.getLoaner().getId().equals(userCtx.getId())) {
                        // this is customer
                        rd.setUserRate(ratingInt);
                        rd.setUserReview(review);
                        if (createInstead) {
                            reviewDataFacade.create(rd);
                        } else {
                            reviewDataFacade.edit(rd);
                        }
                        createReviewBox(out, histSale, request.getContextPath(), "Customer");
                        makeError(out, "Successfully reviewed as a Customer!", "text-emerald-400");
                    } else if (histSale.getCarSales().getSales().getId().equals(userCtx.getId())) {
                        // seller rview
                        rd.setSellerRate(ratingInt);
                        rd.setSellerReview(review);
                        if (createInstead) {
                            reviewDataFacade.create(rd);
                        } else {
                            reviewDataFacade.edit(rd);
                        }
                        createReviewBox(out, histSale, request.getContextPath(), "Seller");
                        makeError(out, "Successfully reviewed as a Seller!", "text-emerald-400");
                    } else {
                        makeError(out, "You are not allowed to review this sale!");
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
