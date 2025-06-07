package model;

public class Suppliers {
    private int supplierId;
    private String name;
    private String phone;
    private String address;

    // Default Constructor
    public Suppliers() {
    }

    // Parameterized Constructor
    public Suppliers(int supplierId, String name, String phone, String address) {
        this.supplierId = supplierId;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // toString() method
    @Override
    public String toString() {
        return "Suppliers{" +
                "supplierId=" + supplierId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}