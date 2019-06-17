package com.fpt.edu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fpt.edu.common.enums.EBookStatus;
import com.fpt.edu.common.enums.EBookTransferStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Entity
@Table(name = "book")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Book extends AbstractTimestampEntity implements Serializable {

	@Transient
	@JsonSerialize
	private final List bcTransactionList;
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
	private BookAsset bookAsset;
	@Transient
	@JsonIgnore
	private BookMetadata bookMetadata;
	@Transient
	@JsonSerialize
	private com.bigchaindb.model.Transaction bcLastTransaction;

	public Book() {
		this.status = EBookStatus.IN_USE.getValue();
		this.transferStatus = EBookTransferStatus.TRANSFERRED.getValue();
		this.bcTransactionList = new ArrayList();
		if (this.isNewToBigchain()) {
			this.bookAsset = new BookAsset();
			this.bookMetadata = new BookMetadata();
		} else {
			this.bookAsset = new BookAsset(String.valueOf(this.id));
			this.bookMetadata = new BookMetadata(this.user.getEmail(), this.status);
		}
	}

	private boolean isNewToBigchain() {
		return this.assetId == null;
	}

	public BookAsset getAsset() {
		return this.bookAsset;
	}

	public void setAsset(BookAsset bookAsset) {
		this.bookAsset = bookAsset;
	}

	public BookMetadata getMetadata() {
		return this.bookMetadata;
	}

	public void setMetadata(BookMetadata bookMetadata) {
		this.bookMetadata = bookMetadata;
	}

	public List<com.bigchaindb.model.Transaction> getBcTransactionList() {
		return this.bcTransactionList;
	}

	public void setBcTransactionList(List<com.bigchaindb.model.Transaction> bcTransactionList) {
		this.bcTransactionList.clear();
		for (com.bigchaindb.model.Transaction bcTransaction : bcTransactionList) {
			this.bcTransactionList.add(bcTransaction.getMetaData());
		}
	}

	public com.bigchaindb.model.Transaction getBcLastTransaction() {
		return this.bcLastTransaction;
	}

	public void setBcLastTransaction(com.bigchaindb.model.Transaction lastTransaction) {
		this.bcLastTransaction = lastTransaction;
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
		this.bookMetadata.setStatus(status);
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
		this.bookMetadata.setCurrentKeeper(user.getEmail());
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
