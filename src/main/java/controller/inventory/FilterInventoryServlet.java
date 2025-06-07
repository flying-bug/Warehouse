package controller;

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
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "FilterInventoryServlet", urlPatterns = {"/filterInventory"})
public class FilterInventoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productCode = request.getParameter("productCode");
        String productName = request.getParameter("productName");
        String warehouseIdRaw = request.getParameter("warehouseId");

        InventoryDAO inventoryDAO = new InventoryDAO();
        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        List<Inventory> inventoryList = inventoryDAO.getAllInventory();
        List<Products> products = productDAO.getAllProducts();
        List<Warehouses> warehouses = warehouseDAO.getAllWarehouses();

        // Lọc sản phẩm theo productCode và productName
        List<Products> filteredProducts = products;
        if (productCode != null && !productCode.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getProductCode().toLowerCase().contains(productCode.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (productName != null && !productName.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getName().toLowerCase().contains(productName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Lấy danh sách productId đã lọc
        Set<Integer> productIds = filteredProducts.stream()
                .map(Products::getProductId)
                .collect(Collectors.toSet());

        // Lọc inventory theo sản phẩm
        inventoryList = inventoryList.stream()
                .filter(inv -> productIds.contains(inv.getProductId()))
                .collect(Collectors.toList());

        // Lọc theo warehouseId nếu có
        if (warehouseIdRaw != null && !warehouseIdRaw.isEmpty()) {
            try {
                int warehouseId = Integer.parseInt(warehouseIdRaw);
                inventoryList = inventoryList.stream()
                        .filter(inv -> inv.getWarehouseId() == warehouseId)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {
            }
        }

        // Phân trang
        int currentPage = 1;
        int recordsPerPage = 10;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }

        int totalRecords = inventoryList.size();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        int start = (currentPage - 1) * recordsPerPage;
        int end = Math.min(start + recordsPerPage, totalRecords);
        List<Inventory> pagedInventory = inventoryList.subList(start, end);

        // Gửi dữ liệu ra view
        request.setAttribute("productList", products); // cần đủ để map tên, đơn vị
        request.setAttribute("warehouseList", warehouses);
        request.setAttribute("inventoryList", pagedInventory);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/views/inventory/viewInventory.jsp").forward(request, response);
    }
}
