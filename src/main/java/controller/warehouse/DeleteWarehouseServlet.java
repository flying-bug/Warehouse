package controller.warehouse;

import dal.WarehouseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DeleteWarehouseServlet", urlPatterns = {"/deleteWarehouse"})
public class DeleteWarehouseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Get the warehouse ID from the request
        String warehouseId = request.getParameter("id");

        if (warehouseId != null && !warehouseId.isEmpty()) {
            try {
                WarehouseDAO warehouseDAO = new WarehouseDAO();
                boolean isDeleted = warehouseDAO.deleteWarehouse(Integer.parseInt(warehouseId));

                if (isDeleted) {
                    request.setAttribute("message", "Warehouse deleted successfully!");
                } else {
                    request.setAttribute("error", "Failed to delete warehouse. Please try again.");
                }

            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid Warehouse ID format.");
            }
        } else {
            request.setAttribute("error", "Warehouse ID is required to delete.");
        }

        response.sendRedirect("viewListWarehouses");
    }
}