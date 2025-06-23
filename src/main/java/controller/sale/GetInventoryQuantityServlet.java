package controller.sale;

import com.google.gson.JsonObject;
import dal.InventoryDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Inventory;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getInventoryQuantity")
public class GetInventoryQuantityServlet extends HttpServlet {

    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonResponse = new JsonObject();
        PrintWriter out = response.getWriter();

        try {
            String warehouseParam = request.getParameter("warehouseId");
            String productParam = request.getParameter("productId");

            System.out.println("📥 Raw Params: warehouseId = " + warehouseParam + ", productId = " + productParam);

            int warehouseId = Integer.parseInt(warehouseParam);
            int productId = Integer.parseInt(productParam);

            Inventory inventory = inventoryDAO.getInventoryById(productId, warehouseId);
            int quantity = (inventory != null) ? inventory.getQuantity() : 0;

            System.out.println("✅ Inventory lookup: warehouseId = " + warehouseId +
                    ", productId = " + productId + " --> quantity = " + quantity);

            jsonResponse.addProperty("quantity", quantity);
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("error", "❌ Invalid parameters: warehouseId or productId is not a number.");
            System.out.println("❌ NumberFormatException: " + e.getMessage());
        } catch (Exception e) {
            jsonResponse.addProperty("error", "❌ Server error: " + e.getMessage());
            System.out.println("❌ Exception: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
        out.flush();
    }
}
