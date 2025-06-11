package controller.sale;

import com.google.gson.Gson;
import dal.CustomerDAO;
import dal.ExportOrderDAO;
import dal.ExportOrderDetailDAO;
import dal.InventoryDAO;
import dal.ProductDAO;
import dal.WarehouseDAO;
import model.Customers;
import model.Inventory;
import model.Products;
import model.Warehouses;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "CreateRequestProductServlet", urlPatterns = {"/createRequestProduct"})
public class CreateRequestProductServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String CREATE_EXPORT_ORDER_CONTENT = "/views/sales/createExportOrder.jsp";

    // Lớp để ánh xạ dữ liệu inventoryJson
    public static class InventoryWithProduct {
        private Products product;
        private int warehouseId;
        private int inventoryQuantity;

        public InventoryWithProduct(Products product, int warehouseId, int inventoryQuantity) {
            this.product = product;
            this.warehouseId = warehouseId;
            this.inventoryQuantity = inventoryQuantity;
        }

        public Products getProduct() {
            return product;
        }

        public int getWarehouseId() {
            return warehouseId;
        }

        public int getInventoryQuantity() {
            return inventoryQuantity;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        InventoryDAO inventoryDAO = new InventoryDAO();

        // Lấy danh sách kho
        List<Warehouses> warehouseList = warehouseDAO.getAllWarehouses();
        request.setAttribute("warehouseList", warehouseList);

        // Lấy danh sách tồn kho và sản phẩm
        List<Inventory> inventoryList = inventoryDAO.getAllInventory();
        List<Products> productList = productDAO.getAllProducts();

        // Tạo inventoryJson
        List<InventoryWithProduct> inventoryWithProductList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            Products product = productList.stream()
                    .filter(p -> p.getProductId() == inventory.getProductId())
                    .findFirst()
                    .orElse(null);
            if (product != null && product.getStatus() == 1) { // Chỉ lấy sản phẩm hoạt động
                inventoryWithProductList.add(new InventoryWithProduct(
                        product,
                        inventory.getWarehouseId(),
                        inventory.getQuantity()
                ));
            }
        }

        String inventoryJson = new Gson().toJson(inventoryWithProductList);
        request.setAttribute("inventoryJson", inventoryJson);

        request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        InventoryDAO inventoryDAO = new InventoryDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        ExportOrderDAO exportDAO = new ExportOrderDAO();
        ExportOrderDetailDAO detailDAO = new ExportOrderDetailDAO();

        // Lấy lại danh sách kho và tồn kho để hiển thị nếu có lỗi
        List<Warehouses> warehouseList = warehouseDAO.getAllWarehouses();
        request.setAttribute("warehouseList", warehouseList);
        List<Inventory> inventoryList = inventoryDAO.getAllInventory();
        List<Products> productList = productDAO.getAllProducts();
        List<InventoryWithProduct> inventoryWithProductList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            Products product = productList.stream()
                    .filter(p -> p.getProductId() == inventory.getProductId())
                    .findFirst()
                    .orElse(null);
            if (product != null && product.getStatus() == 1) {
                inventoryWithProductList.add(new InventoryWithProduct(
                        product,
                        inventory.getWarehouseId(),
                        inventory.getQuantity()
                ));
            }
        }
        request.setAttribute("inventoryJson", new Gson().toJson(inventoryWithProductList));

        try {
            // Lấy thông tin khách hàng
            String customerName = request.getParameter("customerName");
            String customerPhone = request.getParameter("customerPhone");
            String customerEmail = request.getParameter("customerEmail");
            String customerAddress = request.getParameter("customerAddress");
            String customerNote = request.getParameter("customerNote");

            // Lấy thông tin đơn hàng
            String[] warehouseIds = request.getParameterValues("warehouseIds[]");
            String[] productIds = request.getParameterValues("productIds[]");
            String[] quantities = request.getParameterValues("quantities[]");
            String reason = request.getParameter("reason");
            String note = request.getParameter("note");

            // Kiểm tra dữ liệu đầu vào
            if (warehouseIds == null || productIds == null || quantities == null ||
                    warehouseIds.length != productIds.length || productIds.length != quantities.length) {
                request.setAttribute("fail", "Dữ liệu sản phẩm không hợp lệ.");
                request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            // Kiểm tra tài khoản
            HttpSession session = request.getSession();
            Integer accountId = (Integer) session.getAttribute("id");
            if (accountId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Lưu thông tin khách hàng
            Customers customer = new Customers();
            customer.setName(customerName);
            customer.setPhone(customerPhone);
            customer.setEmail(customerEmail);
            customer.setAddress(customerAddress);
            customer.setNote(customerNote);
            customer.setStatus(1);
            int customerId = customerDAO.addCustomer(customer);
            if (customerId <= 0) {
                request.setAttribute("fail", "Không thể tạo thông tin khách hàng.");
                request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            // Kiểm tra tồn kho và tính tổng giá
            double totalPrice = 0;
            for (int i = 0; i < productIds.length; i++) {
                int warehouseId = Integer.parseInt(warehouseIds[i]);
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                // Kiểm tra tồn kho
                Inventory inventory = inventoryDAO.getInventoryById(productId, warehouseId);
                if (inventory == null || inventory.getQuantity() < quantity) {
                    request.setAttribute("fail", "Số lượng yêu cầu vượt quá tồn kho cho sản phẩm ID: " + productId);
                    request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                    request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                    return;
                }

                // Tính tổng giá
                Products product = productDAO.getProductById(productId);
                if (product == null) {
                    request.setAttribute("fail", "Sản phẩm không tồn tại: ID " + productId);
                    request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                    request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                    return;
                }
                double salePrice = product.getSalePrice().doubleValue();
                totalPrice += quantity * salePrice;
            }

            // Tạo đơn hàng xuất kho
            int exportId = exportDAO.createExportOrder(
                    customerId,
                    Integer.parseInt(warehouseIds[0]), // Giả sử tất cả sản phẩm từ cùng một kho
                    accountId,
                    reason,
                    note,
                    totalPrice
            );

            if (exportId <= 0) {
                request.setAttribute("fail", "Không thể tạo đơn hàng xuất kho.");
                request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            // Thêm chi tiết đơn hàng
            for (int i = 0; i < productIds.length; i++) {
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                Products product = productDAO.getProductById(productId);
                double salePrice = product != null ? product.getSalePrice().doubleValue() : 0;
                detailDAO.insertExportOrderDetail(exportId, productId, quantity, salePrice);
            }

            request.setAttribute("success", "Đơn hàng xuất kho đã được tạo thành công.");
            doGet(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("fail", "Đã xảy ra lỗi khi xử lý đơn hàng: " + e.getMessage());
            request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        }
    }
}