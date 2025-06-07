package controller.supplier;

import dal.SupplierDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Suppliers;

import java.io.IOException;

@WebServlet(name = "UpdateSupplierServlet", urlPatterns = {"/updateSupplier"})
public class UpdateSupplierServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String UPDATE_SUPPLIER_CONTENT = "/views/suppliers/updateSupplier.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String supplierIdString = request.getParameter("supplier_id");
            int supplierId = Integer.parseInt(supplierIdString);

            SupplierDAO supplierDAO = new SupplierDAO();
            Suppliers supplier = supplierDAO.getSupplierById(supplierId);

            if (supplier != null) {
                request.setAttribute("supplier", supplier);
                request.setAttribute("PAGE_CONTENT", UPDATE_SUPPLIER_CONTENT);
                request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }

        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int supplierId = Integer.parseInt(request.getParameter("supplier_id"));
            String name = request.getParameter("name").trim();
            String phone = request.getParameter("phone").trim();
            String address = request.getParameter("address").trim();

            request.setAttribute("supplier_id", supplierId);
            request.setAttribute("name", name);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);

            SupplierDAO supplierDAO = new SupplierDAO();
            Suppliers existing = supplierDAO.getSupplierById(supplierId);

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ tất cả các trường.");
            } else if (!phone.matches("\\d{10,11}")) {
                request.setAttribute("errorMessage", "Số điện thoại không hợp lệ. Vui lòng nhập 10 hoặc 11 chữ số.");
            } else if (supplierDAO.isDuplicateSupplierName(name) && !existing.getName().equals(name)) {
                request.setAttribute("errorMessage", "Tên nhà cung cấp đã tồn tại, vui lòng chọn tên khác.");
            } else if (supplierDAO.isDuplicatePhone(phone) && !existing.getPhone().equals(phone)) {
                request.setAttribute("errorMessage", "Số điện thoại đã tồn tại, vui lòng nhập số khác.");
            } else {
                Suppliers updated = new Suppliers(supplierId, name, phone, address);
                boolean isUpdated = supplierDAO.updateSupplier(updated);

                if (isUpdated) {
                    response.sendRedirect(request.getContextPath() + "/viewListSuppliers");
                    return;
                } else {
                    request.setAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật nhà cung cấp.");
                }
            }

            // Nếu có lỗi, quay lại form cập nhật
            request.setAttribute("supplier", existing);
            request.setAttribute("PAGE_CONTENT", UPDATE_SUPPLIER_CONTENT);
            RequestDispatcher dispatcher = request.getRequestDispatcher(LAYOUT_PAGE);
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "ID nhà cung cấp không hợp lệ.");
            request.setAttribute("PAGE_CONTENT", UPDATE_SUPPLIER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật nhà cung cấp.");
            request.setAttribute("PAGE_CONTENT", UPDATE_SUPPLIER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        }
    }
}
