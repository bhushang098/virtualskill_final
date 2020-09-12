
package com.twilio.video.app.JobsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FindJobsRespose {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("jobs")
    @Expose
    private Jobs jobs;
    @SerializedName("posted_jobs")
    @Expose
    private PostedJobs postedJobs;
    @SerializedName("applied_jobs")
    @Expose
    private AppliedJobs appliedJobs;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Jobs getJobs() {
        return jobs;
    }

    public void setJobs(Jobs jobs) {
        this.jobs = jobs;
    }

    public PostedJobs getPostedJobs() {
        return postedJobs;
    }

    public void setPostedJobs(PostedJobs postedJobs) {
        this.postedJobs = postedJobs;
    }

    public AppliedJobs getAppliedJobs() {
        return appliedJobs;
    }

    public void setAppliedJobs(AppliedJobs appliedJobs) {
        this.appliedJobs = appliedJobs;
    }

}
