package com.fpt.edu.entities;


import com.bigchaindb.model.Transactions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fpt.edu.common.EBookStatus;
import com.fpt.edu.common.EBookTransferStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.*;

@SuppressWarnings("unchecked")
@Entity
@Table(name = "book")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Book extends AbstractTimestampEntity implements Serializable {

	private static final String BC_CURRENT_KEEPER = "current_keeper";
	private static final String BC_BOOK_STATUS = "status";
	private static final String BC_TX_TIMESTAMP = "transaction_timestamp";
	private static final String BC_REJECT_COUNT = "reject_count";
	private static final String BC_REJECT_REASON = "reject_reason";
	private static final String BC_IMAGE_HASH = "img_hash";

	private static final int BC_MAX_REJECT_COUNT = 5;
	private static final int BC_MIN_REJECT_COUNT = 0;

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
    private String status;

    @Column(name = "transfer_status")
    @JsonSerialize
	private String transferStatus;

    @Transient
    @JsonIgnore
    private final Map<String, String> asset;

    @Transient
    @JsonIgnore
    private final Map<String, String> metadata;

    @Transient
	@JsonSerialize
	private final List bcTransactions;

	public Book() {
		this.status = EBookStatus.IN_USE.getValue();
		this.transferStatus = EBookTransferStatus.TRANSFERRED.getValue();
		this.asset = new TreeMap<>();
		this.metadata = new TreeMap<>();
		this.bcTransactions = new ArrayList();
	}

	public Map<String, String> getAsset() {
        this.asset.put("book_id", String.valueOf(this.id));
        return this.asset;
    }

    public Map<String, String> getMetadata() {
        this.metadata.put(BC_CURRENT_KEEPER, this.user.getEmail());
        this.metadata.put(BC_BOOK_STATUS, this.status);
        this.metadata.put(BC_TX_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
        this.metadata.put(BC_REJECT_COUNT, String.valueOf(BC_MIN_REJECT_COUNT));
        this.metadata.put(BC_REJECT_REASON, "");
        this.metadata.put(BC_IMAGE_HASH, "");

        return this.metadata;
    }

	public List getBcTransactions() {
		return bcTransactions;
	}

	public void setBcTransactions(Transactions bcTransactions) {
    	this.bcTransactions.clear();
		for (com.bigchaindb.model.Transaction bcTransaction : bcTransactions.getTransactions()) {
			this.bcTransactions.add(bcTransaction.getMetaData());
		}
	}

	public boolean isRejectCountOver() {
		int rejectCount = Integer.parseInt(this.metadata.get(BC_REJECT_COUNT));
		return rejectCount >= BC_MAX_REJECT_COUNT;
	}

	public void setRejectCount() {
		int rejectCount = Integer.parseInt(this.metadata.get(BC_REJECT_COUNT));
		this.metadata.put(BC_REJECT_COUNT, String.valueOf(rejectCount + 1));
	}

	public void setRejectReason(String reason) {
    	this.metadata.put(BC_REJECT_REASON, reason);
	}

	public void setRejectImage(String hash) {
		this.metadata.put(BC_IMAGE_HASH, hash);
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
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
