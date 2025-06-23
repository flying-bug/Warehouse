package controller.supplier;

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

// ProductSuppliersViewServlet.java
@WebServlet("/viewProductSuppliers")
public class ProductSuppliersViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDAO productDAO = new ProductDAO();
        ProductSupplierDAO psDAO = new ProductSupplierDAO();

        List<Products> allProducts = productDAO.getAllProducts();
        request.setAttribute("products", allProducts);

        String productIdRaw = request.getParameter("productId");
        if (productIdRaw != null && !productIdRaw.isEmpty()) {
            try {
                int productId = Integer.parseInt(productIdRaw);
                Products product = productDAO.getProductById(productId);
                List<Suppliers> suppliers = psDAO.getSuppliersByProductId(productId);

                request.setAttribute("selectedProductId", productId);
                request.setAttribute("product", product);
                request.setAttribute("suppliers", suppliers);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid product ID.");
            }
        }

        request.setAttribute("PAGE_CONTENT", "/views/purchases/viewProductSuppliers.jsp");
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SupplierDAO supplierDAO = new SupplierDAO();
        ProductSupplierDAO psDAO = new ProductSupplierDAO();

        // Luôn load toàn bộ danh sách nhà cung cấp để render dropdown
        List<Suppliers> allSuppliers = supplierDAO.getAllSuppliers();
        request.setAttribute("supplierList", allSuppliers);

        String supplierIdRaw = request.getParameter("supplierId");
        if (supplierIdRaw != null && !supplierIdRaw.isEmpty()) {
            try {
                int supplierId = Integer.parseInt(supplierIdRaw);
                Suppliers supplier = supplierDAO.getSupplierById(supplierId);
                List<Products> productList = psDAO.getProductsBySupplierId(supplierId);

                request.setAttribute("supplier", supplier);
                request.setAttribute("products", productList);
                request.setAttribute("selectedSupplierId", supplierId);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid supplier ID.");
            }
        }

        request.setAttribute("PAGE_CONTENT", "/views/suppliers/viewProBySup.jsp");
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }


}
