
package com.twilio.video.app.SearchResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {

    @SerializedName("users")
    @Expose
    private List<User> users = null;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("classes")
    @Expose
    private List<Class> classes = null;
    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;
    @SerializedName("user")
    @Expose
    private User_ user;
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;
    @SerializedName("company")
    @Expose
    private List<Company> company = null;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public User_ getUser() {
        return user;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public List<Company> getCompany() {
        return company;
    }

    public void setCompany(List<Company> company) {
        this.company = company;
    }

}
