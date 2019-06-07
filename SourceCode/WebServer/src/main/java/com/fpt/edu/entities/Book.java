package com.fpt.edu.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.*;

@Entity
@Table(name = "book")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Book extends AbstractTimestampEntity implements Serializable {

    @Id
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "bookdetail_id")
    private BookDetail bookDetail;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> transactions;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Request> requests;

    // current keeper of the book
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "asset_id", updatable = false)
    @JsonIgnore
    private String assetId;

    @Column(name = "last_tx_id")
    @JsonIgnore
    private String lastTxId;

    @Column(name = "status")
    private String status = StatusType.IN_USE.value();

    @Transient
    @JsonIgnore
    private Map<String, String> asset;

    @Transient
    @JsonIgnore
    private Map<String, String> metadata;

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

    public Map<String, String> getAsset() {
        if (this.asset == null) {
            this.asset = new TreeMap<>();
        }
        this.asset.put("book_id", String.valueOf(this.id));
        return this.asset;
    }

    public Map<String, String> getMetadata() {
        if (this.metadata == null) {
            this.metadata = new TreeMap<>();
        }
        this.metadata.put("current_keeper", this.user.getEmail());
        this.metadata.put("status", this.status);
        this.metadata.put("transaction_timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        return this.metadata;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getLastTxId() {
        return lastTxId;
    }

    public void setLastTxId(String lastTxId) {
        this.lastTxId = lastTxId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookDetail getBookDetail() {
        return bookDetail;
    }

    public void setBookDetail(BookDetail bookDetail) {
        this.bookDetail = bookDetail;
    }
}