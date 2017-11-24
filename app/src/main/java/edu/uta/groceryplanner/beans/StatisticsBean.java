package edu.uta.groceryplanner.beans;

/**
 * Created by Prakhar on 11/17/2017.
 */

public class StatisticsBean {

    private String productName;
    private int cost;
    private int percentage;

    public StatisticsBean(){}

    public StatisticsBean(String productName, int cost, int percentage) {
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
