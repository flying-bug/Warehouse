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

@WebServlet(name = "UpdateAccountServlet", urlPatterns = {"/updateAccount"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50)
public class UpdateAccountServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";
    private static final String LAYOUT_PAGE = "/views/dashboard.jsp";
    private static final String UPDATE_ACCOUNT_CONTENT = "/views/accounts/updateAccount.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        try {
            int accountId = Integer.parseInt(idParam);
            AccountDAO accountDAO = new AccountDAO();
            Accounts account = accountDAO.getAccountById(accountId);

            if (account == null) {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
                return;
            }

            request.setAttribute("a", account);
            request.setAttribute("roles", new RoleDAO().getAllRoles());

            request.setAttribute("PAGE_CONTENT", UPDATE_ACCOUNT_CONTENT);
            request.getRequestDispatcher(LAYOUT_PAGE).forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int accountId = Integer.parseInt(request.getParameter("account_id"));
            String accountName = request.getParameter("account_name");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String fullName = request.getParameter("full_name");
            int roleId = Integer.parseInt(request.getParameter("role_id"));

            String errorMsg = null;

            if (phone.isEmpty() || fullName.isEmpty()) {
                errorMsg = "Vui lòng nhập đầy đủ thông tin hợp lệ.";
            } else if (!phone.matches("^0\\d{9,11}$")) {
                errorMsg = "Số điện thoại phải bắt đầu bằng 0 và có từ 10 đến 12 chữ số.";
            } else if (!password.isEmpty()) {
                if (password.length() < 6 ||
                        !password.matches(".*[A-Z].*") ||
                        !password.matches(".*[a-z].*") ||
                        !password.matches(".*\\d.*") ||
                        !password.matches("[A-Za-z0-9]+")) {
                    errorMsg = "Password phải có ít nhất 6 ký tự, gồm chữ hoa, chữ thường và số, không có ký tự đặc biệt.";
                }
            }

            AccountDAO accountDAO = new AccountDAO();
            Accounts existingAccount = accountDAO.getAccountById(accountId);
            if (existingAccount == null) {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
                return;
            }

            if (errorMsg != null) {
                request.setAttribute("fail", errorMsg);
                repopulateForm(request, existingAccount);
                forwardToLayout(request, response);
                return;
            }

            String profileImage = null;
            Part filePart = request.getPart("profile_image");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = extractFileName(filePart);
                if (fileName != null && !fileName.isEmpty()) {
                    String applicationPath = request.getServletContext().getRealPath("");
                    String uploadPath = Paths.get(applicationPath, UPLOAD_DIR).toString();
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    String extension = "";
                    int dotIndex = fileName.lastIndexOf('.');
                    if (dotIndex > 0) {
                        extension = fileName.substring(dotIndex);
                    }

                    String uniqueName = UUID.randomUUID() + extension;
                    profileImage = UPLOAD_DIR + "/" + uniqueName;
                    filePart.write(uploadPath + File.separator + uniqueName);
                }
            }

            Accounts account = new Accounts();
            account.setAccount_id(accountId);
            account.setAccount_name(accountName);
            account.setEmail(email);
            account.setPhone(phone);
            account.setFull_name(fullName);
            account.setProfile_image(profileImage != null ? profileImage : existingAccount.getProfile_image());
            account.setRole_id(roleId);
            account.setPassword(password.isEmpty() ? existingAccount.getPassword() :
                    BCrypt.hashpw(password, BCrypt.gensalt(12)));

            boolean success = accountDAO.updateAccount(account);
            request.setAttribute(success ? "success" : "fail", success ? "Cập nhật tài khoản thành công." : "Cập nhật tài khoản thất bại.");
            repopulateForm(request, account);
            forwardToLayout(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    private void repopulateForm(HttpServletRequest request, Accounts account) {
        request.setAttribute("a", account);
        request.setAttribute("roles", new RoleDAO().getAllRoles());
    }

    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("PAGE_CONTENT", UPDATE_ACCOUNT_CONTENT);
        RequestDispatcher dispatcher = request.getRequestDispatcher(LAYOUT_PAGE);
        dispatcher.forward(request, response);
    }

    private String extractFileName(Part part) {
        if (part == null) return null;
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) return null;

        for (String cd : contentDisposition.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return Paths.get(cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "")).getFileName().toString();
            }
        }
        return null;
    }
}
