package controller.warehouse;

import dal.InventoryDAO;
import dal.ProductDAO;
import dal.StockCheckDAO;
import dal.WarehouseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Accounts;
import model.Inventory;
import model.Products;
import model.StockCheck;
import model.StockCheckDetail;
import model.Warehouses;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet(name = "StockCheckServlet", urlPatterns = {"/stockCheck"})
public class StockCheckServlet extends HttpServlet {

    private static final String DASHBOARD_PAGE = "/views/dashboard.jsp";
    private static final String STOCK_CHECK_PAGE = "/views/warehouses/stockCheck.jsp";
    private static final String SELECT_WAREHOUSE_PAGE = "/views/warehouses/stockCheck.jsp";
    private static final String ERROR_PAGE = "/views/error.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InventoryDAO inventoryDAO = new InventoryDAO();
        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        List<Warehouses> warehouseList = warehouseDAO.getAllWarehouses();
        request.setAttribute("warehouseList", warehouseList);

        String warehouseIdStr = request.getParameter("warehouseId");
        if (warehouseIdStr == null || warehouseIdStr.isEmpty()) {
            request.setAttribute("PAGE_CONTENT", SELECT_WAREHOUSE_PAGE);
            request.getRequestDispatcher(DASHBOARD_PAGE).forward(request, response);
            return;
        }

        try {
            int warehouseId = Integer.parseInt(warehouseIdStr);

            List<Inventory> inventoryList = inventoryDAO.getInventoryByWarehouse(warehouseId);
            Map<Integer, Products> productMap = productDAO.getAllProductsMap();
            Warehouses warehouse = warehouseDAO.getWarehouseById(warehouseId);
            request.setAttribute("warehouseId", warehouseId);
            request.setAttribute("inventoryList", inventoryList);
            request.setAttribute("productMap", productMap);
            request.setAttribute("warehouse", warehouse);
            request.setAttribute("PAGE_CONTENT", STOCK_CHECK_PAGE);
            request.getRequestDispatcher(DASHBOARD_PAGE).forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID kho không hợp lệ.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String startDateStr = request.getParameter("checkStartDate");
        String endDateStr = request.getParameter("checkEndDate");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

        Timestamp sqlStart = Timestamp.valueOf(startDate);
        Timestamp sqlEnd = Timestamp.valueOf(endDate);

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Accounts acc = (Accounts) session.getAttribute("account");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int warehouseId = Integer.parseInt(request.getParameter("warehouseId"));
            String[] productIds = request.getParameterValues("productIds[]");
            String[] actualQuantities = request.getParameterValues("actualQuantities[]");
            String note = request.getParameter("note");

            StockCheckDAO stockCheckDAO = new StockCheckDAO();

            StockCheck stockCheck = new StockCheck();
            stockCheck.setWarehouseId(warehouseId);
            stockCheck.setAccountId(acc.getAccount_id());
            stockCheck.setCheckDate(sqlStart);
            stockCheck.setEndDate(sqlEnd);
            stockCheck.setStatus("Scheduled");
            stockCheck.setNote(note);

            int checkId = stockCheckDAO.insertStockCheck(stockCheck);
            if (checkId > 0) {
                for (int i = 0; i < productIds.length; i++) {
                    int pid = Integer.parseInt(productIds[i]);
                    int actualQty = Integer.parseInt(actualQuantities[i]);
                    int systemQty = stockCheckDAO.getSystemQuantity(pid, warehouseId);

                    StockCheckDetail detail = new StockCheckDetail();
                    detail.setCheckId(checkId);
                    detail.setProductId(pid);
                    detail.setActualQuantity(actualQty);
                    detail.setSystemQuantity(systemQty);

                    stockCheckDAO.insertStockCheckDetail(detail);
                }
                request.getSession().setAttribute("success", "✔️ Kiểm kê đã được lưu thành công.");
            } else {
                request.getSession().setAttribute("fail", "❌ Không thể lưu kiểm kê.");
            }

            response.sendRedirect(request.getContextPath() + "/stockCheck?warehouseId=" + warehouseId);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("fail", "⚠️ Lỗi trong quá trình xử lý kiểm kê.");
            response.sendRedirect(request.getContextPath() );

        }
    }
}
