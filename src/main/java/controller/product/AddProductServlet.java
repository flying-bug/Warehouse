package controller.product;

import dal.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Products;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

@WebServlet(name = "AddProductServlet", urlPatterns = "/addProduct")
public class AddProductServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddProductServlet.class.getName());
    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String ADD_PRODUCT_CONTENT = "/views/products/addProduct.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
        req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // Extract form data
        String productCode = req.getParameter("product_code");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String size = req.getParameter("size");
        String color = req.getParameter("color");
        String material = req.getParameter("material");
        String unit = req.getParameter("unit");
        String costPriceStr = req.getParameter("cost_price");
        String salePriceStr = req.getParameter("sale_price");
        String statusStr = req.getParameter("status");
        String image = req.getParameter("image");
        String minStockLevelStr = req.getParameter("min_stock_level");

        try {
            // Validate required fields
            if (isBlank(productCode) || isBlank(name) || isBlank(unit) ||
                    isBlank(costPriceStr) || isBlank(salePriceStr) || isBlank(statusStr) || isBlank(minStockLevelStr)) {
                req.setAttribute("fail", "Vui lòng điền đầy đủ các trường bắt buộc.");
                setAttributesForFormValues(req, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            // Parse and validate numeric fields
            BigDecimal costPrice = new BigDecimal(costPriceStr);
            BigDecimal salePrice = new BigDecimal(salePriceStr);
            int status = Integer.parseInt(statusStr);
            int minStockLevel = Integer.parseInt(minStockLevelStr);

            // Validate business rules
            if (costPrice.compareTo(BigDecimal.ZERO) <= 0 || salePrice.compareTo(BigDecimal.ZERO) <= 0) {
                req.setAttribute("fail", "Giá vốn và giá bán phải lớn hơn 0.");
                setAttributesForFormValues(req, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            if (salePrice.compareTo(costPrice) <= 0) {
                req.setAttribute("fail", "Giá bán phải lớn hơn giá vốn.");
                setAttributesForFormValues(req, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            if (minStockLevel < 0) {
                req.setAttribute("fail", "Mức tồn kho tối thiểu phải lớn hơn hoặc bằng 0.");
                setAttributesForFormValues(req, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            // Check for duplicate product code
            ProductDAO productDAO = new ProductDAO();
            Products existingProduct = productDAO.getProductByCode(productCode.trim());
            if (existingProduct != null) {
                req.setAttribute("fail", "Mã sản phẩm đã tồn tại. Vui lòng chọn mã khác.");
                setAttributesForFormValues(req, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            // Create product object
            Products product = new Products(
                    0, // productId auto-generated
                    productCode.trim(),
                    name.trim(),
                    description != null ? description.trim() : "",
                    size != null ? size.trim() : "",
                    color != null ? color.trim() : "",
                    material != null ? material.trim() : "",
                    unit.trim(),
                    costPrice,
                    salePrice,
                    status,
                    image != null ? image.trim() : null,
                    minStockLevel
            );

            // Save product
            boolean success = productDAO.addProduct(product);

            if (success) {
                req.setAttribute("success", "Thêm sản phẩm thành công.");
                resp.sendRedirect(req.getContextPath() + "/viewListProducts");
            } else {
                req.setAttribute("fail", "Thêm sản phẩm thất bại. Vui lòng thử lại.");
                setAttributesForFormValues(req, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
            }

        } catch (NumberFormatException e) {
            LOGGER.severe("Invalid numeric input: " + e.getMessage());
            req.setAttribute("fail", "Dữ liệu số không hợp lệ (giá, trạng thái, hoặc mức tồn kho).");
            setAttributesForFormValues(req, productCode, name, description, size, color, material, unit,
                    costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
            req.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
            req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
        } catch (Exception e) {
            LOGGER.severe("Error adding product: " + e.getMessage());
            req.setAttribute("fail", "Lỗi hệ thống. Vui lòng thử lại sau.");
            setAttributesForFormValues(req, productCode, name, description, size, color, material, unit,
                    costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
            req.setAttribute("PAGE_CONTENT", ADD_PRODUCT_CONTENT);
            req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void setAttributesForFormValues(HttpServletRequest req,
                                            String productCode, String name, String description, String size,
                                            String color, String material, String unit,
                                            String costPrice, String salePrice, String status,
                                            String image, String minStockLevel) {
        req.setAttribute("product_code", productCode);
        req.setAttribute("name", name);
        req.setAttribute("description", description);
        req.setAttribute("size", size);
        req.setAttribute("color", color);
        req.setAttribute("material", material);
        req.setAttribute("unit", unit);
        req.setAttribute("cost_price", costPrice);
        req.setAttribute("sale_price", salePrice);
        req.setAttribute("status", status);
        req.setAttribute("image", image);
        req.setAttribute("min_stock_level", minStockLevel);
    }
}