package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ImportOrders {
    private int importId;
    private int supplierId;
    private int warehouseId;
    private int accountId;
    private Timestamp importDate; // ✅ Đổi sang Timestamp
    private String note;
    private BigDecimal totalCost;
    private String status;

    // ✅ Các trường bổ sung từ DB
    private String code;
    private Timestamp orderDeadline;
    private Timestamp expectedArrival;
    private Timestamp confirmDate;
    private String invoiceStatus;
    private String activityNote;

    public ImportOrders() {}

    public ImportOrders(int importId) {
        this.importId = importId;
    }

    public ImportOrders(int importId, int supplierId, int warehouseId, int accountId,
                        Timestamp importDate, String note, BigDecimal totalCost, String status,
                        String code, Timestamp orderDeadline, Timestamp expectedArrival, Timestamp confirmDate,
                        String invoiceStatus, String activityNote) {
        this.importId = importId;
        this.supplierId = supplierId;
        this.warehouseId = warehouseId;
        this.accountId = accountId;
        this.importDate = importDate;
        this.note = note;
        this.totalCost = totalCost;
        this.status = status;
        this.code = code;
        this.orderDeadline = orderDeadline;
        this.expectedArrival = expectedArrival;
        this.confirmDate = confirmDate;
        this.invoiceStatus = invoiceStatus;
        this.activityNote = activityNote;
    }

    // --- Getters & Setters ---
    public int getImportId() { return importId; }
    public void setImportId(int importId) { this.importId = importId; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public Timestamp getImportDate() { return importDate; }
    public void setImportDate(Timestamp importDate) { this.importDate = importDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ✅ Các trường bổ sung
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Timestamp getOrderDeadline() { return orderDeadline; }
    public void setOrderDeadline(Timestamp orderDeadline) { this.orderDeadline = orderDeadline; }

    public Timestamp getExpectedArrival() { return expectedArrival; }
    public void setExpectedArrival(Timestamp expectedArrival) { this.expectedArrival = expectedArrival; }

    public Timestamp getConfirmDate() { return confirmDate; }
    public void setConfirmDate(Timestamp confirmDate) { this.confirmDate = confirmDate; }

    public String getInvoiceStatus() { return invoiceStatus; }
    public void setInvoiceStatus(String invoiceStatus) { this.invoiceStatus = invoiceStatus; }

    public String getActivityNote() { return activityNote; }
    public void setActivityNote(String activityNote) { this.activityNote = activityNote; }
}
