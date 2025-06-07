package controller.warehouse;

import dal.WarehouseDAO;
import model.Warehouses;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "UpdateWarehouseServlet", urlPatterns = {"/updateWarehouse"})
public class UpdateWarehouseServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String CONTENT_PAGE = "/views/warehouses/updateWarehouse.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String warehouseIdStr = request.getParameter("id");

        try {
            int warehouseId = Integer.parseInt(warehouseIdStr);
            WarehouseDAO warehouseDAO = new WarehouseDAO();
            Warehouses warehouse = warehouseDAO.getWarehouseById(warehouseId);

            if (warehouse != null) {
                request.setAttribute("warehouse", warehouse);
                request.setAttribute("PAGE_CONTENT", CONTENT_PAGE);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
            } else {
                response.sendRedirect("viewListWarehouses?error=Warehouse not found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("viewListWarehouses?error=Invalid Warehouse ID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String warehouseIdStr = request.getParameter("warehouseId");
        String name = request.getParameter("name");
        String location = request.getParameter("location");

        try {
            int warehouseId = Integer.parseInt(warehouseIdStr);
            Warehouses warehouse = new Warehouses(warehouseId, name, location);

            WarehouseDAO warehouseDAO = new WarehouseDAO();
            boolean isUpdated = warehouseDAO.updateWarehouse(warehouse);

            if (isUpdated) {
                response.sendRedirect("viewListWarehouses?success=Warehouse updated successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to update the warehouse! Please try again.");
                request.setAttribute("warehouse", warehouse);
                request.setAttribute("PAGE_CONTENT", CONTENT_PAGE);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Warehouse ID format!");
            request.setAttribute("PAGE_CONTENT", CONTENT_PAGE);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        }
    }
}
