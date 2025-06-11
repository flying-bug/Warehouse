package controller.product;

import dal.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "DeleteProductServlet", urlPatterns = {"/deleteProduct"})
public class DeleteProductServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DeleteProductServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Not implemented for now. Status update is handled via GET for simplicity.
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("productId"); // Changed from "id" to "productId" to match previous usage
        ProductDAO productDAO = new ProductDAO();

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                boolean updated = productDAO.updateProductStatus(id, 0); // Update status to inactive (0)

                if (updated) {
                    request.setAttribute("success", "Sản phẩm đã được đặt thành trạng thái không hoạt động.");
                } else {
                    request.setAttribute("fail", "Không thể cập nhật trạng thái sản phẩm.");
                }
            } catch (NumberFormatException e) {
                LOGGER.severe("Invalid product ID format: " + e.getMessage());
                request.setAttribute("fail", "Mã sản phẩm không hợp lệ.");
            }

            // Redirect back to the product list page
            request.getRequestDispatcher("/viewListProducts").forward(request, response);
        } else {
            request.setAttribute("fail", "Mã sản phẩm bị thiếu.");
            request.getRequestDispatcher("/viewListProducts").forward(request, response);
        }
    }
}