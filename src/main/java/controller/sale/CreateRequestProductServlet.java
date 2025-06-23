package controller.sale;

import com.google.gson.Gson;
import dal.CustomerDAO;
import dal.ExportOrderDAO;
import dal.ExportOrderDetailDAO;
import dal.InventoryDAO;
import dal.ProductDAO;
import dal.WarehouseDAO;
import model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "CreateRequestProductServlet", urlPatterns = {"/createRequestProduct"})
public class CreateRequestProductServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String CREATE_EXPORT_ORDER_CONTENT = "/views/sales/createExportOrder.jsp";

    // Lớp để ánh xạ dữ liệu inventoryJson
    public static class InventoryWithProduct {
        private int productId;
        private String productCode;
        private String name;
        private double salePrice;
        private int warehouseId;
        private int inventoryQuantity;

        public InventoryWithProduct(Products product, int warehouseId, int inventoryQuantity) {
            this.productId = product.getProductId();
            this.productCode = product.getProductCode();
            this.name = product.getName();
            this.salePrice = product.getSalePrice().doubleValue();
            this.warehouseId = warehouseId;
            this.inventoryQuantity = inventoryQuantity;
        }

        public int getProductId() {
            return productId;
        }

        public String getProductCode() {
            return productCode;
        }

        public String getName() {
            return name;
        }

        public double getSalePrice() {
            return salePrice;
        }

        public int getWarehouseId() {
            return warehouseId;
        }

        public int getInventoryQuantity() {
            return inventoryQuantity;
        }

        @Override
        public String toString() {
            return "InventoryWithProduct{" +
                    "productId=" + productId +
                    ", productCode='" + productCode + '\'' +
                    ", name='" + name + '\'' +
                    ", salePrice=" + salePrice +
                    ", warehouseId=" + warehouseId +
                    ", inventoryQuantity=" + inventoryQuantity +
                    '}';
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
        List<Products> productList = productDAO.getAllProducts();
        request.setAttribute("productList", productList);


        System.out.println("Product List: " + productList);


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

        try {
            // Lấy thông tin khách hàng từ form
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

            // Kiểm tra dữ liệu
            if (warehouseIds == null || productIds == null || quantities == null ||
                    warehouseIds.length != productIds.length || productIds.length != quantities.length) {
                request.setAttribute("fail", "Dữ liệu sản phẩm không hợp lệ.");
                request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            // Kiểm tra tài khoản đăng nhập
            HttpSession session = request.getSession();
            Accounts account = (Accounts) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Kiểm tra khách hàng đã tồn tại theo SĐT
            Customers customer = customerDAO.getCustomerByPhone(customerPhone);
            int customerId;

            if (customer != null) {
                customerId = customer.getCustomerId(); // Dùng lại
            } else {
                customer = new Customers();
                customer.setName(customerName);
                customer.setPhone(customerPhone);
                customer.setEmail(customerEmail);
                customer.setAddress(customerAddress);
                customer.setNote(customerNote);
                customer.setStatus(1);
                customerId = customerDAO.addCustomer(customer); //để add databae
                if (customerId <= 0) {
                    request.setAttribute("fail", "Không thể tạo thông tin khách hàng.");
                    request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                    request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                    return;
                }
            }

            // Gom nhóm theo warehouse
            Map<Integer, List<Integer>> warehouseToProducts = new HashMap<>();
            Map<String, Integer> quantityMap = new HashMap<>();

            for (int i = 0; i < warehouseIds.length; i++) {
                int warehouseId = Integer.parseInt(warehouseIds[i]);
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                warehouseToProducts.computeIfAbsent(warehouseId, k -> new ArrayList<>()).add(productId);
                quantityMap.put(warehouseId + "_" + productId, quantity);
            }

            // Tạo export_order trước, lưu exportId theo warehouse
            Map<Integer, Integer> warehouseToExportId = new HashMap<>();
            for (int warehouseId : warehouseToProducts.keySet()) {
                int exportId = exportDAO.createExportOrder( //
                        customerId,
                        warehouseId,
                        account.getAccount_id(),
                        reason,
                        note
                );
                if (exportId <= 0) {
                    request.setAttribute("fail", "Không thể tạo đơn hàng cho kho ID: " + warehouseId);
                    request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                    request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                    return;
                }
                warehouseToExportId.put(warehouseId, exportId);
            }

            // Sau đó thêm chi tiết vào export_order_detail
            for (int i = 0; i < productIds.length; i++) {
                int warehouseId = Integer.parseInt(warehouseIds[i]);
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                Inventory inventory = inventoryDAO.getInventoryById(productId, warehouseId);
                if (inventory == null || inventory.getQuantity() < quantity) {
                    request.setAttribute("fail", "Không đủ tồn kho cho sản phẩm ID " + productId + " ở kho " + warehouseId);
                    request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                    request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                    return;
                }

                Products product = productDAO.getProductById(productId);
                if (product == null) {
                    request.setAttribute("fail", "Sản phẩm không tồn tại: ID " + productId);
                    request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                    request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                    return;
                }

                int exportId = warehouseToExportId.get(warehouseId);
                double salePrice = product.getSalePrice().doubleValue();
                detailDAO.insertExportOrderDetail(exportId, productId, quantity, salePrice);
            }

            // Thành công
            request.setAttribute("success", "Đơn hàng xuất kho đã được tạo thành công.");
            doGet(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("fail", "Đã xảy ra lỗi khi xử lý đơn hàng: " + e.getMessage());
            doGet(request, response);
            request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        }
    }

}