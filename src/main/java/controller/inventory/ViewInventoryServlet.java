package controller.inventory;

import dal.InventoryDAO;
import dal.ProductDAO;
import dal.WarehouseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Inventory;
import model.Products;
import model.Warehouses;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewInventoryServlet", urlPatterns = {"/viewInventory"})
public class ViewInventoryServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String INVENTORY_PAGE = "/views/inventory/viewInventory.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            InventoryDAO inventoryDAO = new InventoryDAO();
            ProductDAO productDAO = new ProductDAO();
            WarehouseDAO warehouseDAO = new WarehouseDAO();

            // Lấy danh sách tồn kho
            List<Inventory> inventoryList = inventoryDAO.getAllInventory();
            List<Products> productList = productDAO.getAllProducts();
            List<Warehouses> warehouseList = warehouseDAO.getAllWarehouses();

            // Truyền dữ liệu ra view
            request.setAttribute("inventoryList", inventoryList);
            request.setAttribute("productList", productList);
            request.setAttribute("warehouseList", warehouseList);
            request.setAttribute("PAGE_CONTENT", INVENTORY_PAGE);

            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
} 