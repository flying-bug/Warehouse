package model;

import java.math.BigDecimal;

public class ImportOrderDetails {
    private int id;
    private int importId;
    private int productId;
    private int quantity;
    private BigDecimal costPrice = BigDecimal.ZERO;
    private BigDecimal taxPercent = BigDecimal.ZERO;
    private int quantityReceived;
    private int quantityInvoiced;
    private String importStatus;

    private BigDecimal lineTotal = BigDecimal.ZERO; // costPrice * quantity

    public ImportOrderDetails() {}

    public ImportOrderDetails(int id, int importId, int productId, int quantity,
                              BigDecimal costPrice, BigDecimal taxPercent,
                              int quantityReceived, int quantityInvoiced, String importStatus) {
        this.id = id;
        this.importId = importId;
        this.productId = productId;
        this.quantity = quantity;
        this.costPrice = costPrice != null ? costPrice : BigDecimal.ZERO;
        this.taxPercent = taxPercent != null ? taxPercent : BigDecimal.ZERO;
        this.quantityReceived = quantityReceived;
        this.quantityInvoiced = quantityInvoiced;
        this.importStatus = importStatus;
        updateLineTotal();
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getImportId() { return importId; }
    public void setImportId(int importId) { this.importId = importId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateLineTotal();
    }

    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice != null ? costPrice : BigDecimal.ZERO;
        updateLineTotal();
    }

    public BigDecimal getTaxPercent() { return taxPercent; }
    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent != null ? taxPercent : BigDecimal.ZERO;
    }

    public int getQuantityReceived() { return quantityReceived; }
    public void setQuantityReceived(int quantityReceived) { this.quantityReceived = quantityReceived; }

    public int getQuantityInvoiced() { return quantityInvoiced; }
    public void setQuantityInvoiced(int quantityInvoiced) { this.quantityInvoiced = quantityInvoiced; }

    public String getImportStatus() { return importStatus; }
    public void setImportStatus(String importStatus) { this.importStatus = importStatus; }

    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }

    // --- Logic: auto update line total ---
    private void updateLineTotal() {
        if (this.costPrice != null && this.quantity > 0) {
            this.lineTotal = this.costPrice.multiply(BigDecimal.valueOf(this.quantity));
        } else {
            this.lineTotal = BigDecimal.ZERO;
        }
    }

    // --- Tính thuế cho dòng ---
    public BigDecimal getTaxAmount() {
        return lineTotal.multiply(taxPercent).divide(BigDecimal.valueOf(100));
    }

    public BigDecimal getTotalWithTax() {
        return lineTotal.add(getTaxAmount());
    }
}
