package edu.uta.groceryplanner.beans;

/**
 * Created by Vaibhav's Console on 11/24/2017.
 */

public class FriendsBean {
    private String friendId;
    private String friendName;
    private String oweStatus;
    private String owePrice;

    public FriendsBean(String friendId, String friendName, String oweStatus, String owePrice) {
        this.friendId = friendId;
        this.friendName = friendName;
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

    public String getOwePrice() {
        return owePrice;
    }

    public void setOwePrice(String owePrice) {
        this.owePrice = owePrice;
    }

    @Override
    public String toString() {
        return "FriendsBean{" +
                "friendId='" + friendId + '\'' +
                ", friendName='" + friendName + '\'' +
                ", oweStatus='" + oweStatus + '\'' +
                ", owePrice='" + owePrice + '\'' +
                '}';
    }
}
