package model;

import java.math.BigDecimal;

public class ExportOrderDetails {
    private int id;
    private int exportId;
    private int productId;
    private int quantity;
    private BigDecimal salePrice;

    public ExportOrderDetails() {
    }

    public ExportOrderDetails(int id, int exportId, int productId, int quantity, BigDecimal salePrice) {
        this.id = id;
        this.exportId = exportId;
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExportId() {
        return exportId;
    }

    public void setExportId(int exportId) {
        this.exportId = exportId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
}
