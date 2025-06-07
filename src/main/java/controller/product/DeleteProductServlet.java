package controller.product;

import dal.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "DeleteProductServlet", urlPatterns = {"/deleteProduct"})
public class DeleteProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Not implemented for now. Deletion is handled via GET for simplicity.
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        ProductDAO productDAO = new ProductDAO();

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                boolean deleted = productDAO.deleteProduct(id); // Call DAO to delete the product

                if (deleted) { // Check if the deletion was successful
                    request.setAttribute("success", "Product deleted successfully.");
                } else {
                    request.setAttribute("fail", "Failed to delete the product.");
                }
            } catch (NumberFormatException e) { // Handle invalid ID format
                request.setAttribute("fail", "Invalid product ID.");
            }

            // Redirect back to the product list page
            request.getRequestDispatcher("/viewListProducts").forward(request, response);
        } else {
            request.setAttribute("fail", "Product ID is missing.");
            request.getRequestDispatcher("/viewListProducts").forward(request, response);
        }
    }
}