package controller.sale;

import dal.AccountDAO;
import dal.CustomerDAO;
import dal.ExportOrderDAO;
import dal.WarehouseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Accounts;
import model.Customers;
import model.ExportOrders;
import model.Warehouses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ViewListRequestProducts", urlPatterns = {"/viewListRequestProducts"})
public class ViewListRequestProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy các tham số lọc từ request
        String selectW = req.getParameter("selectW");
        String selectA = req.getParameter("selectA");
        String selectC = req.getParameter("selectC");
        String selectS = req.getParameter("selectS");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");


        if (selectW == null) selectW = "all"; else selectW = selectW.trim();
        if (selectA == null) selectA = "all"; else selectA = selectA.trim();
        if (selectC == null) selectC = "all"; else selectC = selectC.trim();
        if (selectS == null) selectS = "all"; else selectS = selectS.trim();
        if (fromDate != null) fromDate = fromDate.trim();
        if (toDate != null) toDate = toDate.trim();

        int page = 1;
        int pageSize = 10;

        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        int offset = (page - 1) * pageSize;

        // Tạo điều kiện WHERE động
        List<String> conditions = new ArrayList<>();
        try {
            if (!"all".equals(selectW)) {
                int warehouseId = Integer.parseInt(selectW);
                conditions.add("warehouse_id = " + warehouseId);
            }
            if (!"all".equals(selectA)) {
                int accountId = Integer.parseInt(selectA);
                conditions.add("account_id = " + accountId);
            }
            if (!"all".equals(selectC)) {
                int customerId = Integer.parseInt(selectC);
                conditions.add("customer_id = " + customerId);
            }
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid filter value (must be numeric)", e);
        }

        if (!"all".equals(selectS)) {
            conditions.add("export_status = '" + selectS + "'");
        }
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            conditions.add("export_date >= '" + fromDate + "'");
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            conditions.add("export_date <= '" + toDate + "'");
        }

        String whereClause = "";
        if (!conditions.isEmpty()) {
            whereClause = " WHERE " + String.join(" AND ", conditions);
        }

        String baseQuery = "SELECT * FROM export_orders" + whereClause;
        String countCondition = whereClause.replace("WHERE", "").trim();

        // Lấy dữ liệu đơn hàng + phân trang
        ExportOrderDAO exportOrderDAO = new ExportOrderDAO();
        List<ExportOrders> exportOrderList = new ArrayList<>();
        int totalRecords = 0;

        try {
            exportOrderList = exportOrderDAO.getExportOrderPaginated(baseQuery, offset, pageSize);
            totalRecords = exportOrderDAO.countExportOrders(countCondition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi truy vấn đơn hàng xuất", e);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Lấy dữ liệu hỗ trợ (kho, tài khoản, khách hàng)
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        AccountDAO accountDAO = new AccountDAO();
        CustomerDAO customerDAO = new CustomerDAO();

        List<Warehouses> warehousesList = warehouseDAO.getAllWarehouses();
        List<Accounts> accountsList = accountDAO.getAllAccounts();
        List<Customers> customersList = customerDAO.getAllCustomers();

        req.setAttribute("startRecord", offset + 1);
        req.setAttribute("endRecord", Math.min(offset + pageSize, totalRecords));
        req.setAttribute("totalRecords", totalRecords);


        // Truyền dữ liệu sang JSP
        req.setAttribute("exportOrderList", exportOrderList);
        req.setAttribute("warehousesList", warehousesList);
        req.setAttribute("accountsList", accountsList);
        req.setAttribute("customersList", customersList);

        req.setAttribute("selectW", selectW);
        req.setAttribute("selectA", selectA);
        req.setAttribute("selectC", selectC);
        req.setAttribute("selectS", selectS);
        req.setAttribute("fromDate", fromDate);
        req.setAttribute("toDate", toDate);

        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("PAGE_CONTENT", "/views/sales/viewListRequestProduct.jsp");
        req.getRequestDispatcher("/views/dashboard.jsp").forward(req, resp);
    }
}
