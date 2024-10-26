package com.example.bookflight.Model;

public abstract class BaseEntity {
    protected String id;

    public BaseEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    // Abstract method to be implemented by subclasses
    public abstract String getDisplayInfo();
}
