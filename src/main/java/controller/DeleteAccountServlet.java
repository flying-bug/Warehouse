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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        AccountDAO accountDAO = new AccountDAO();
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            boolean deleted = accountDAO.deleteAccount(id);
            if (deleted) {
                request.setAttribute("success", "Account deleted successfully");
                request.getRequestDispatcher("/viewListAccounts").forward(request, response);
            } else {
                request.setAttribute("fail", "Account deleted failed");
                request.getRequestDispatcher("/viewListAccounts").forward(request, response);
            }

        }
    }
}

