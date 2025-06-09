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

@WebServlet(name = "ViewListInventoryServlet", urlPatterns = {"/viewListInventory"})
public class ViewListInventoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int pageSize = 10;
        int currentPage = 1;

        // Lấy tham số page nếu có
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }

        InventoryDAO inventoryDAO = new InventoryDAO();
        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        // Danh sách đầy đủ để phân trang
        List<Inventory> fullList = inventoryDAO.getAllInventory();
        int totalItems = fullList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<Inventory> paginatedList = fullList.subList(startIndex, endIndex);

        // Truyền dữ liệu ra view
        request.setAttribute("inventoryList", paginatedList);
        request.setAttribute("productList", productDAO.getAllProducts());
        request.setAttribute("warehouseList", warehouseDAO.getAllWarehouses());
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        // Sử dụng layout với PAGE_CONTENT
        request.setAttribute("PAGE_CONTENT", "/views/inventory/viewInventory.jsp");
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
