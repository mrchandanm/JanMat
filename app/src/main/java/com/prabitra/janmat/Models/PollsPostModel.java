package com.prabitra.janmat.Models;

import java.util.ArrayList;

public class PollsPostModel {
    String userName;
    String title;
    long time;
    ArrayList<PollsModel> pollsModels;
    String pollId;
    String youtubevideoId;
    ArrayList<String> photos;

    public PollsPostModel(String userName,String pollId, String title, long time, ArrayList<PollsModel> pollsModels,String youtubevideoId,ArrayList<String> photos) {
        this.userName = userName;
        this.title = title;
        this.time = time;
        this.pollsModels = pollsModels;
        this.pollId=pollId;
        this.youtubevideoId=youtubevideoId;
        this.photos=photos;

    }

    public PollsPostModel() {
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getYoutubevideoId() {
        return youtubevideoId;
    }

    public void setYoutubevideoId(String youtubevideoId) {
        this.youtubevideoId = youtubevideoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollsId) {
        this.pollId = pollsId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<PollsModel> getPollsModels() {
        return pollsModels;
    }

    public void setPollsModels(ArrayList<PollsModel> pollsModels) {
        this.pollsModels = pollsModels;
    }

    @Override
    public String toString() {
        return "PollsPostModel{" +
                "userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", pollsModels=" + pollsModels +
                ", pollId='" + pollId + '\'' +
                ", youtubevideoId='" + youtubevideoId + '\'' +
                ", photos=" + photos +
                '}';
    }
}
