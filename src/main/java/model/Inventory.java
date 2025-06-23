package model;
import java.sql.Timestamp;

public class Inventory {

    private int productId;
    private int warehouseId;
    private int quantity;
    private Timestamp last_updated;

    public Inventory() {
    }

    public Inventory(int productId, int warehouseId, int quantity, Timestamp last_updated) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.last_updated = last_updated;
    }

    public Inventory(int productId, int warehouseId, int quantity) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
    }

    // Getter và Setter
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Timestamp getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Timestamp last_updated) {
        this.last_updated = last_updated;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "productId=" + productId +
                ", warehouseId=" + warehouseId +
                ", quantity=" + quantity +
                '}';
    }
}