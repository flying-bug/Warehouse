package controller.sale;

import dal.ExportOrderDAO;
import dal.ExportOrderDetailDAO;
import dal.ProductDAO;
import dal.WarehouseDAO;
import model.Products;
import model.Warehouses;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CreateRequestProductServlet", urlPatterns = {"/createRequestProduct"})
public class CreateRequestProductServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String CREATE_EXPORT_ORDER_CONTENT = "/views/sales/createExportOrder.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        List<Products> productList = productDAO.getAllProducts();
        List<Warehouses> warehouseList = warehouseDAO.getAllWarehouses();

        request.setAttribute("productList", productList);
        request.setAttribute("warehouseList", warehouseList);
        request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        ProductDAO productDAO = new ProductDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        List<Products> productList = productDAO.getAllProducts();
        List<Warehouses> warehouseList = warehouseDAO.getAllWarehouses();

        request.setAttribute("productList", productList);
        request.setAttribute("warehouseList", warehouseList);

        try {
            int warehouseId = Integer.parseInt(request.getParameter("warehouseId"));
            String customerName = request.getParameter("customerName");
            String customerPhone = request.getParameter("customerPhone");
            String reason = request.getParameter("reason");
            String note = request.getParameter("note");

            HttpSession session = request.getSession();
            Integer accountId = (Integer) session.getAttribute("id");
            if (accountId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            String[] productIds = request.getParameterValues("productId");
            String[] quantities = request.getParameterValues("quantity");
            String[] salePrices = request.getParameterValues("salePrice");

            if (productIds == null || quantities == null || salePrices == null ||
                    productIds.length != quantities.length || productIds.length != salePrices.length) {
                request.setAttribute("fail", "Invalid product data.");
                request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
                return;
            }

            double totalPrice = 0;
            for (int i = 0; i < productIds.length; i++) {
                int quantity = Integer.parseInt(quantities[i]);
                double price = Double.parseDouble(salePrices[i]);
                totalPrice += quantity * price;
            }

            ExportOrderDAO exportDAO = new ExportOrderDAO();
            int exportId = exportDAO.createExportOrder(
                    warehouseId,
                    accountId,
                    customerName,
                    customerPhone,
                    reason,
                    note,
                    totalPrice
            );

            ExportOrderDetailDAO detailDAO = new ExportOrderDetailDAO();
            for (int i = 0; i < productIds.length; i++) {
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                double price = Double.parseDouble(salePrices[i]);
                detailDAO.insertExportOrderDetail(exportId, productId, quantity, price);
            }

            request.setAttribute("success", "Export order created successfully.");
            doGet(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("fail", "An error occurred while processing the order: " + e.getMessage());
            request.setAttribute("PAGE_CONTENT", CREATE_EXPORT_ORDER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        }
    }
}
