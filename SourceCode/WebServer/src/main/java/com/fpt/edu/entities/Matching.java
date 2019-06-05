package com.fpt.edu.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "matching")
public class Matching extends AbstractTimestampEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Request returnerRequest;

    @OneToOne
    private Request borrowerRequest;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "pin")
    private String pin;

    @CreationTimestamp
    @Column(name = "matching_start_date", nullable = true)
    private Date matchingStartDate;

    @Column(name = "status")
    private int status;

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request getReturnerRequest() {
        return returnerRequest;
    }

    public void setReturnerRequest(Request returnerRequest) {
        this.returnerRequest = returnerRequest;
    }

    public Request getBorrowerRequest() {
        return borrowerRequest;
    }

    public void setBorrowerRequest(Request borrowerRequest) {
        this.borrowerRequest = borrowerRequest;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Date getMatchingStartDate() {
        return matchingStartDate;
    }

    public void setMatchingStartDate(Date matchingStartDate) {
        this.matchingStartDate = matchingStartDate;
    }
}
