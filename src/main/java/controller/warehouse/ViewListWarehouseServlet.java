package controller.warehouse;

import dal.WarehouseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Warehouses;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewListWarehouseServlet", urlPatterns = {"/viewListWarehouses"})
public class ViewListWarehouseServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String WAREHOUSE_LIST_PAGE = "/views/warehouses/viewListWarehouse.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            WarehouseDAO warehouseDAO = new WarehouseDAO();
            List<Warehouses> warehouses = warehouseDAO.getAllWarehouses();

            request.setAttribute("warehouseList", warehouses);
            request.setAttribute("PAGE_CONTENT", WAREHOUSE_LIST_PAGE);

            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
