package model;

public class Adjustment {
    private int adjustmentId;
    private int productId;
    private int warehouseId;
    private int accountId;
    private String adjustmentDate;
    private int quantityChange;
    private int oldQuantity;
    private String reason;

    public Adjustment() {
    }

    // ➕ Constructor đầy đủ
    public Adjustment(int adjustmentId, int productId, int warehouseId, int accountId,
                      String adjustmentDate, int quantityChange, int oldQuantity, String reason) {
        this.adjustmentId = adjustmentId;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.accountId = accountId;
        this.adjustmentDate = adjustmentDate;
        this.quantityChange = quantityChange;
        this.oldQuantity = oldQuantity;
        this.reason = reason;
    }



    public Adjustment(int productId, int warehouseId, int quantityChange, int oldQuantity, String reason, int accountId) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantityChange = quantityChange;
        this.oldQuantity = oldQuantity;
        this.reason = reason;
        this.accountId = accountId;
    }
    // Getter & Setter

    public int getAdjustmentId() {
        return adjustmentId;
    }

    public void setAdjustmentId(int adjustmentId) {
        this.adjustmentId = adjustmentId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(String adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }

    public int getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(int oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Adjustment{" +
                "adjustmentId=" + adjustmentId +
                ", productId=" + productId +
                ", warehouseId=" + warehouseId +
                ", accountId=" + accountId +
                ", adjustmentDate='" + adjustmentDate + '\'' +
                ", quantityChange=" + quantityChange +
                ", oldQuantity=" + oldQuantity +
                ", reason='" + reason + '\'' +
                '}';
    }
}
