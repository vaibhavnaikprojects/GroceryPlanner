package edu.uta.groceryplanner.beans;

/**
 * Created by Vaibhav's Console on 10/29/2017.
 */

public class ListBean {
    private int listId;
    private String listName;
    private String createdDate;
    private String updatedDate;
    private String createUserId;
    private String listType;
    private int listGroupId;
    private String listGroupName;
    private String intro;

    public ListBean(int listId, String listName, String createdDate, String updatedDate, String createUserId, String listType, int listGroupId, String listGroupName, String intro) {
        this.listId = listId;
        this.listName = listName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createUserId = createUserId;
        this.listType = listType;
        this.listGroupId = listGroupId;
        this.listGroupName = listGroupName;
        this.intro = intro;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
