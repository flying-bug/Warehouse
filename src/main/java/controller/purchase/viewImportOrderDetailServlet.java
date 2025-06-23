package controller.purchase;

import dal.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "viewImportOrderDetailServlet", urlPatterns = {"/viewImportOrderDetail"})
public class viewImportOrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String importId = request.getParameter("importId");
        int id = Integer.parseInt(importId);

        if (importId != null && !importId.trim().isEmpty()) {
            ImportOrderDetailDAO dao = new ImportOrderDetailDAO();
            String sql = "SELECT * FROM import_order_details WHERE import_id = " + importId;

            List<ImportOrderDetails> list = dao.getImportOrderDetails(sql);
            request.setAttribute("importDetails", list);
        }
        ImportOrderDAO dao = new ImportOrderDAO();
        ImportOrders importOrder = dao.getImportOrderById(id);

        request.setAttribute("importOrder", importOrder);

        ProductDAO pdao = new ProductDAO();
        List<Products> plist = pdao.getAllProducts();
        request.setAttribute("plist", plist);
        SupplierDAO supplierDAO = new SupplierDAO();
        List<Suppliers> suppliersList = supplierDAO.getAllSuppliers();

        WarehouseDAO warehouseDAO = new WarehouseDAO();
        List<Warehouses> warehousesList = warehouseDAO.getAllWarehouses();

        AccountDAO accountDAO = new AccountDAO();
        List<Accounts> accountsList = accountDAO.getAllAccounts();

        request.setAttribute("suppliersList", suppliersList);
        request.setAttribute("warehousesList", warehousesList);
        request.setAttribute("accountsList", accountsList);


        // Truyền đến layout chung
        request.setAttribute("PAGE_CONTENT", "/views/purchases/importOrderDetail.jsp");
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
