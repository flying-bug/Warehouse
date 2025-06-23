package controller;

import dal.AccountDAO;
import dal.RoleDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Accounts;
import model.Roles;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "AddAccountServlet", urlPatterns = {"/addAccount"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50)


public class AddAccountServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";
    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String ADD_ACCOUNT_CONTENT = "/views/accounts/addAccount.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Roles> roleList = createRoleDAO().getAllRoles(); // Đã thay thế new RoleDAO()
        request.setAttribute("roles", roleList);

        request.setAttribute("PAGE_CONTENT", ADD_ACCOUNT_CONTENT);
        RequestDispatcher dispatcher = request.getRequestDispatcher(LAYOUT_PAGE);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accountName = request.getParameter("account_name").trim();
            String password = request.getParameter("password").trim();
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone").trim();
            String fullName = request.getParameter("full_name").trim();
            int roleId = Integer.parseInt(request.getParameter("role_id").trim());
            String errorMsg = null;

            if (accountName.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || fullName.isEmpty()) {
                errorMsg = "Vui lòng nhập đầy đủ thông tin hợp lệ (không để trống hoặc chỉ có khoảng trắng).";
            } else if (!accountName.matches("^[a-zA-Z0-9]+$")) {
                errorMsg = "Tên tài khoản chỉ được chứa chữ cái và số, không có ký tự đặc biệt hoặc dấu cách.";
            } else if (password.length() < 6 ||
                    !password.matches(".*[A-Z].*") ||
                    !password.matches(".*[a-z].*") ||
                    !password.matches(".*\\d.*") ||
                    !password.matches("[A-Za-z0-9]+")) {
                errorMsg = "Password phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số, không có ký tự đặc biệt.";
            } else if (!email.endsWith("@gmail.com") && !email.endsWith("@fpt.edu.vn")) {
                errorMsg = "Email chỉ chấp nhận @gmail.com hoặc @fpt.edu.vn.";
            } else if (!phone.matches("^0\\d{9,11}$")) {
                errorMsg = "Số điện thoại phải bắt đầu bằng 0 và có từ 10 đến 12 chữ số.";
            }

            if (errorMsg != null) {
                request.setAttribute("fail", errorMsg);
                repopulateForm(request, accountName, email, phone, fullName, roleId);
                forwardToLayout(request, response);
                return;
            }

            AccountDAO accountDAO = createAccountDAO(); // thay vì new AccountDAO()

            if (accountDAO.isEmailDuplicate(email)) {
                request.setAttribute("fail", "Email đã được sử dụng");
                repopulateForm(request, accountName, email, phone, fullName, roleId);
                forwardToLayout(request, response);
                return;
            }

            if (accountDAO.isPhoneDuplicate(phone)) {
                request.setAttribute("fail", "Số điện thoại đã được sử dụng");
                repopulateForm(request, accountName, email, phone, fullName, roleId);
                forwardToLayout(request, response);
                return;
            }

            String storedFileName = null;
            Part filePart = request.getPart("profile_image");
            String originalFileName = extractFileName(filePart);

            if (originalFileName != null && !originalFileName.isEmpty()) {
                String applicationPath = request.getServletContext().getRealPath("");
                String uploadPathAbsolute = Paths.get(applicationPath, UPLOAD_DIR).toString();
                File uploadDir = new File(uploadPathAbsolute);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String fileExtension = "";
                int dotIndex = originalFileName.lastIndexOf('.');
                if (dotIndex > 0) {
                    fileExtension = originalFileName.substring(dotIndex);
                }
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                storedFileName = UPLOAD_DIR + "/" + uniqueFileName;
                filePart.write(uploadPathAbsolute + File.separator + uniqueFileName);
            }

            Accounts account = new Accounts();
            account.setAccount_name(accountName);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
            account.setPassword(hashedPassword);
            account.setEmail(email);
            account.setPhone(phone);
            account.setFull_name(fullName);
            account.setProfile_image(storedFileName);
            account.setRole_id(roleId);

            boolean check = accountDAO.addAccount(account);
            request.setAttribute("roles", createRoleDAO().getAllRoles()); // sửa

            if (!check) {
                request.setAttribute("fail", "Add account failed");
                repopulateForm(request, accountName, email, phone, fullName, roleId);
            } else {
                request.setAttribute("success", "Account added successfully");
            }
            forwardToLayout(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    private void repopulateForm(HttpServletRequest request, String accountName, String email, String phone, String fullName, int roleId) {
        request.setAttribute("roles", createRoleDAO().getAllRoles()); // sửa
        request.setAttribute("account_name", accountName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("full_name", fullName);
        request.setAttribute("selectedRoleId", roleId);
    }

    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("PAGE_CONTENT", ADD_ACCOUNT_CONTENT);
        RequestDispatcher dispatcher = request.getRequestDispatcher(LAYOUT_PAGE);
        dispatcher.forward(request, response);
    }

    private String extractFileName(Part part) {
        if (part == null) return null;
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) return null;

        for (String cd : contentDisposition.split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return Paths.get(fileName).getFileName().toString();
            }
        }
        return null;
    }

    //  THÊM VÀO CUỐI CLASS — để test override
    protected AccountDAO createAccountDAO() {
        return new AccountDAO();
    }

    protected RoleDAO createRoleDAO() {
        return new RoleDAO();
    }
}
