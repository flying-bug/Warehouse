package controller.warehouse;

import dal.ExportOrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/confirmRequest")
public class ConfirmRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String exportIdParam = request.getParameter("exportId");
        String message = "invalid";  // giá trị mặc định nếu lỗi

        if (action == null || exportIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/viewListExportProducts?message=" + message);
            return;
        }

        int exportId;
        try {
            exportId = Integer.parseInt(exportIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/viewListExportProducts?message=invalid_id");
            return;
        }

        ExportOrderDAO dao = new ExportOrderDAO();
        int result = 0;

        switch (action) {
            case "update":
                result = dao.updateExportStatusToDone(exportId);
                message = (result > 0) ? "update_success" : "update_fail";
                break;

            case "delete":
                result = dao.cancelExportOrder(exportId);
                message = (result > 0) ? "delete_success" : "delete_fail";
                break;

            default:
                message = "unknown_action";
                break;
        }

        response.sendRedirect(request.getContextPath() + "/viewListExportProducts?message=" + message);
    }
}
