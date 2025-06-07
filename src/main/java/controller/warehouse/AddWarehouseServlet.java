package controller.warehouse;

import dal.WarehouseDAO;
import model.Warehouses;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AddWarehouseServlet", value = "/addWarehouse")
public class AddWarehouseServlet extends HttpServlet {


    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";

    private static final String ADD_WAREHOUSE_CONTENT = "/views/warehouses/addWarehouse.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Bước 1: Đặt attribute để layout biết cần nạp trang con nào
        request.setAttribute("PAGE_CONTENT", ADD_WAREHOUSE_CONTENT);

        // Bước 2: Luôn forward đến file layout chính
        RequestDispatcher dispatcher = request.getRequestDispatcher(LAYOUT_PAGE);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String location = request.getParameter("location");

        // Luôn đặt các giá trị đã nhập vào request để có thể hiển thị lại nếu có lỗi
        request.setAttribute("name", name);
        request.setAttribute("location", location);

        if (name == null || name.trim().isEmpty() || location == null || location.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Name and Location must not be empty.");
            // Gọi phương thức forward đến layout
            forwardToLayout(request, response);
            return;
        }

        WarehouseDAO warehouseDAO = new WarehouseDAO();
        if (warehouseDAO.isWarehouseNameExists(name.trim())) {
            request.setAttribute("errorMessage", "Warehouse name already exists. Please choose another name.");
            // Gọi phương thức forward đến layout
            forwardToLayout(request, response);
            return;
        }

        Warehouses warehouse = new Warehouses(0, name.trim(), location.trim());
        boolean isAdded = warehouseDAO.addWarehouse(warehouse);

        if (isAdded) {
            // Khi thành công, nên redirect về trang danh sách để tránh F5 gây submit lại form
            // Thêm một session attribute để hiển thị thông báo thành công ở trang danh sách
            request.getSession().setAttribute("successMessage", "Warehouse '" + name.trim() + "' added successfully!");
            response.sendRedirect(request.getContextPath() + "/viewListWarehouses");
        } else {
            request.setAttribute("errorMessage", "Failed to add warehouse. Please try again!");
            // Gọi phương thức forward đến layout
            forwardToLayout(request, response);
        }
    }

    // Phương thức helper để tái sử dụng code forward
    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("PAGE_CONTENT", ADD_WAREHOUSE_CONTENT);
        RequestDispatcher dispatcher = request.getRequestDispatcher(LAYOUT_PAGE);
        dispatcher.forward(request, response);
    }
}