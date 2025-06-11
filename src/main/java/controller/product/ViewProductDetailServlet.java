package controller.product;

import dal.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Products;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "ViewProductDetailServlet", urlPatterns = "/viewProductDetail")
public class ViewProductDetailServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ViewProductDetailServlet.class.getName());
    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String PRODUCT_DETAIL_CONTENT = "/views/products/viewProductDetail.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("productId");

        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("fail", "Mã sản phẩm không hợp lệ.");
            forwardWithLayout(req, resp);
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            ProductDAO productDAO = new ProductDAO();
            Products product = productDAO.getProductById(productId);

            if (product == null) {
                req.setAttribute("fail", "Không tìm thấy sản phẩm.");
            } else {
                req.setAttribute("p", product);
            }

            forwardWithLayout(req, resp);
        } catch (NumberFormatException e) {
            LOGGER.severe("Invalid product ID format: " + idParam);
            req.setAttribute("fail", "Định dạng mã sản phẩm không hợp lệ.");
            forwardWithLayout(req, resp);
        } catch (Exception e) {
            LOGGER.severe("Unexpected error while loading product detail: " + e.getMessage());
            req.setAttribute("fail", "Lỗi hệ thống khi tải chi tiết sản phẩm.");
            forwardWithLayout(req, resp);
        }
    }

    private void forwardWithLayout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("PAGE_CONTENT", PRODUCT_DETAIL_CONTENT);
        req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
    }
}
