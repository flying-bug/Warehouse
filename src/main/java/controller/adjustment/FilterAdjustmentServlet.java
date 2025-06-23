package controller.adjustment;

import dal.AdjustmentDAO;
import dal.ProductDAO;
import dal.WarehouseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Adjustment;
import model.Products;
import model.Warehouses;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "FilterAdjustmentServlet", urlPatterns = {"/filterAdjustment"})
public class FilterAdjustmentServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String ADJUSTMENT_CONTENT = "/views/adjustment/viewAdjustment.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Nhận tham số từ request
        String adjustmentDate = request.getParameter("adjustmentDate");
        String productCode = request.getParameter("productCode");
        String warehouseIdRaw = request.getParameter("warehouseId");

        int warehouseId = 0;
        try {
            warehouseId = Integer.parseInt(warehouseIdRaw);
        } catch (NumberFormatException ignored) {}

        // Build query
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM adjustments a " +
                        "JOIN products p ON a.product_id = p.product_id " +
                        "JOIN warehouses w ON a.warehouse_id = w.warehouse_id WHERE 1=1 ");

        if (adjustmentDate != null && !adjustmentDate.trim().isEmpty()) {
            sql.append(" AND a.adjustment_date LIKE '%").append(adjustmentDate.trim()).append("%' ");
        }
        if (productCode != null && !productCode.trim().isEmpty()) {
            sql.append(" AND p.product_code LIKE '%").append(productCode.trim()).append("%' ");
        }
        if (warehouseId > 0) {
            sql.append(" AND w.warehouse_id = ").append(warehouseId).append(" ");
        }

        // DAO
        AdjustmentDAO adjustmentDAO = new AdjustmentDAO();
        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        // Lấy danh sách theo query
        List<Adjustment> allAdjustments = adjustmentDAO.searchAdjustments(sql.toString());

        // Phân trang
        int defaultPageSize = 10;
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {}
        }

        int totalItems = allAdjustments.size();
        int totalPages = (int) Math.ceil((double) totalItems / defaultPageSize);
        int start = (currentPage - 1) * defaultPageSize;
        int end = Math.min(start + defaultPageSize, totalItems);
        List<Adjustment> paginated = allAdjustments.subList(start, end);

        // Gửi dữ liệu ra view
        request.setAttribute("adjustmentList", paginated);
        request.setAttribute("productList", productDAO.getAllProducts());
        request.setAttribute("warehouseList", warehouseDAO.getAllWarehouses());
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        // Cần giữ lại dữ liệu lọc khi render lại
        request.setAttribute("param.adjustmentDate", adjustmentDate);
        request.setAttribute("param.productCode", productCode);
        request.setAttribute("param.warehouseId", warehouseId);

        // Template include
        request.setAttribute("PAGE_CONTENT", ADJUSTMENT_CONTENT);
        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }
}
