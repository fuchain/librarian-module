package com.fpt.edu.exceptions;

public class EntityAldreayExisted extends  Exception {
    private long id;
    private String message;

    public EntityAldreayExisted(String message) {
        super(message);
    }

    public EntityAldreayExisted(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
