package controller.product;

import dal.ProductDAO;
import dal.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Products;
import model.Suppliers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "AddProductServlet", urlPatterns = {"/addProduct"})
public class AddProductServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String ADD_PRODUCT_CONTENT = "/views/products/addProduct.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SupplierDAO supplierDAO = new SupplierDAO();
        List<Suppliers> suppliers = supplierDAO.getAllSuppliers();
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        SupplierDAO supplierDAO = new SupplierDAO();
        List<Suppliers> suppliers = supplierDAO.getAllSuppliers();
        request.setAttribute("suppliers", suppliers);  // luôn cần lại danh sách

        String productCode = request.getParameter("product_code");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String size = request.getParameter("size");
        String color = request.getParameter("color");
        String material = request.getParameter("material");
        String unit = request.getParameter("unit");
        String costPriceStr = request.getParameter("cost_price");
        String salePriceStr = request.getParameter("sale_price");
        String supplierIdStr = request.getParameter("supplier_id");

        try {
            // Validate input
            if (isBlank(productCode) || isBlank(name) || isBlank(unit) ||
                    isBlank(costPriceStr) || isBlank(salePriceStr) || isBlank(supplierIdStr)) {
                request.setAttribute("fail", "Please fill in all required fields.");
                setAttributesForFormValues(request, productCode, name, description, size, color, material, unit, costPriceStr, salePriceStr, supplierIdStr);
                request.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            // Convert and validate prices
            BigDecimal costPrice = new BigDecimal(costPriceStr);
            BigDecimal salePrice = new BigDecimal(salePriceStr);
            if (costPrice.compareTo(BigDecimal.ZERO) <= 0 || salePrice.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("fail", "Prices must be greater than 0.");
                setAttributesForFormValues(request, productCode, name, description, size, color, material, unit, costPriceStr, salePriceStr, supplierIdStr);
                request.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            if (salePrice.compareTo(costPrice) <= 0) {
                request.setAttribute("fail", "Sale price must be greater than cost price.");
                setAttributesForFormValues(request, productCode, name, description, size, color, material, unit, costPriceStr, salePriceStr, supplierIdStr);
                request.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            // Create product object
            Products product = new Products();
            product.setProductCode(productCode.trim());
            product.setName(name.trim());
            product.setDescription(description != null ? description.trim() : "");
            product.setSize(size != null ? size.trim() : "");
            product.setColor(color != null ? color.trim() : "");
            product.setMaterial(material != null ? material.trim() : "");
            product.setUnit(unit.trim());
            product.setCostPrice(costPrice);
            product.setSalePrice(salePrice);
            product.setSupplierId(Integer.parseInt(supplierIdStr.trim()));

            // Add product
            ProductDAO productDAO = new ProductDAO();
            boolean success = productDAO.addProduct(product);

            if (success) {
                request.setAttribute("success", "Product added successfully.");
                // reset form
                doGet(request, response); // clear form
            } else {
                setAttributesForFormValues(request, productCode, name, description, size, color, material, unit, costPriceStr, salePriceStr, supplierIdStr);
                request.setAttribute("fail", "Failed to add product.");
                request.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            setAttributesForFormValues(request, productCode, name, description, size, color, material, unit, costPriceStr, salePriceStr, supplierIdStr);
            request.setAttribute("fail", e.getMessage());
            request.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void setAttributesForFormValues(HttpServletRequest request,
                                            String productCode, String name, String description, String size,
                                            String color, String material, String unit,
                                            String costPrice, String salePrice, String supplierId) {
        request.setAttribute("product_code", productCode);
        request.setAttribute("name", name);
        request.setAttribute("description", description);
        request.setAttribute("size", size);
        request.setAttribute("color", color);
        request.setAttribute("material", material);
        request.setAttribute("unit", unit);
        request.setAttribute("cost_price", costPrice);
        request.setAttribute("sale_price", salePrice);
        request.setAttribute("supplier_id", supplierId);
    }
}