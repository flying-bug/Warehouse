package controller.inventory;

import dal.InventoryDAO;
import dal.ProductDAO;
import dal.WarehouseDAO;
import model.Inventory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Products;
import model.Warehouses;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewListInventoryServlet", urlPatterns = {"/viewListInventory"})
public class ViewListInventoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int defaultPageSize = 10;
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            currentPage = Integer.parseInt(pageParam);
        }

        InventoryDAO inventoryDAO = new InventoryDAO();

        List<Inventory> fullInventoryList = inventoryDAO.getAllInventory();
        int totalItems = fullInventoryList.size();
        int totalPages = (int) Math.ceil((double) totalItems / defaultPageSize);
        int startIndex = (currentPage - 1) * defaultPageSize;
        int endIndex = Math.min(startIndex + defaultPageSize, totalItems);
        List<Inventory> inventoryList = fullInventoryList.subList(startIndex, endIndex);

        ProductDAO productDAO = new ProductDAO();
        List<Products> products = productDAO.getAllProducts();

        WarehouseDAO warehouseDAO = new WarehouseDAO();
        List<Warehouses> warehouses = warehouseDAO.getAllWarehouses();

        request.setAttribute("productList", products);
        request.setAttribute("warehouseList", warehouses);

        request.setAttribute("inventoryList", inventoryList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        // Forward the request to the JSP
        request.getRequestDispatcher("/views/inventory/viewInventory.jsp").forward(request, response);
    }
}