package com.vector.onetodo.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table REMINDER.
 */
public class Reminder {

    private Long id;
    private Boolean is_time_location;
    private Boolean is_alertNotification;
    private Boolean is_alertEmail;
    private String location;
    private String location_tag;
    private Integer location_type;
    private Long time;

    public Reminder() {
    }

    public Reminder(Long id) {
        this.id = id;
    }

    public Reminder(Long id, Boolean is_time_location, Boolean is_alertNotification, Boolean is_alertEmail, String location, String location_tag, Integer location_type, Long time) {
        this.id = id;
        this.is_time_location = is_time_location;
        this.is_alertNotification = is_alertNotification;
        this.is_alertEmail = is_alertEmail;
        this.location = location;
        this.location_tag = location_tag;
        this.location_type = location_type;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIs_time_location() {
        return is_time_location;
    }

    public void setIs_time_location(Boolean is_time_location) {
        this.is_time_location = is_time_location;
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

    public String getLocation_tag() {
        return location_tag;
    }

    public void setLocation_tag(String location_tag) {
        this.location_tag = location_tag;
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
