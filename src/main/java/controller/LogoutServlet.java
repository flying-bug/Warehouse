package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "Logout", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Invalidate session if it exists
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Set cache control headers to prevent caching
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
        resp.setDateHeader("Expires", 0); // Prevent caching in the past
        resp.setHeader("X-Frame-Options", "DENY"); // Prevent clickjacking
        resp.setHeader("X-Content-Type-Options", "nosniff"); // Prevent MIME-type sniffing

        // Redirect to login page with success parameter
        req.getRequestDispatcher("/login.jsp?logout=success").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Redirect POST requests to doGet for consistency
        doGet(req, resp);
    }
}