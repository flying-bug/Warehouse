package controller.adjustment;

import dal.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewAdjustmentServlet", urlPatterns = {"/viewAdjustment"})
public class ViewAdjustmentServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String ADJUSTMENT_CONTENT = "/views/adjustment/viewAdjustment.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Phân trang
        int defaultPageSize = 10;
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // DAO
        AdjustmentDAO adjustmentDAO = new AdjustmentDAO();
        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        AccountDAO accountDAO = new AccountDAO();

        // Dữ liệu
        List<Adjustment> allAdjustments = adjustmentDAO.getAllAdjustments();
        List<Products> productList = productDAO.getAllProducts();
        List<Warehouses> warehouseList = warehouseDAO.getAllWarehouses();
        List<Accounts> accountList = accountDAO.getAllAccounts();


        // Phân trang danh sách điều chỉnh
        int totalItems = allAdjustments.size();
        int totalPages = (int) Math.ceil((double) totalItems / defaultPageSize);
        int startIndex = (currentPage - 1) * defaultPageSize;
        int endIndex = Math.min(startIndex + defaultPageSize, totalItems);
        List<Adjustment> adjustmentList = allAdjustments.subList(startIndex, endIndex);

        // Gửi dữ liệu sang JSP
        request.setAttribute("adjustmentList", adjustmentList);
        request.setAttribute("productList", productList);
        request.setAttribute("warehouseList", warehouseList);
        request.setAttribute("accountList", accountList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("PAGE_CONTENT", ADJUSTMENT_CONTENT);

        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }
}
