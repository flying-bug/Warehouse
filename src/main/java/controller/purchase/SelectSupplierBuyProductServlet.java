package controller.purchase;

import dal.*;
import model.Products;
import model.Suppliers;
import model.Warehouses;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SelectSupplierBuyProductServlet", urlPatterns = {"/selectSupplier"})
public class SelectSupplierBuyProductServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String SELECT_SUPPLIER_PAGE = "/views/purchases/selectSupplier.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SupplierDAO supplierDAO = new SupplierDAO();
        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        // Lấy danh sách nhà cung cấp
        List<Suppliers> supplierList = supplierDAO.getAllSuppliers();
        request.setAttribute("supplierList", supplierList);

        // Lấy danh sách sản phẩm đang hoạt động
        List<Products> productList = productDAO.getAllProducts(); // Nếu có field `status`, bạn có thể lọc ở đây
        request.setAttribute("productList", productList);

        // Lấy danh sách kho
        List<Warehouses> warehouseList = warehouseDAO.getAllWarehouses();
        request.setAttribute("warehouseList", warehouseList);

        // Gắn layout
        request.setAttribute("PAGE_CONTENT", SELECT_SUPPLIER_PAGE);
        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        SupplierDAO supplierDAO = new SupplierDAO();
        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        ImportOrderDAO importDAO = new ImportOrderDAO();
        ImportOrderDetailDAO detailDAO = new ImportOrderDetailDAO();
        ProductSupplierDAO productSupplierDAO = new ProductSupplierDAO();

        final String CREATE_PAGE = "/views/purchases/selectSupplier.jsp";
        final String LAYOUT_PAGE = "/views/dashboard.jsp";

        // Đưa lại danh sách nếu lỗi
        request.setAttribute("supplierList", supplierDAO.getAllSuppliers());
        request.setAttribute("productList", productDAO.getAllProducts());
        request.setAttribute("warehouseList", warehouseDAO.getAllWarehouses());

        try {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            int warehouseId = Integer.parseInt(request.getParameter("warehouseId"));
            String note = request.getParameter("note");

            String[] productIds = request.getParameterValues("productIds[]");
            String[] quantities = request.getParameterValues("quantities[]");

            if (productIds == null || quantities == null || productIds.length != quantities.length) {
                request.setAttribute("fail", "Danh sách sản phẩm không hợp lệ.");
                request.setAttribute("PAGE_CONTENT", CREATE_PAGE);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            model.Accounts account = (model.Accounts) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // ✅ Tính tổng tiền
            double totalCost = 0;
            for (int i = 0; i < productIds.length; i++) {
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                Double cost = productSupplierDAO.getEstimatedPrice(productId, supplierId);
                if (cost != null) {
                    totalCost += cost * quantity;
                }
            }

            // ✅ Tạo đơn nhập với total_cost
            int importId = importDAO.createImportOrder(supplierId, warehouseId, account.getAccount_id(), note, totalCost);

            if (importId <= 0) {
                request.setAttribute("fail", "Không thể tạo đơn nhập kho.");
                request.setAttribute("PAGE_CONTENT", CREATE_PAGE);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            // ✅ Thêm chi tiết đơn nhập
            for (int i = 0; i < productIds.length; i++) {
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                Double cost = productSupplierDAO.getEstimatedPrice(productId, supplierId);
                if (cost != null) {
                    detailDAO.insertImportDetail(importId, productId, quantity, cost);   //hàm
                }
            }

            request.setAttribute("success", "Đơn nhập kho đã được tạo thành công.");
            doGet(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("fail", "Lỗi xử lý đơn nhập: " + e.getMessage());
            request.setAttribute("PAGE_CONTENT", CREATE_PAGE);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        }
    }

}
