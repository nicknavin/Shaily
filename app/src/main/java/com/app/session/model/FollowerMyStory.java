package com.app.session.model;

/**
 * Created by ITEGRITYPRO on 19-09-2018.
 */

public class FollowerMyStory {
    private String user_cd;
    private String follower_user_cd;
    private String imgUrl;
    private String imageUrl;
    private String user_name;
    private int no_of_unseen_stories;

    public String getUser_cd() {
        return user_cd;
    }

    public void setUser_cd(String user_cd) {
        this.user_cd = user_cd;
    }

    public String getFollower_user_cd() {
        return follower_user_cd;
    }

    public void setFollower_user_cd(String follower_user_cd) {
        this.follower_user_cd = follower_user_cd;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getNo_of_unseen_stories() {
        return no_of_unseen_stories;
    }

    public void setNo_of_unseen_stories(int no_of_unseen_stories) {
        this.no_of_unseen_stories = no_of_unseen_stories;
    }
}
