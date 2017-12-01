package edu.uta.groceryplanner.beans;

/**
 * Created by Prakhar on 11/17/2017.
 */

public class StatisticsBean {

    private String productName;
    private double cost;
    private double percentage;

    public StatisticsBean(){}

    public StatisticsBean(String productName, double cost, double percentage) {
        this.productName = productName;
        this.cost = cost;
        this.percentage = percentage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
