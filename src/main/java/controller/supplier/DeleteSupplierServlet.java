package controller.supplier;

import dal.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DeleteSupplierServlet", urlPatterns = {"/deleteSupplier"})
public class DeleteSupplierServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve the supplier ID sent as a request parameter
            String supplierIdString = request.getParameter("supplier_id");
            int supplierId = Integer.parseInt(supplierIdString);

            // Create DAO and delete the supplier from the database
            SupplierDAO supplierDAO = new SupplierDAO();
            boolean isDeleted = supplierDAO.deleteSupplier(supplierId);

            if (isDeleted) {
                // Redirect to supplier list page after successful deletion
                response.sendRedirect(request.getContextPath() + "/viewListSuppliers");
            } else {
                // Redirect to an error page if the supplier could not be deleted
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}