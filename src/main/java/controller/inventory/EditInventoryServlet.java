package controller.inventory;

import dal.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;

@WebServlet(name = "EditInventoryServlet", urlPatterns = {"/editInventory"})
public class EditInventoryServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String UPDATE_PAGE = "/views/inventory/updateInventory.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            int warehouseId = Integer.parseInt(request.getParameter("warehouse"));

            InventoryDAO inventoryDAO = new InventoryDAO();
            ProductDAO productDAO = new ProductDAO();
            WarehouseDAO warehouseDAO = new WarehouseDAO();

            Inventory inventory = inventoryDAO.getInventoryById(productId, warehouseId);
            Products product = productDAO.getProductById(productId);
            Warehouses warehouse = warehouseDAO.getWarehouseById(warehouseId);

            request.setAttribute("inventory", inventory);
            request.setAttribute("product", product);
            request.setAttribute("warehouse", warehouse);
            request.setAttribute("PAGE_CONTENT", UPDATE_PAGE);

            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("viewListInventory");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setCharacterEncoding("UTF-8");

            int productId = Integer.parseInt(request.getParameter("productId"));
            int warehouseId = Integer.parseInt(request.getParameter("warehouseId"));
            int newQuantity = Integer.parseInt(request.getParameter("newQuantity"));
            String reason = request.getParameter("reason");

            HttpSession session = request.getSession();
            Accounts account = (Accounts) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            InventoryDAO inventoryDAO = new InventoryDAO();
            AdjustmentDAO adjustmentDAO = new AdjustmentDAO();
            ProductDAO productDAO = new ProductDAO();
            WarehouseDAO warehouseDAO = new WarehouseDAO();

            Inventory inventory = inventoryDAO.getInventoryById(productId, warehouseId);
            if (inventory == null) {
                request.setAttribute("error", "Inventory record not found.");
                reloadPageWithData(request, response, productId, warehouseId);
                return;
            }

            int oldQuantity = inventory.getQuantity();
            int quantityChange = newQuantity - oldQuantity;

            // Update inventory
            inventory.setQuantity(newQuantity);
            boolean inventoryUpdated = inventoryDAO.updateInventory(inventory);

            System.out.println(productId + " " + warehouseId + " " + quantityChange);
            // Log adjustment
            Adjustment adjustment = new Adjustment(
                    productId,
                    warehouseId,
                    quantityChange,
                    oldQuantity,
                    reason,
                    account.getAccount_id()
            );

            boolean adjustmentInserted = adjustmentDAO.insertAdjustment(adjustment);

            if (inventoryUpdated && adjustmentInserted) {
                response.sendRedirect("viewListInventory");
            } else {
                request.setAttribute("error", "Failed to update inventory or adjustment log.");
                reloadPageWithData(request, response, productId, warehouseId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            int productId = Integer.parseInt(request.getParameter("productId"));
            int warehouseId = Integer.parseInt(request.getParameter("warehouseId"));
            reloadPageWithData(request, response, productId, warehouseId);
        }
    }

    private void reloadPageWithData(HttpServletRequest request, HttpServletResponse response,
                                    int productId, int warehouseId) throws ServletException, IOException {
        InventoryDAO inventoryDAO = new InventoryDAO();
        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        request.setAttribute("inventory", inventoryDAO.getInventoryById(productId, warehouseId));
        request.setAttribute("product", productDAO.getProductById(productId));
        request.setAttribute("warehouse", warehouseDAO.getWarehouseById(warehouseId));
        request.setAttribute("PAGE_CONTENT", UPDATE_PAGE);
        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }
}
