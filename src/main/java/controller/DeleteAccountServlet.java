package controller;

import dal.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Accounts;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DeleteAccountServlet", urlPatterns = {"/deleteAccount"})
public class DeleteAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        AccountDAO accountDAO = new AccountDAO();

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                boolean deactivated = accountDAO.deactivateAccount(id);
                if (deactivated) {
                    request.setAttribute("success", "Account marked as inactive successfully.");
                } else {
                    request.setAttribute("fail", "Failed to mark account as inactive.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("fail", "Invalid account ID format.");
            }
        } else {
            request.setAttribute("fail", "Account ID not provided.");
        }

        // Forward lại để load danh sách
        request.getRequestDispatcher("/viewListAccounts").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
