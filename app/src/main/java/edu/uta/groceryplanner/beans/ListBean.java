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
    private String listGroupId;
    private String listGroupName;
    private String intro;
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

    public String getListGroupId() {
        return listGroupId;
    }

    public void setListGroupId(String listGroupId) {
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
