package controller.purchase;

import dal.ImportOrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/confirmImport")
public class ConfirmImportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String importIdParam = request.getParameter("importId");
        String message = "invalid";

        if (action == null || importIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/viewListImportOrders?message=" + message);
            return;
        }

        int importId;
        try {
            importId = Integer.parseInt(importIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/viewListImportOrders?message=invalid_id");
            return;
        }

        ImportOrderDAO dao = new ImportOrderDAO();
        int result = 0;

        switch (action) {
            case "update":
                result = dao.updateImportStatusToDone(importId);
                message = (result > 0) ? "update_success" : "update_fail";
                break;

            case "delete":
                result = dao.cancelImportOrder(importId);
                message = (result > 0) ? "delete_success" : "delete_fail";
                break;

            default:
                message = "unknown_action";
                break;
        }

        response.sendRedirect(request.getContextPath() + "/viewListImportOrders?message=" + message);
    }
}
