package controller;

import dal.ProductDAO;
import dal.SupplierDAO;
import dal.ProductSupplierDAO;
import model.Products;
import model.Suppliers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/productSupplier")
public class ProductSupplierServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDAO productDAO = new ProductDAO();
        SupplierDAO supplierDAO = new SupplierDAO();

        List<Products> products = productDAO.getAllProducts();
        List<Suppliers> suppliers = supplierDAO.getAllSuppliers();

        request.setAttribute("products", products);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("PAGE_CONTENT", "/views/suppliers/addProductSupplier.jsp");
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("productId"));
        int supplierId = Integer.parseInt(request.getParameter("supplierId"));
        int deliveryDuration = Integer.parseInt(request.getParameter("deliveryDuration"));
        double estimatedPrice = Double.parseDouble(request.getParameter("estimatedPrice"));
        String policies = request.getParameter("policies");

        ProductSupplierDAO dao = new ProductSupplierDAO();

        if (dao.exists(productId, supplierId)) {
            // Đã tồn tại liên kết → chuyển về lại với thông báo lỗi
            response.sendRedirect("productSupplier?error=exists");
        } else {
            dao.insertProductSupplier(productId, supplierId, deliveryDuration, estimatedPrice, policies);
            response.sendRedirect("productSupplier?success=true");
        }
    }

}
