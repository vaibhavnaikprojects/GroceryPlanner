package edu.uta.groceryplanner.beans;

import java.util.List;

/**
 * Created by Vaibhav's Console on 11/27/2017.
 */

public class GroupBean {
    private String groupId;
    private String groupName;
    private List<UserBean> userBeans;
    private String createdDate;
    private String createdUser;
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

    public List<UserBean> getUserBeans() {
        return userBeans;
    }

    public void setUserBeans(List<UserBean> userBeans) {
        this.userBeans = userBeans;
    }
}
