package controller.product;

import dal.ProductDAO;
import dal.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Products;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "UpdateProductServlet", urlPatterns = {"/updateProduct"})
public class UpdateProductServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String CONTENT_PAGE = "/views/products/updateProduct.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            ProductDAO productDAO = new ProductDAO();
            Products product = productDAO.getProductById(productId);

            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
                return;
            }

            SupplierDAO supplierDAO = new SupplierDAO();
            request.setAttribute("suppliers", supplierDAO.getAllSuppliers());
            request.setAttribute("p", product);
            request.setAttribute("currentSupplierId", product.getSupplierId());

            request.setAttribute("PAGE_CONTENT", CONTENT_PAGE);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("product_id"));
            String productCode = request.getParameter("product_code").trim();
            String name = request.getParameter("name").trim();
            String description = request.getParameter("description").trim();
            String size = request.getParameter("size").trim();
            String color = request.getParameter("color").trim();
            String material = request.getParameter("material").trim();
            String unit = request.getParameter("unit").trim();
            BigDecimal costPrice = new BigDecimal(request.getParameter("cost_price"));
            BigDecimal salePrice = new BigDecimal(request.getParameter("sale_price"));
            int supplierId = Integer.parseInt(request.getParameter("supplier_id"));

            // Lấy dữ liệu cũ
            ProductDAO productDAO = new ProductDAO();
            Products existingProduct = productDAO.getProductById(productId);
            if (existingProduct == null) {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
                return;
            }

            // Cập nhật dữ liệu
            Products product = new Products();
            product.setProductId(productId);
            product.setProductCode(productCode);
            product.setName(name);
            product.setDescription(description);
            product.setSize(size);
            product.setColor(color);
            product.setMaterial(material);
            product.setUnit(unit);
            product.setCostPrice(costPrice);
            product.setSalePrice(salePrice);
            product.setSupplierId(supplierId);

            boolean updated = productDAO.updateProduct(product);

            if (updated) {
                request.setAttribute("success", "Cập nhật sản phẩm thành công.");
            } else {
                request.setAttribute("fail", "Cập nhật sản phẩm thất bại.");
            }

            // Set lại dữ liệu cho trang JSP
            request.setAttribute("p", product);
            request.setAttribute("currentSupplierId", supplierId);
            request.setAttribute("suppliers", new SupplierDAO().getAllSuppliers());

            request.setAttribute("PAGE_CONTENT", CONTENT_PAGE);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
