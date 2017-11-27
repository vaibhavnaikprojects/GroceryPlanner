package edu.uta.groceryplanner.beans;

/**
 * Created by Vaibhav's Console on 10/30/2017.
 */

public class ProductBean {
    private int productId;
    private String productName;
    private int productTypeId;
    private String productTypeName;
    private String quantity;
    private double rate;
    private double cost;
    private String status;

    public ProductBean(){

    }
    public ProductBean(int productId, String productName, int productTypeId, String quantity,String status) {
        this.productId = productId;
        this.productName = productName;
        this.productTypeId = productTypeId;
        this.quantity = quantity;
        this.status=status;
    }

    public ProductBean(int productId, String productName, int productTypeId, String quantity, double rate, double cost,String status) {
        this.productId = productId;
        this.productName = productName;
        this.productTypeId = productTypeId;
        this.quantity = quantity;
        this.rate = rate;
        this.cost = cost;
        this.status=status;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductBean{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productTypeId=" + productTypeId +
                ", productTypeName='" + productTypeName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", rate=" + rate +
                ", cost=" + cost +
                ", status='" + status + '\'' +
                '}';
    }
}
