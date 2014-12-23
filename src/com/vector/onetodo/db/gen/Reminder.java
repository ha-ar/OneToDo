package com.vector.onetodo.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table REMINDER.
 */
public class Reminder {

    private Long id;
    private Boolean is_time;
    private Boolean is_location;
    private Boolean is_alertNotification;
    private Boolean is_alertEmail;
    private String location;
    private Integer location_type;
    private Long time;

    public Reminder() {
    }

    public Reminder(Long id) {
        this.id = id;
    }

    public Reminder(Long id, Boolean is_time, Boolean is_location, Boolean is_alertNotification, Boolean is_alertEmail, String location, Integer location_type, Long time) {
        this.id = id;
        this.is_time = is_time;
        this.is_location = is_location;
        this.is_alertNotification = is_alertNotification;
        this.is_alertEmail = is_alertEmail;
        this.location = location;
        this.location_type = location_type;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIs_time() {
        return is_time;
    }

    public void setIs_time(Boolean is_time) {
        this.is_time = is_time;
    }

    public Boolean getIs_location() {
        return is_location;
    }

    public void setIs_location(Boolean is_location) {
        this.is_location = is_location;
    }

    public Boolean getIs_alertNotification() {
        return is_alertNotification;
    }

    public void setIs_alertNotification(Boolean is_alertNotification) {
        this.is_alertNotification = is_alertNotification;
    }

    public Boolean getIs_alertEmail() {
        return is_alertEmail;
    }

    public void setIs_alertEmail(Boolean is_alertEmail) {
        this.is_alertEmail = is_alertEmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getLocation_type() {
        return location_type;
    }

    public void setLocation_type(Integer location_type) {
        this.location_type = location_type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

}