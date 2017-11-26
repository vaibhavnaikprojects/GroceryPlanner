package edu.uta.groceryplanner.beans;

/**
 * Created by Vaibhav's Console on 11/24/2017.
 */

public class FriendsBean {
    private String friendId;
    private String friendName;
    private String friendEmail;
    private String oweStatus;
    private double owePrice;

    public FriendsBean() {
    }

    public FriendsBean(String friendId, String friendName, String friendEmail, String oweStatus, double owePrice) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.friendEmail=friendEmail;
        this.oweStatus = oweStatus;
        this.owePrice = owePrice;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getOweStatus() {
        return oweStatus;
    }

    public void setOweStatus(String oweStatus) {
        this.oweStatus = oweStatus;
    }

    public double getOwePrice() {
        return owePrice;
    }

    public void setOwePrice(double owePrice) {
        this.owePrice = owePrice;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    @Override
    public String toString() {
        return "FriendsBean{" +
                "friendId='" + friendId + '\'' +
                ", friendName='" + friendName + '\'' +
                ", friendEmail='" + friendEmail + '\'' +
                ", oweStatus='" + oweStatus + '\'' +
                ", owePrice='" + owePrice + '\'' +
                '}';
    }
}
