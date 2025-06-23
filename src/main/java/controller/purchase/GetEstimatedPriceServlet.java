package controller.purchase;

import dal.ProductSupplierDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/getEstimatedPrice")
public class GetEstimatedPriceServlet extends HttpServlet {
    private final ProductSupplierDAO dao = new ProductSupplierDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));

            // Kiểm tra có mối liên kết không
            boolean isLinked = dao.supplierProvidesProduct(productId, supplierId);
            response.setContentType("application/json");

            if (!isLinked) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Sản phẩm không được cung cấp bởi nhà cung cấp đã chọn.\"}");
                return;
            }

            Double price = dao.getEstimatedPrice(productId, supplierId);
            response.getWriter().write("{\"estimatedPrice\": " + (price != null ? price : 0) + "}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Lỗi xử lý giá sản phẩm.\"}");
            e.printStackTrace();
        }
    }
}
