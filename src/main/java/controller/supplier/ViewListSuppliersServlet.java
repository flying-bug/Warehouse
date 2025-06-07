package controller.supplier;

import dal.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Suppliers;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewListSupplierServlet", urlPatterns = {"/viewListSuppliers"})
public class ViewListSuppliersServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String SUPPLIER_LIST_PAGE = "/views/suppliers/viewListSuppliers.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            SupplierDAO supplierDAO = new SupplierDAO();
            List<Suppliers> suppliers = supplierDAO.getAllSuppliers();

            request.setAttribute("suppliers", suppliers);
            request.setAttribute("PAGE_CONTENT", SUPPLIER_LIST_PAGE);

            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
