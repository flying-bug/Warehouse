package controller;

import dal.AccountDAO;
import dal.LoginByGoogle;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Accounts;
import model.UserGoogle;

import java.io.IOException;
import java.sql.SQLException;
@WebServlet(name = "GoogleLoginHandlerController", urlPatterns = {"/LoginGoogle"})
public class LoginByGoogleServlet extends HttpServlet {
    AccountDAO dao = new AccountDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        HttpSession session = request.getSession();
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            response.sendRedirect("login.jsp");
            return;
        }
        LoginByGoogle gg = new LoginByGoogle();
        String accesstoken = gg.getToken(code);
        UserGoogle data = gg.getUserInfo(accesstoken);
        AccountDAO dao = new AccountDAO();
        Accounts account = dao.getAccountByEmail(data.getEmail());

        if (account != null) {
            session.setAttribute("account", account);
            response.sendRedirect("views/dashboard.jsp");
        } else {
            request.setAttribute("error", "Tài khoản Google này chưa được đăng ký.");
            try {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            processRequest(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            processRequest(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

}
