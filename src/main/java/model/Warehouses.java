package model;

public class Warehouses {

    private int warehouseId;
    private String name;
    private String location;

    public Warehouses() {
    }

    public Warehouses(int warehouseId, String name, String location) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.location = location;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Phương thức toString để hiển thị thông tin chi tiết của đối tượng
    @Override
    public String toString() {
        return "Warehouses{" +
                "warehouseId=" + warehouseId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}