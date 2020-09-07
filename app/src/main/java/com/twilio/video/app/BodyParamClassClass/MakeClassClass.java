package com.twilio.video.app.BodyParamClassClass;

import java.sql.Date;

public class MakeClassClass {
     String name,location,start_time,recurring_class,duration,
             about,free_class,fee;
     Date start_date,end_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getRecurring_class() {
        return recurring_class;
    }

    public void setRecurring_class(String recurring_class) {
        this.recurring_class = recurring_class;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFree_class() {
        return free_class;
    }

    public void setFree_class(String free_class) {
        this.free_class = free_class;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
