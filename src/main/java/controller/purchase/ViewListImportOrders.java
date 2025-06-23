package controller.purchase;

import dal.AccountDAO;
import dal.ImportOrderDAO;
import dal.SupplierDAO;
import dal.WarehouseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Accounts;
import model.ImportOrders;
import model.Suppliers;
import model.Warehouses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ViewListImportOrders", urlPatterns = {"/viewListImportOrders"})
public class ViewListImportOrders extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ===== Nhận các tham số lọc =====
        String selectW = req.getParameter("selectW");
        String selectS = req.getParameter("selectS");
        String selectA = req.getParameter("selectA");
        String selectStatus = req.getParameter("selectStatus");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");

        if (selectW == null) selectW = "all";
        if (selectS == null) selectS = "all";
        if (selectA == null) selectA = "all";
        if (selectStatus == null) selectStatus = "all";

        // ===== Phân trang =====
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

        // ===== Xây dựng điều kiện truy vấn động =====
        List<String> conditions = new ArrayList<>();
        if (!"all".equals(selectW)) conditions.add("warehouse_id = " + selectW);
        if (!"all".equals(selectS)) conditions.add("supplier_id = " + selectS);
        if (!"all".equals(selectA)) conditions.add("account_id = " + selectA);
        if (!"all".equals(selectStatus)) conditions.add("status = '" + selectStatus + "'");
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            conditions.add("import_date >= '" + fromDate + "'");
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            conditions.add("import_date <= '" + toDate + "'");
        }

        String whereClause = "";
        if (!conditions.isEmpty()) {
            whereClause = " WHERE " + String.join(" AND ", conditions);
        }

        String baseQuery = "SELECT * FROM import_orders" + whereClause;
        String countCondition = whereClause.replace("WHERE", "").trim();

        // ===== Truy vấn đơn hàng nhập =====
        ImportOrderDAO importOrderDAO = new ImportOrderDAO();
        List<ImportOrders> importOrderList = importOrderDAO.getImportOrdersPaginated(baseQuery, offset, pageSize);
        int totalRecords = importOrderDAO.countImportOrders(countCondition);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // ===== Danh sách phụ trợ =====
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        List<Warehouses> warehousesList = warehouseDAO.getAllWarehouses();

        SupplierDAO supplierDAO = new SupplierDAO();
        List<Suppliers> suppliersList = supplierDAO.getAllSuppliers();

        AccountDAO accountDAO = new AccountDAO();
        List<Accounts> accountsList = accountDAO.getAllAccounts();

        // ===== Truyền sang JSP =====
        req.setAttribute("importOrderList", importOrderList);
        req.setAttribute("warehousesList", warehousesList);
        req.setAttribute("suppliersList", suppliersList);
        req.setAttribute("accountsList", accountsList);

        req.setAttribute("selectW", selectW);
        req.setAttribute("selectS", selectS);
        req.setAttribute("selectA", selectA);
        req.setAttribute("selectStatus", selectStatus);
        req.setAttribute("fromDate", fromDate);
        req.setAttribute("toDate", toDate);

        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("PAGE_CONTENT", "/views/purchases/listImportOrders.jsp");
        req.getRequestDispatcher("/views/dashboard.jsp").forward(req, resp);
    }
}
