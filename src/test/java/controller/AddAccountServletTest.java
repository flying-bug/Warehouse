package controller;

import dal.AccountDAO;
import dal.RoleDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import model.Accounts;
import model.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.mockito.Mockito.*;

class AddAccountServletTest {

    private AddAccountServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private RoleDAO roleDAO;
    private AccountDAO accountDAO;

    @BeforeEach
    void setup() {
        servlet = new AddAccountServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        roleDAO = mock(RoleDAO.class);
        accountDAO = mock(AccountDAO.class);
    }

    @Test
    void testDoGet() throws Exception {
        when(request.getRequestDispatcher("/views/dashboard.jsp")).thenReturn(dispatcher);
        when(roleDAO.getAllRoles()).thenReturn(Arrays.asList(new Roles(1, "Admin")));

        servlet = spy(servlet);
        doReturn(roleDAO).when(servlet).createRoleDAO();

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("roles"), any());
        verify(request).setAttribute("PAGE_CONTENT", "/views/accounts/addAccount.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_MissingFields() throws Exception {
        mockRequestParams("", "", "", "", "", "1");
        when(request.getRequestDispatcher("/views/dashboard.jsp")).thenReturn(dispatcher);
        when(roleDAO.getAllRoles()).thenReturn(Collections.emptyList());

        servlet = spy(servlet);
        doReturn(roleDAO).when(servlet).createRoleDAO();

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("fail"), contains("Vui lòng nhập đầy đủ"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_InvalidAccountName() throws Exception {
        mockRequestParams("test user", "Password1", "test@gmail.com", "0123456789", "Test User", "1");
        when(request.getRequestDispatcher("/views/dashboard.jsp")).thenReturn(dispatcher);
        when(roleDAO.getAllRoles()).thenReturn(Collections.emptyList());

        servlet = spy(servlet);
        doReturn(roleDAO).when(servlet).createRoleDAO();

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("fail"), contains("chỉ được chứa chữ cái và số"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_InvalidPassword() throws Exception {
        mockRequestParams("testuser", "abc", "test@gmail.com", "0123456789", "Test User", "1");
        when(request.getRequestDispatcher("/views/dashboard.jsp")).thenReturn(dispatcher);
        when(roleDAO.getAllRoles()).thenReturn(Collections.emptyList());

        servlet = spy(servlet);
        doReturn(roleDAO).when(servlet).createRoleDAO();

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("fail"), contains("Password phải có ít nhất 6 ký tự"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_InvalidPhone() throws Exception {
        mockRequestParams("testuser", "Password1", "test@gmail.com", "12345", "Test User", "1");
        when(request.getRequestDispatcher("/views/dashboard.jsp")).thenReturn(dispatcher);
        when(roleDAO.getAllRoles()).thenReturn(Collections.emptyList());

        servlet = spy(servlet);
        doReturn(roleDAO).when(servlet).createRoleDAO();

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("fail"), contains("Số điện thoại phải bắt đầu"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_InvalidEmail() throws Exception {
        mockRequestParams("testuser", "Password1", "abc@yahoo.com", "0123456789", "Test User", "1");
        when(request.getRequestDispatcher("/views/dashboard.jsp")).thenReturn(dispatcher);
        when(roleDAO.getAllRoles()).thenReturn(Collections.emptyList());

        servlet = spy(servlet);
        doReturn(roleDAO).when(servlet).createRoleDAO();

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("fail"), contains("Email chỉ chấp nhận"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_EmailExisted() throws Exception {
        mockRequestParams("testuser", "Password1", "test@gmail.com", "0123456789", "Test User", "1");
        when(request.getRequestDispatcher("/views/dashboard.jsp")).thenReturn(dispatcher);

        servlet = spy(servlet);
        doReturn(roleDAO).when(servlet).createRoleDAO();
        doReturn(accountDAO).when(servlet).createAccountDAO();

        when(accountDAO.isEmailDuplicate("test@gmail.com")).thenReturn(true);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("fail"), contains("Email đã được sử dụng"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_AddAccountFailed() throws Exception {
        mockRequestParams("testuser", "Password1", "test@gmail.com", "0123456789", "Test User", "1");

        Part filePart = mock(Part.class);
        when(filePart.getHeader("content-disposition")).thenReturn("form-data; name=\"profile_image\"; filename=\"avatar.jpg\"");
        doNothing().when(filePart).write(anyString());
        when(request.getPart("profile_image")).thenReturn(filePart);

        ServletContext context = mock(ServletContext.class);
        when(context.getRealPath("")).thenReturn(System.getProperty("java.io.tmpdir"));
        when(request.getServletContext()).thenReturn(context);

        when(accountDAO.isEmailDuplicate(any())).thenReturn(false);
        when(accountDAO.isPhoneDuplicate(any())).thenReturn(false);
        when(accountDAO.addAccount(any(Accounts.class))).thenReturn(false);
        when(roleDAO.getAllRoles()).thenReturn(Collections.singletonList(new Roles(1, "Admin")));
        when(request.getRequestDispatcher("/views/dashboard.jsp")).thenReturn(dispatcher);

        servlet = spy(servlet);
        doReturn(roleDAO).when(servlet).createRoleDAO();
        doReturn(accountDAO).when(servlet).createAccountDAO();

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("fail"), contains("Add account failed"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_Success() throws Exception {
        mockRequestParams("testuser", "Password1", "test@gmail.com", "0123456789", "Test User", "1");

        Part filePart = mock(Part.class);
        when(filePart.getHeader("content-disposition")).thenReturn("form-data; name=\"profile_image\"; filename=\"avatar.jpg\"");
        doNothing().when(filePart).write(anyString());
        when(request.getPart("profile_image")).thenReturn(filePart);

        ServletContext context = mock(ServletContext.class);
        when(context.getRealPath("")).thenReturn(System.getProperty("java.io.tmpdir"));
        when(request.getServletContext()).thenReturn(context);

        when(accountDAO.isEmailDuplicate(any())).thenReturn(false);
        when(accountDAO.isPhoneDuplicate(any())).thenReturn(false);
        when(accountDAO.addAccount(any(Accounts.class))).thenReturn(true);
        when(roleDAO.getAllRoles()).thenReturn(Collections.singletonList(new Roles(1, "Admin")));
        when(request.getRequestDispatcher("/views/dashboard.jsp")).thenReturn(dispatcher);

        servlet = spy(servlet);
        doReturn(roleDAO).when(servlet).createRoleDAO();
        doReturn(accountDAO).when(servlet).createAccountDAO();

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("success"), anyString());
        verify(dispatcher).forward(request, response);
    }

    private void mockRequestParams(String accountName, String password, String email, String phone, String fullName, String roleId) {
        when(request.getParameter("account_name")).thenReturn(accountName);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("phone")).thenReturn(phone);
        when(request.getParameter("full_name")).thenReturn(fullName);
        when(request.getParameter("role_id")).thenReturn(roleId);
    }
}
