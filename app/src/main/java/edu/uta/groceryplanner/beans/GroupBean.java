package edu.uta.groceryplanner.beans;

import java.io.Serializable;

/**
 * Created by Vaibhav's Console on 11/27/2017.
 */

public class GroupBean implements Serializable{
    private String groupId;
    private String groupName;
    private int peopleCount;
    public GroupBean() {
    }

    public GroupBean(String groupId, String groupName,int peopleCount) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.peopleCount=peopleCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupBean groupBean = (GroupBean) o;

        return groupName != null ? groupName.equals(groupBean.groupName) : groupBean.groupName == null;
    }

    @Override
    public int hashCode() {
        return groupName != null ? groupName.hashCode() : 0;
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

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }
}
