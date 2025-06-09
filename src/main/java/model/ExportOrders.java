package model;

import java.math.BigDecimal;
import java.util.Date;

public class ExportOrders {
    private int exportId;
    private int warehouseId;
    private int accountId;
    private Date exportDate;
    private String customerName;
    private String customerPhone;
    private String reason;
    private String note;
    private BigDecimal totalSalePrice;

    public ExportOrders() {}

    public ExportOrders(int exportId, int warehouseId, int accountId, Date exportDate,
                        String customerName, String customerPhone, String reason,
                        String note, BigDecimal totalSalePrice) {
        this.exportId = exportId;
        this.warehouseId = warehouseId;
        this.accountId = accountId;
        this.exportDate = exportDate;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.reason = reason;
        this.note = note;
        this.totalSalePrice = totalSalePrice;
    }

    // Getters & Setters
    public int getExportId() {
        return exportId;
    }

    public void setExportId(int exportId) {
        this.exportId = exportId;
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

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getTotalSalePrice() {
        return totalSalePrice;
    }

    public void setTotalSalePrice(BigDecimal totalSalePrice) {
        this.totalSalePrice = totalSalePrice;
    }
}
