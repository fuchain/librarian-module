package com.fpt.edu.entities;


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

	private static final String BC_BOOK_ID = "book_id";
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
    private Map<String, String> asset;

    @Transient
    @JsonIgnore
    private Map<String, String> metadata;

    @Transient
	@JsonSerialize
	private List bcTransactionList;

    @Transient
    private com.bigchaindb.model.Transaction bcLastTransaction;

	public Book() {
		this.status = EBookStatus.IN_USE.getValue();
		this.transferStatus = EBookTransferStatus.TRANSFERRED.getValue();
		this.asset = new TreeMap<>();
		this.metadata = new TreeMap<>();
		this.bcTransactionList = new ArrayList();
	}

	private boolean isNew() {
		// Check if this book is new to Bigchain
		return this.assetId == null && this.lastTxId == null;
	}

	public void setAsset(Map<String, String> asset) {
		this.asset = asset;
	}

	public Map<String, String> getAsset() {
		if (this.isNew() && this.asset.isEmpty()) {
			// This is default value of asset
			this.asset.put(BC_BOOK_ID, String.valueOf(this.id));
		}

        return this.asset;
    }

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

    public Map<String, String> getMetadata() {
		if (this.isNew() && this.metadata.isEmpty()) {
			// These are default values of metadata
			this.metadata.put(BC_CURRENT_KEEPER, this.user.getEmail());
			this.metadata.put(BC_BOOK_STATUS, this.status);
			this.metadata.put(BC_TX_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
			this.metadata.put(BC_REJECT_COUNT, String.valueOf(BC_MIN_REJECT_COUNT));
			this.metadata.put(BC_REJECT_REASON, "");
			this.metadata.put(BC_IMAGE_HASH, "");
		}

        return this.metadata;
    }

	public void setBcTransactionList(List<com.bigchaindb.model.Transaction> bcTransactionList) {
		this.bcTransactionList = bcTransactionList;
	}

	public List<com.bigchaindb.model.Transaction> getBcTransactionList() {
		return this.bcTransactionList;
	}

	public void setBcLastTransaction(com.bigchaindb.model.Transaction lastTransaction) {
		this.bcLastTransaction = lastTransaction;
	}

	public com.bigchaindb.model.Transaction getBcLastTransaction() {
		return this.bcLastTransaction;
    }

	public int getRejectCount()  {
		return Integer.parseInt(this.getMetadata().get(BC_REJECT_COUNT));
	}

	public boolean isRejectCountOver() {
		return this.getRejectCount() >= BC_MAX_REJECT_COUNT;
	}

	public void increaseRejectCount() {
		this.getMetadata().put(
			BC_REJECT_COUNT,
			String.valueOf(this.getRejectCount() + 1)
		);
	}

	public void setRejectReason(String reason) {
    	this.getMetadata().put(BC_REJECT_REASON, reason);
	}

	public void setRejectImage(String hash) {
		this.getMetadata().put(BC_IMAGE_HASH, hash);
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
