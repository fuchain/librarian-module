package com.fpt.edu.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Request returner_req_id;

    @OneToOne
    private Request borrower_req_id;

    @Column(name = "pin")
    private String pin;

    @CreationTimestamp
    @Column(name = "created_at", nullable = true)
    private Date matchingStartDate;

    private int status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request getReturner_req_id() {
        return returner_req_id;
    }

    public void setReturner_req_id(Request returner_req_id) {
        this.returner_req_id = returner_req_id;
    }

    public Request getBorrower_req_id() {
        return borrower_req_id;
    }

    public void setBorrower_req_id(Request borrower_req_id) {
        this.borrower_req_id = borrower_req_id;
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
