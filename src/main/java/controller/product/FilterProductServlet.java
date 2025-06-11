package controller.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.ProductDAO;
import model.Products;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "FilterProductServlet", urlPatterns = "/filterListProducts")
public class FilterProductServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(FilterProductServlet.class.getName());
    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String PRODUCT_LIST_PAGE = "/views/products/viewListProducts.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productCode = request.getParameter("productCode");
        String material = request.getParameter("material");
        String costPriceFromStr = request.getParameter("costPrice"); // Lower bound
        String costPriceToStr = request.getParameter("salePrice");   // Upper bound
        String statusStr = request.getParameter("status");
        String pageStr = request.getParameter("page");

        BigDecimal costPriceFrom = null;
        BigDecimal costPriceTo = null;
        Integer status = null;
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int pageSize = 10; // Adjust as needed

        try {
            if (costPriceFromStr != null && !costPriceFromStr.isEmpty()) {
                costPriceFrom = new BigDecimal(costPriceFromStr);
            }
            if (costPriceToStr != null && !costPriceToStr.isEmpty()) {
                costPriceTo = new BigDecimal(costPriceToStr);
            }
            if (statusStr != null && !statusStr.isEmpty()) {
                status = Integer.parseInt(statusStr);
            }
        } catch (NumberFormatException e) {
            LOGGER.severe("Invalid numeric input: " + e.getMessage());
            request.setAttribute("fail", "Invalid price or status format.");
        }

        ProductDAO dao = new ProductDAO();
        int startIndex = (page - 1) * pageSize;
        List<Products> productList = dao.getFilteredProductsByPage(productCode, material, costPriceFrom, costPriceTo, startIndex, pageSize, status);
        int totalProducts = dao.getFilteredProductsCount(productCode, material, costPriceFrom, costPriceTo, status);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        request.setAttribute("productList", productList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Set layout
        request.setAttribute("PAGE_CONTENT", PRODUCT_LIST_PAGE);
        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }
}