package controller;

import dal.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Accounts;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");

        AccountDAO dao = new AccountDAO();
        Accounts acc = dao.loginAccount(email, password);

        if (acc != null) {
            HttpSession session = req.getSession();
            session.setAttribute("account", acc);

            // Remember Me
            if ("on".equals(remember)) {
                Cookie cemail = new Cookie("email", email);
                Cookie cpass = new Cookie("password", password);
                Cookie crem = new Cookie("remember", "on");
                cemail.setMaxAge(60 * 60 * 24 * 7); // 7 ngày
                cpass.setMaxAge(60 * 60 * 24 * 7);
                crem.setMaxAge(60 * 60 * 24 * 7);
                resp.addCookie(cemail);
                resp.addCookie(cpass);
                resp.addCookie(crem);
            } else {
                Cookie cemail = new Cookie("email", "");
                Cookie cpass = new Cookie("password", "");
                Cookie crem = new Cookie("remember", "");
                cemail.setMaxAge(0);
                cpass.setMaxAge(0);
                crem.setMaxAge(0);
                resp.addCookie(cemail);
                resp.addCookie(cpass);
                resp.addCookie(crem);
            }

            resp.sendRedirect("views/dashboard.jsp"); // trang admin sau khi login
        } else {
            req.setAttribute("error", "Email or password is incorrect.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Đọc cookie nếu có
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("email")) {
                    req.setAttribute("email", c.getValue());
                }
                if (c.getName().equals("password")) {
                    req.setAttribute("password", c.getValue());
                }
                if (c.getName().equals("remember")) {
                    req.setAttribute("remember", "checked");
                }
            }
        }
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }
}
