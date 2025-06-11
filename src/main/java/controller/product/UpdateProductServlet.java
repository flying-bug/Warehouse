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

@WebServlet(name = "UpdateProductServlet", urlPatterns = "/updateProduct")
public class UpdateProductServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UpdateProductServlet.class.getName());
    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String UPDATE_PRODUCT_CONTENT = "/views/products/updateProduct.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("productId");
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("fail", "Mã sản phẩm không hợp lệ.");
            req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
            req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            ProductDAO productDAO = new ProductDAO();
            Products product = productDAO.getProductById(productId);

            if (product == null) {
                req.setAttribute("fail", "Không tìm thấy sản phẩm.");
                req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            req.setAttribute("p", product);
            req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
            req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
        } catch (NumberFormatException e) {
            LOGGER.severe("Invalid product ID format: " + idParam);
            req.setAttribute("fail", "Mã sản phẩm không hợp lệ.");
            req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
            req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String productIdStr = req.getParameter("product_id");
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
            if (isBlank(productIdStr) || isBlank(productCode) || isBlank(name) || isBlank(unit) ||
                    isBlank(costPriceStr) || isBlank(salePriceStr) || isBlank(statusStr) || isBlank(minStockLevelStr)) {
                req.setAttribute("fail", "Vui lòng điền đầy đủ các trường bắt buộc.");
                setAttributesForFormValues(req, productIdStr, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            // Parse numeric fields
            int productId = Integer.parseInt(productIdStr);
            BigDecimal costPrice = new BigDecimal(costPriceStr);
            BigDecimal salePrice = new BigDecimal(salePriceStr);
            int status = Integer.parseInt(statusStr);
            int minStockLevel = Integer.parseInt(minStockLevelStr);

            // Validate business rules
            if (costPrice.compareTo(BigDecimal.ZERO) <= 0 || salePrice.compareTo(BigDecimal.ZERO) <= 0) {
                req.setAttribute("fail", "Giá vốn và giá bán phải lớn hơn 0.");
                setAttributesForFormValues(req, productIdStr, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            if (salePrice.compareTo(costPrice) <= 0) {
                req.setAttribute("fail", "Giá bán phải lớn hơn giá vốn.");
                setAttributesForFormValues(req, productIdStr, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            if (minStockLevel < 0) {
                req.setAttribute("fail", "Mức tồn kho tối thiểu phải lớn hơn hoặc bằng 0.");
                setAttributesForFormValues(req, productIdStr, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            // Check for duplicate product code (excluding current product)
            ProductDAO productDAO = new ProductDAO();
            Products existingProduct = productDAO.getProductByCode(productCode.trim());
            if (existingProduct != null && existingProduct.getProductId() != productId) {
                req.setAttribute("fail", "Mã sản phẩm đã tồn tại. Vui lòng chọn mã khác.");
                setAttributesForFormValues(req, productIdStr, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
                return;
            }

            // Create product object
            Products product = new Products(
                    productId,
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

            // Update product
            boolean success = productDAO.updateProduct(product);

            if (success) {
                req.setAttribute("success", "Cập nhật sản phẩm thành công.");
                resp.sendRedirect(req.getContextPath() + "/viewListProducts");
            } else {
                req.setAttribute("fail", "Cập nhật sản phẩm thất bại. Vui lòng thử lại.");
                setAttributesForFormValues(req, productIdStr, productCode, name, description, size, color, material, unit,
                        costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
                req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
                req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
            }

        } catch (NumberFormatException e) {
            LOGGER.severe("Invalid numeric input: " + e.getMessage());
            req.setAttribute("fail", "Dữ liệu số không hợp lệ (giá, trạng thái, hoặc mức tồn kho).");
            setAttributesForFormValues(req, productIdStr, productCode, name, description, size, color, material, unit,
                    costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
            req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
            req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
        } catch (Exception e) {
            LOGGER.severe("Error updating product: " + e.getMessage());
            req.setAttribute("fail", "Lỗi hệ thống. Vui lòng thử lại sau.");
            setAttributesForFormValues(req, productIdStr, productCode, name, description, size, color, material, unit,
                    costPriceStr, salePriceStr, statusStr, image, minStockLevelStr);
            req.setAttribute("PAGE_CONTENT", UPDATE_PRODUCT_CONTENT);
            req.getRequestDispatcher(LAYOUT_PAGE).forward(req, resp);
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void setAttributesForFormValues(HttpServletRequest req,
                                            String productId, String productCode, String name, String description,
                                            String size, String color, String material, String unit,
                                            String costPrice, String salePrice, String status,
                                            String image, String minStockLevel) {
        req.setAttribute("p", new Products(
                isBlank(productId) ? 0 : Integer.parseInt(productId),
                productCode,
                name,
                description,
                size,
                color,
                material,
                unit,
                isBlank(costPrice) ? null : new BigDecimal(costPrice),
                isBlank(salePrice) ? null : new BigDecimal(salePrice),
                isBlank(status) ? 0 : Integer.parseInt(status),
                image,
                isBlank(minStockLevel) ? 0 : Integer.parseInt(minStockLevel)
        ));
    }
}