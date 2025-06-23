package controller.warehouse;
import dal.StockCheckDAO;
import dal.AccountDAO;
import dal.WarehouseDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Accounts;
import model.StockCheck;
import model.Warehouses;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ListStockChecksServlet", urlPatterns = {"/listStockChecks"})
public class ListStockChecksServlet extends HttpServlet {

    private static final String DASHBOARD_PAGE = "/views/dashboard.jsp";
    private static final String LIST_PAGE = "/views/warehouses/listStockCheck.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            StockCheckDAO stockCheckDAO = new StockCheckDAO();
            WarehouseDAO warehouseDAO = new WarehouseDAO();
            AccountDAO accountDAO = new AccountDAO();

            List<StockCheck> stockChecks = stockCheckDAO.getAllStockChecks();
            Map<Integer, Warehouses> warehouseMap = warehouseDAO.getWarehouseMap();
            Map<Integer, Accounts> accountMap = accountDAO.getAllAccountsMap();

            request.setAttribute("stockCheckList", stockChecks);
            request.setAttribute("warehouseMap", warehouseMap);
            request.setAttribute("accountMap", accountMap);
            request.setAttribute("PAGE_CONTENT", LIST_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể lấy danh sách kiểm kê.");
        }

        request.getRequestDispatcher(DASHBOARD_PAGE).forward(request, response);
    }
}
