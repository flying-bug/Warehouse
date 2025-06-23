package controller.sale;

import dal.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet(name = "viewExportOrderDetailServlet", urlPatterns = {"/viewExportOrderDetail"})
public class viewExportOrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String exportId = request.getParameter("exportId");

        if (exportId != null && !exportId.trim().isEmpty()) {
            int id = Integer.parseInt(exportId);

            // Lấy chi tiết đơn hàng
            ExportOrderDetailDAO detailDAO = new ExportOrderDetailDAO();
            List<ExportOrderDetails> list = detailDAO.getExportOrderDetails(
                    "SELECT * FROM export_order_details WHERE export_id = " + id);

            // Tính tổng tiền, thuế động theo tax_percent
            BigDecimal totalUntaxed = BigDecimal.ZERO;
            BigDecimal grandTotal = BigDecimal.ZERO;
            Map<BigDecimal, BigDecimal> vatMap = new LinkedHashMap<>();

            for (ExportOrderDetails item : list) {
                BigDecimal lineTotal = item.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                item.setLineTotal(lineTotal); // ✅ Gán lineTotal cho từng sản phẩm

                totalUntaxed = totalUntaxed.add(lineTotal);

                if (item.getTaxPercent() != null) {
                    BigDecimal taxAmount = lineTotal.multiply(item.getTaxPercent()).divide(BigDecimal.valueOf(100));
                    vatMap.merge(item.getTaxPercent(), taxAmount, BigDecimal::add);
                }
            }

            // Tính tổng tiền cuối cùng
            for (BigDecimal tax : vatMap.values()) {
                grandTotal = grandTotal.add(tax);
            }
            grandTotal = grandTotal.add(totalUntaxed);

            // Gửi dữ liệu ra JSP
            request.setAttribute("list", list);
            request.setAttribute("totalUntaxed", totalUntaxed);
            request.setAttribute("grandTotal", grandTotal);
            request.setAttribute("vatMap", vatMap);

            // Lấy thông tin đơn hàng chính
            ExportOrderDAO orderDAO = new ExportOrderDAO();
            ExportOrders exportOrder = orderDAO.getExportOrderById(id);
            request.setAttribute("exportOrder", exportOrder);
        }

        // Các danh sách phụ trợ
        ProductDAO productDAO = new ProductDAO();
        request.setAttribute("plist", productDAO.getAllProducts());

        CustomerDAO customerDAO = new CustomerDAO();
        request.setAttribute("customersList", customerDAO.getAllCustomers());

        WarehouseDAO warehouseDAO = new WarehouseDAO();
        request.setAttribute("warehousesList", warehouseDAO.getAllWarehouses());

        AccountDAO accountDAO = new AccountDAO();
        request.setAttribute("accountsList", accountDAO.getAllAccounts());

        // Forward đến dashboard layout
        request.setAttribute("PAGE_CONTENT", "/views/sales/viewExportOrderDetail.jsp");
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
