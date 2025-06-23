package controller.purchase;

import dal.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet(name = "viewImportOrderDetailServlet", urlPatterns = {"/viewImportOrderDetail"})
public class viewImportOrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String importId = request.getParameter("importId");

        if (importId != null && !importId.trim().isEmpty()) {
            int id = Integer.parseInt(importId);

            // Lấy danh sách chi tiết đơn nhập
            ImportOrderDetailDAO detailDAO = new ImportOrderDetailDAO();
            List<ImportOrderDetails> list = detailDAO.getImportOrderDetails(
                    "SELECT * FROM import_order_details WHERE import_id = " + id);

            // Xử lý thuế và tổng tiền
            BigDecimal totalUntaxed = BigDecimal.ZERO;
            BigDecimal grandTotal = BigDecimal.ZERO;
            Map<BigDecimal, BigDecimal> vatMap = new LinkedHashMap<>();

            for (ImportOrderDetails item : list) {
                BigDecimal lineTotal = item.getCostPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                item.setLineTotal(lineTotal);

                totalUntaxed = totalUntaxed.add(lineTotal);

                if (item.getTaxPercent() != null) {
                    BigDecimal taxAmount = lineTotal.multiply(item.getTaxPercent()).divide(BigDecimal.valueOf(100));
                    vatMap.merge(item.getTaxPercent(), taxAmount, BigDecimal::add);
                }
            }

            for (BigDecimal tax : vatMap.values()) {
                grandTotal = grandTotal.add(tax);
            }
            grandTotal = grandTotal.add(totalUntaxed);

            // Truyền dữ liệu sang JSP
            request.setAttribute("importDetails", list);
            request.setAttribute("totalUntaxed", totalUntaxed);
            request.setAttribute("vatMap", vatMap);
            request.setAttribute("grandTotal", grandTotal);

            // Đơn hàng chính
            ImportOrderDAO orderDAO = new ImportOrderDAO();
            ImportOrders importOrder = orderDAO.getImportOrderById(id);
            request.setAttribute("importOrder", importOrder);
        }

        // Danh sách phụ trợ
        ProductDAO pdao = new ProductDAO();
        request.setAttribute("plist", pdao.getAllProducts());

        SupplierDAO supplierDAO = new SupplierDAO();
        request.setAttribute("suppliersList", supplierDAO.getAllSuppliers());

        WarehouseDAO warehouseDAO = new WarehouseDAO();
        request.setAttribute("warehousesList", warehouseDAO.getAllWarehouses());

        AccountDAO accountDAO = new AccountDAO();
        request.setAttribute("accountsList", accountDAO.getAllAccounts());

        // Truyền đến layout chung
        request.setAttribute("PAGE_CONTENT", "/views/purchases/importOrderDetail.jsp");
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
