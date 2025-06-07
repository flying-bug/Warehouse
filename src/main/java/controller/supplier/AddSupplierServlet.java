package controller.supplier;

import dal.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Suppliers;

import java.io.IOException;

@WebServlet(name = "AddSupplierServlet", urlPatterns = {"/addSupplier"})
public class AddSupplierServlet extends HttpServlet {

    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String ADD_SUPPLIER_CONTENT = "/views/suppliers/addSupplier.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("PAGE_CONTENT", ADD_SUPPLIER_CONTENT);
        request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu form
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Gán lại để giữ giá trị nếu có lỗi trả về trang form
        request.setAttribute("name", name);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);

        // Validate dữ liệu đầu vào
        if (name == null || name.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                address == null || address.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng điền đầy đủ tất cả các trường.");
            request.setAttribute("PAGE_CONTENT", ADD_SUPPLIER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
            return;
        }

        // Kiểm tra định dạng số điện thoại đơn giản (ví dụ chỉ gồm số và 10-11 ký tự)
        if (!phone.matches("\\d{10,11}")) {
            request.setAttribute("errorMessage", "Số điện thoại không hợp lệ. Vui lòng nhập 10 hoặc 11 chữ số.");
            request.setAttribute("PAGE_CONTENT", ADD_SUPPLIER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
            return;
        }

        SupplierDAO supplierDAO = new SupplierDAO();

        // Kiểm tra trùng tên nhà cung cấp
        if (supplierDAO.isDuplicateSupplierName(name)) {
            request.setAttribute("errorMessage", "Tên nhà cung cấp đã tồn tại, vui lòng chọn tên khác.");
            request.setAttribute("PAGE_CONTENT", ADD_SUPPLIER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
            return;
        }

        // Kiểm tra trùng số điện thoại
        if (supplierDAO.isDuplicatePhone(phone)) {
            request.setAttribute("errorMessage", "Số điện thoại đã tồn tại, vui lòng nhập số khác.");
            request.setAttribute("PAGE_CONTENT", ADD_SUPPLIER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
            return;
        }

        // Tạo đối tượng Supplier và thêm mới
        Suppliers supplier = new Suppliers();
        supplier.setName(name.trim());
        supplier.setPhone(phone.trim());
        supplier.setAddress(address.trim());

        try {
            supplierDAO.addSupplier(supplier);
            // Thêm thành công, chuyển hướng về danh sách
            response.sendRedirect(request.getContextPath() + "/viewListSuppliers");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi thêm nhà cung cấp.");
            request.setAttribute("PAGE_CONTENT", ADD_SUPPLIER_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        }
    }
}