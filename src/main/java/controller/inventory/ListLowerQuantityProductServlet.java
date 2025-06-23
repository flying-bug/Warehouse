package controller.inventory;

import dal.InventoryDAO;
import dal.ProductDAO;
import dal.WarehouseDAO;
import model.Inventory;
import model.Products;
import model.Warehouses;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/lowInventory")
public class ListLowerQuantityProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InventoryDAO inventoryDAO = new InventoryDAO();
        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        List<Inventory> lowInventoryList = inventoryDAO.getLowInventory();  // Chỉ lấy những tồn kho thấp
        List<Products> products = productDAO.getAllProducts();              // Để map mã, tên, đơn vị
        List<Warehouses> warehouses = warehouseDAO.getAllWarehouses();     // Để map tên kho

        // Đẩy về JSP
        request.setAttribute("linventoryList", lowInventoryList);
        System.out.println(lowInventoryList.size());
        for (Inventory inv : lowInventoryList) {
            System.out.println("Product: " + inv.getProductId() + ", Warehouse: " + inv.getWarehouseId() + ", Quantity: " + inv.getQuantity());
        }

        request.setAttribute("productList", products);
        request.setAttribute("warehouseList", warehouses);
        request.setAttribute("PAGE_CONTENT", "/views/inventory/lowInventory.jsp");

        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
