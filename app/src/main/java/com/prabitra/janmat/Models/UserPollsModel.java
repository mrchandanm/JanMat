package com.prabitra.janmat.Models;

public class UserPollsModel {
    int pollselected;
    Boolean isvisible;

    public UserPollsModel(int pollselected, Boolean isvisible) {
        this.pollselected = pollselected;
        this.isvisible = isvisible;
    }

    public UserPollsModel() {
    }

    public int getPollselected() {
        return pollselected;
    }

    public void setPollselected(int pollselected) {
        this.pollselected = pollselected;
    }

    public Boolean getIsvisible() {
        return isvisible;
    }

    public void setIsvisible(Boolean isvisible) {
        this.isvisible = isvisible;
    }

    @Override
    public String toString() {
        return "UserPollsModel{" +
                "pollselected=" + pollselected +
                ", isvisible=" + isvisible +
                '}';
    }
}
