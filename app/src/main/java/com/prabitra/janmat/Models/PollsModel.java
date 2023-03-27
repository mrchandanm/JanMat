package com.prabitra.janmat.Models;

public class PollsModel {
    String pollOptions;
    String pollPercentage;
    int progress;
    Boolean ispolled=false;

    public PollsModel(String pollOptions, String pollPercentage, int progress,Boolean ispolled) {
        this.pollOptions = pollOptions;
        this.pollPercentage = pollPercentage;
        this.progress = progress;
        this.ispolled=ispolled;
    }

    public PollsModel() {
    }

    public Boolean getIspolled() {
        return ispolled;
    }

    public void setIspolled(Boolean ispolled) {
        this.ispolled = ispolled;
    }

    public String getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(String pollOptions) {
        this.pollOptions = pollOptions;
    }

    public String getPollPercentage() {
        return pollPercentage;
    }

    public void setPollPercentage(String pollPercentage) {
        this.pollPercentage = pollPercentage;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "PollsModel{" +
                "pollOptions='" + pollOptions + '\'' +
                ", pollPercentage='" + pollPercentage + '\'' +
                ", progress=" + progress +
                ", ispolled=" + ispolled +
                '}';
    }
}
