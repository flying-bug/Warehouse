package controller.product;

import dal.ProductDAO;
import dal.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Products;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "ViewProductDetailServlet", urlPatterns = {"/viewProductDetail"})
public class ViewProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve product ID from request parameter
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            ProductDAO productDAO = new ProductDAO();
            Products product = productDAO.getProductById(productId);

            int currentSupplierId = product.getSupplierId();
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
                return;
            }

            SupplierDAO supplierDAO = new SupplierDAO();
            request.setAttribute("suppliers", supplierDAO.getAllSuppliers());

            request.setAttribute("p", product);
            request.setAttribute("currentSupplierId", currentSupplierId);
            request.getRequestDispatcher("/views/products/viewProductDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}