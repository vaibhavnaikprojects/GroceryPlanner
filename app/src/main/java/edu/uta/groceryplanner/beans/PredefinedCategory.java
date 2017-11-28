package edu.uta.groceryplanner.beans;

import java.io.Serializable;

/**
 * Created by Vaibhav's Console on 11/27/2017.
 */

public class PredefinedCategory implements Serializable{
    private String categoryId;
    private String categoryName;

    public PredefinedCategory(){
    }
    public PredefinedCategory(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "PredefinedCategory{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
