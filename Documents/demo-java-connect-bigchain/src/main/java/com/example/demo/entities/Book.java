package com.example.demo.entities;

import java.util.Map;
import java.util.TreeMap;

public class Book {

    public static enum StatusType {
        IN_USE("in use"),
        DAMAGED("damanged");

        private String value;

        StatusType(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    // These fields are for asset
    private int id;
    private String name;
    private String author;
    private String category;
    private String publisher;
    private String description;
    private int publishYear;

    // These fields are for metadata
    private String status;
    private String keeper;

    Map<String, String> asset;
    Map<String, String> metadata;

    public Book(
            int id,
            String name,
            String author,
            String category,
            String publisher,
            String description,
            int publishYear,
            String keeper
    ) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.description = description;
        this.publishYear = publishYear;

        this.keeper = keeper;
        this.status = StatusType.IN_USE.value();

        this.asset = setAsset();
        this.metadata = setMetadata();
    }

    private Map<String, String> setAsset() {
        Map<String, String> asset = new TreeMap<>();
        asset.put("id", String.valueOf(this.id));
        asset.put("name", this.name);
        asset.put("author", this.author);
        asset.put("category", this.category);
        asset.put("publisher", this.publisher);
        asset.put("description", this.description);
        return asset;
    }

    public Map<String, String> getAsset() {
        return this.asset;
    }

    private Map<String, String> setMetadata() {
        Map<String, String> metadata = new TreeMap<>();
        metadata.put("current_keeper", String.valueOf(this.id));
        metadata.put("status", this.status);
        return metadata;
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.metadata.replace("status", status);
    }

    public String getKeeper() {
        return keeper;
    }

    public void setKeeper(String keeper) {
        this.keeper = keeper;
        this.metadata.replace("current_keeper", keeper);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDescription() {
        return description;
    }

    public int getPublishYear() {
        return publishYear;
    }
}
