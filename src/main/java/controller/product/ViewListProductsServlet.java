package controller.product;

import dal.ProductDAO;
import dal.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Products;
import model.Suppliers;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewListProductsServlet", urlPatterns = {"/viewListProducts"})
public class ViewListProductsServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String PRODUCT_LIST_PAGE = "/views/products/viewListProducts.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp); // fallback về GET để tái sử dụng logic
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        SupplierDAO supplierDAO = new SupplierDAO();

        // Danh sách nhà cung cấp
        List<Suppliers> suppliers = supplierDAO.getAllSuppliers();
        req.setAttribute("supplierList", suppliers);

        // Phân trang
        int pageSize = 10;
        int currentPage = 1;

        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        int totalProducts = productDAO.getTotalProductCount();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;

        int startIndex = (currentPage - 1) * pageSize;
        List<Products> productList = productDAO.getProductsByPage(startIndex, pageSize);

        // Gán dữ liệu cho request
        req.setAttribute("productList", productList);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("PAGE_CONTENT", PRODUCT_LIST_PAGE);

        req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
    }
}
