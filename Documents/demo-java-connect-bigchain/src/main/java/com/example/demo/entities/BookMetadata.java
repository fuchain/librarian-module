package com.example.demo.entities;

public class BookMetadata {
    private String keeper;
    private int year;
    private String status;

    public BookMetadata(String keeper, int year, String status) {
        this.keeper = keeper;
        this.year = year;
        this.status = status;
    }

    public String getKeeper() {
        return keeper;
    }

    public void setKeeper(String keeper) {
        this.keeper = keeper;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
