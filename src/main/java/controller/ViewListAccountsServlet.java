package controller;

import dal.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Accounts;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewListAccountsServlet", urlPatterns = {"/viewListAccounts"})
public class ViewListAccountsServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String ACCOUNT_LIST_PAGE = "/views/accounts/viewListAccounts.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp); // fallback to GET for now
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountDAO dao = new AccountDAO();
        List<Accounts> accountList = dao.getAllAccounts();

        req.setAttribute("accountList", accountList);
        req.setAttribute("PAGE_CONTENT", ACCOUNT_LIST_PAGE);
        req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
    }
}
