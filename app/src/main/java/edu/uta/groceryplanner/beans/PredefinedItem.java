package edu.uta.groceryplanner.beans;

/**
 * Created by Vaibhav's Console on 11/28/2017.
 */

public class PredefinedItem {
    private String itemId;
    private String itemName;
    public PredefinedItem(){
    }
    public PredefinedItem(String itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "PredefinedItem{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
