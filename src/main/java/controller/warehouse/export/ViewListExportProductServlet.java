package controller.warehouse.export;

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

@WebServlet(name = "ViewListExportProductServlet", urlPatterns = {"/viewListExportProducts"})
public class ViewListExportProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy các tham số lọc
        String selectW = req.getParameter("selectW");
        String selectC = req.getParameter("selectC");
        String selectS = req.getParameter("selectS");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");

        if (selectW == null) selectW = "all"; else selectW = selectW.trim();
        if (selectC == null) selectC = "all"; else selectC = selectC.trim();
        if (selectS == null) selectS = "all"; else selectS = selectS.trim();
        if (fromDate != null) fromDate = fromDate.trim();
        if (toDate != null) toDate = toDate.trim();

        int page = 1;
        int pageSize = 10;

        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) page = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            page = 1;
        }

        int offset = (page - 1) * pageSize;

        // Xây dựng điều kiện lọc
        List<String> conditions = new ArrayList<>();
        try {
            if (!"all".equals(selectW)) {
                int warehouseId = Integer.parseInt(selectW);
                conditions.add("warehouse_id = " + warehouseId);
            }
            if (!"all".equals(selectC)) {
                int customerId = Integer.parseInt(selectC);
                conditions.add("customer_id = " + customerId);
            }
        } catch (NumberFormatException e) {
            throw new ServletException("Giá trị lọc không hợp lệ", e);
        }

        if (!"all".equals(selectS)) {
            conditions.add("export_status = '" + selectS + "'");
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            conditions.add("export_date >= '" + fromDate + "'");
        }
        if (toDate != null && !toDate.isEmpty()) {
            conditions.add("export_date <= '" + toDate + "'");
        }

        String whereClause = "";
        if (!conditions.isEmpty()) {
            whereClause = " WHERE " + String.join(" AND ", conditions);
        }

        String baseQuery = "SELECT * FROM export_orders" + whereClause;
        String countCondition = whereClause.replace("WHERE", "").trim();

        ExportOrderDAO exportOrderDAO = new ExportOrderDAO();
        List<ExportOrders> exportOrderList = new ArrayList<>();
        int totalRecords = 0;

        try {
            exportOrderList = exportOrderDAO.getExportOrderPaginated(baseQuery, offset, pageSize);
            totalRecords = exportOrderDAO.countExportOrders(countCondition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi truy vấn danh sách phiếu xuất kho", e);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Lấy dữ liệu liên quan (kho, khách hàng, tài khoản)
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();

        List<Warehouses> warehousesList = warehouseDAO.getAllWarehouses();
        List<Customers> customersList = customerDAO.getAllCustomers();
        List<Accounts> accountsList = accountDAO.getAllAccounts();

        // Gán dữ liệu ra view
        req.setAttribute("exportOrderList", exportOrderList);
        req.setAttribute("warehousesList", warehousesList);
        req.setAttribute("customersList", customersList);
        req.setAttribute("accountsList", accountsList);

        req.setAttribute("selectW", selectW);
        req.setAttribute("selectC", selectC);
        req.setAttribute("selectS", selectS);
        req.setAttribute("fromDate", fromDate);
        req.setAttribute("toDate", toDate);

        req.setAttribute("startRecord", offset + 1);
        req.setAttribute("endRecord", Math.min(offset + pageSize, totalRecords));
        req.setAttribute("totalRecords", totalRecords);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("PAGE_CONTENT", "/views/warehouses/confirmExportProduct.jsp");
        req.getRequestDispatcher("/views/dashboard.jsp").forward(req, resp);
    }
}
