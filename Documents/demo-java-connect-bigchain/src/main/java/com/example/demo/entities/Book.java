package com.example.demo.entities;

public class Book {
    private String name;
    private String  author;
    private String category;
    private String publisher;
    private String description;

    public Book(String name, String author, String category, String publisher, String description) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
