package model;

import java.math.BigDecimal;

public class ExportOrderDetails {
    private int id;
    private int exportId;
    private int productId;
    private int quantity;
    private BigDecimal salePrice = BigDecimal.ZERO;
    private BigDecimal taxPercent = BigDecimal.ZERO;
    private int quantityDelivered;
    private int quantityInvoiced;
    private String status;

    private BigDecimal lineTotal = BigDecimal.ZERO; // = salePrice * quantity

    public ExportOrderDetails() {}

    public ExportOrderDetails(int id, int exportId, int productId, int quantity,
                              BigDecimal salePrice, BigDecimal taxPercent,
                              int quantityDelivered, int quantityInvoiced, String status) {
        this.id = id;
        this.exportId = exportId;
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice != null ? salePrice : BigDecimal.ZERO;
        this.taxPercent = taxPercent != null ? taxPercent : BigDecimal.ZERO;
        this.quantityDelivered = quantityDelivered;
        this.quantityInvoiced = quantityInvoiced;
        this.status = status;
        updateLineTotal();
    }

    public ExportOrderDetails(int id, int exportId, int productId,
                              int quantity, BigDecimal salePrice, String status) {
        this(id, exportId, productId, quantity, salePrice, BigDecimal.ZERO, 0, 0, status);
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getExportId() { return exportId; }
    public void setExportId(int exportId) { this.exportId = exportId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateLineTotal();
    }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice != null ? salePrice : BigDecimal.ZERO;
        updateLineTotal();
    }

    public BigDecimal getTaxPercent() { return taxPercent; }
    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent != null ? taxPercent : BigDecimal.ZERO;
    }

    public int getQuantityDelivered() { return quantityDelivered; }
    public void setQuantityDelivered(int quantityDelivered) { this.quantityDelivered = quantityDelivered; }

    public int getQuantityInvoiced() { return quantityInvoiced; }
    public void setQuantityInvoiced(int quantityInvoiced) { this.quantityInvoiced = quantityInvoiced; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }

    // ✅ Auto update
    private void updateLineTotal() {
        if (this.salePrice != null && this.quantity > 0) {
            this.lineTotal = this.salePrice.multiply(BigDecimal.valueOf(this.quantity));
        } else {
            this.lineTotal = BigDecimal.ZERO;
        }
    }

    // ✅ Tính thuế cho dòng sản phẩm
    public BigDecimal getTaxAmount() {
        return lineTotal.multiply(taxPercent).divide(BigDecimal.valueOf(100));
    }

    // ✅ Tổng dòng có thuế
    public BigDecimal getTotalWithTax() {
        return lineTotal.add(getTaxAmount());
    }
}
