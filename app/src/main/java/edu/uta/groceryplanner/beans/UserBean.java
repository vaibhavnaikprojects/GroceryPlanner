package edu.uta.groceryplanner.beans;

/**
 * Created by Vaibhav's Console on 11/25/2017.
 */

public class UserBean {
    private String uid;
    private String userName;
    private String emailId;

    public UserBean() {
    }

    public UserBean(String uid, String userName, String emailId) {
        this.uid = uid;
        this.userName = userName;
        this.emailId = emailId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", emailId='" + emailId + '\'' +
                '}';
    }
}
