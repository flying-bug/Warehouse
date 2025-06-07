package controller.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import dal.ProductDAO;
import dal.SupplierDAO;
import model.Products;
import model.Suppliers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "FilterProductServlet", urlPatterns = {"/filterListProducts"})
public class FilterProductServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String PRODUCT_LIST_PAGE = "/views/products/viewListProducts.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productCode = request.getParameter("productCode");
        String material = request.getParameter("material");
        String costPriceStr = request.getParameter("costPrice");
        String salePriceStr = request.getParameter("salePrice");
        String supplierIdStr = request.getParameter("supplierId");

        BigDecimal costPrice = null, salePrice = null;
        Integer supplierId = null;

        try {
            if (costPriceStr != null && !costPriceStr.isEmpty()) {
                costPrice = new BigDecimal(costPriceStr);
            }
            if (salePriceStr != null && !salePriceStr.isEmpty()) {
                salePrice = new BigDecimal(salePriceStr);
            }
            if (supplierIdStr != null && !supplierIdStr.isEmpty()) {
                supplierId = Integer.parseInt(supplierIdStr);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Có thể log nếu cần
        }

        ProductDAO dao = new ProductDAO();
        List<Products> productList = dao.filterProducts(productCode, material, costPrice, salePrice, supplierId);

        SupplierDAO supplierDAO = new SupplierDAO();
        List<Suppliers> supplierList = supplierDAO.getAllSuppliers();

        request.setAttribute("productList", productList);
        request.setAttribute("supplierList", supplierList);

        // Thêm phân trang giả định nếu bạn cần phân trang tĩnh
        request.setAttribute("currentPage", 1);
        request.setAttribute("totalPages", 1);

        // Gán layout
        request.setAttribute("PAGE_CONTENT", PRODUCT_LIST_PAGE);
        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }
}
