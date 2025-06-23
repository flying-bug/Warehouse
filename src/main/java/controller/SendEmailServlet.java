package controller;
import dal.EmailDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet(name = "SendEmailServlet", urlPatterns = {"/sendEmail"})
public class SendEmailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        System.out.println("Email:"+ email);
        String id = request.getParameter("id");
        System.out.println("ID:"+ id);
        if (email != null && !email.isEmpty()) {
            EmailDAO emailDAO = new EmailDAO();
            emailDAO.sendByEmail(email);
            request.getSession().setAttribute("msg", "Email sent successfully to: " + email);
        } else {
            request.getSession().setAttribute("error", "Email is missing or invalid.");
        }

        response.sendRedirect("viewExportOrderDetail?exportId=" + id);
    }
}

