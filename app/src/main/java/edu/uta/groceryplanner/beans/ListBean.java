package edu.uta.groceryplanner.beans;

import java.io.Serializable;

/**
 * Created by Vaibhav's Console on 10/29/2017.
 */

public class ListBean implements Serializable{
    private String listId;
    private String listName;
    private String createdDate;
    private String updatedDate;
    private String createUserId;
    private String listType;
    private String listState;
    private int listGroupId;
    private String listGroupName;
    private int productCount;

    public ListBean(){

    }
    public ListBean(String listId, String listName, String createdDate, String updatedDate, String createUserId, String listType,String listState, int listGroupId, String listGroupName,int productCount) {
        this.listId = listId;
        this.listName = listName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createUserId = createUserId;
        this.listType = listType;
        this.listState=listState;
        this.listGroupId = listGroupId;
        this.listGroupName = listGroupName;
        this.productCount=productCount;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public int getListGroupId() {
        return listGroupId;
    }

    public void setListGroupId(int listGroupId) {
        this.listGroupId = listGroupId;
    }

    public String getListGroupName() {
        return listGroupName;
    }

    public void setListGroupName(String listGroupName) {
        this.listGroupName = listGroupName;
    }

    public int getProductCount() {
        return productCount;
    }
    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public String getListState() {
        return listState;
    }

    public void setListState(String listState) {
        this.listState = listState;
    }

    @Override
    public String toString() {
        return "ListBean{" +
                "listId='" + listId + '\'' +
                ", listName='" + listName + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", listType='" + listType + '\'' +
                ", listState='" + listState + '\'' +
                ", listGroupId=" + listGroupId +
                ", listGroupName='" + listGroupName + '\'' +
                ", productCount=" + productCount +
                '}';
    }
}
