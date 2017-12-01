package edu.uta.groceryplanner.beans;

import java.io.Serializable;
/**
 * Created by Vaibhav's Console on 10/30/2017.
 */

public class ProductBean implements Serializable{
    private String productId;
    private String productName;
    private String productTypeId;
    private String productTypeName;
    private String quantity;
    private double rate;
    private double cost;
    private String status;

    public ProductBean(){

    }

    public ProductBean( String productName){
        this.productName=productName;
    }
    public ProductBean(String productId, String productName, String productTypeId, String quantity,String status) {
        this.productId = productId;
        this.productName = productName;
        this.productTypeId = productTypeId;
        this.quantity = quantity;
        this.status=status;
    }

    public ProductBean(String productId, String productName, String productTypeId, String quantity, double rate, double cost,String status) {
        this.productId = productId;
        this.productName = productName;
        this.productTypeId = productTypeId;
        this.quantity = quantity;
        this.rate = rate;
        this.cost = cost;
        this.status=status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductBean that = (ProductBean) o;

        return productName != null ? productName.equals(that.productName) : that.productName == null;
    }

    @Override
    public int hashCode() {
        return productName != null ? productName.hashCode() : 0;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
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
