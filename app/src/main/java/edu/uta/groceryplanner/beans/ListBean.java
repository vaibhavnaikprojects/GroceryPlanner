package edu.uta.groceryplanner.beans;

import java.util.List;

/**
 * Created by Vaibhav's Console on 10/29/2017.
 */

public class ListBean {
    private String listId;
    private String listName;
    private String createdDate;
    private String updatedDate;
    private String createUserId;
    private String listType;
    private int listGroupId;
    private String listGroupName;
    private List<ProductBean> productBeans;

    public ListBean(){

    }
    public ListBean(String listId, String listName, String createdDate, String updatedDate, String createUserId, String listType, int listGroupId, String listGroupName) {
        this.listId = listId;
        this.listName = listName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createUserId = createUserId;
        this.listType = listType;
        this.listGroupId = listGroupId;
        this.listGroupName = listGroupName;
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

    public List<ProductBean> getProductBeans() {
        return productBeans;
    }

    public void setProductBeans(List<ProductBean> productBeans) {
        this.productBeans = productBeans;
    }
}
