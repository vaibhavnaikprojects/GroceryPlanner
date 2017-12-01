package edu.uta.groceryplanner.beans;

/**
 * Created by Vaibhav's Console on 11/27/2017.
 */

public class GroupBean {
    private String groupId;
    private String groupName;

    public GroupBean() {
    }

    public GroupBean(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
