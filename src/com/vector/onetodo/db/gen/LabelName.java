package com.vector.onetodo.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table LABEL_NAME.
 */
public class LabelName {

    private Long id;
    private String name;

    public LabelName() {
    }

    public LabelName(Long id) {
        this.id = id;
    }

    public LabelName(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}