package model;

import java.math.BigDecimal;

public class Products {
    private int productId;
    private String productCode;
    private String name;
    private String description;
    private String size;
    private String color;
    private String material;
    private String unit;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private int status;
    private String image;
    private int minStockLevel;

    // Default constructor
    public Products() {
    }

    // Parameterized constructor
    public Products(int productId, String productCode, String name, String description, String size, String color,
                    String material, String unit, BigDecimal costPrice, BigDecimal salePrice, int status,
                    String image, int minStockLevel) {
        this.productId = productId;
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.size = size;
        this.color = color;
        this.material = material;
        this.unit = unit;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.status = status;
        this.image = image;
        this.minStockLevel = minStockLevel;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    // toString() for debugging
    @Override
    public String toString() {
        return "Products{" +
                "productId=" + productId +
                ", productCode='" + productCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", material='" + material + '\'' +
                ", unit='" + unit + '\'' +
                ", costPrice=" + costPrice +
                ", salePrice=" + salePrice +
                ", status=" + status +
                ", image='" + image + '\'' +
                ", minStockLevel=" + minStockLevel +
                '}';
    }
}