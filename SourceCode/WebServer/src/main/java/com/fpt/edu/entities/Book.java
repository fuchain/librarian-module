package com.fpt.edu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fpt.edu.common.enums.EBookStatus;
import com.fpt.edu.common.enums.EBookTransferStatus;
import com.fpt.edu.configs.CustomLocalDateTimeSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "bookdetail_id")
	private BookDetail bookDetail;
	@OneToMany(cascade = CascadeType.PERSIST)
	@JsonIgnore
	private List<Request> requests;
	// current keeper of the book
	@ManyToOne()
	@JoinColumn(name = "user_id", nullable = true)
	@JsonIgnore
	private User user;
	@Column(name = "asset_id")
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
	// for tracking the when the new reader receive the book
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Column(name = "last_transfer_success_date")
	private Date lastTransferSuccess;

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
	}

	private boolean isNewToBigchain() {
		return this.assetId == null;
	}

	public BookAsset getAsset() {
		if (this.bookAsset == null) {
			if (this.isNewToBigchain()) {
				this.bookAsset = new BookAsset();
			} else {
				this.bookAsset = new BookAsset(String.valueOf(this.id));
			}
		}
		return this.bookAsset;
	}

	public void setAsset(BookAsset bookAsset) {
		this.bookAsset = bookAsset;
	}

	public BookMetadata getMetadata() {
		if (this.bookMetadata == null) {
			if (this.isNewToBigchain()) {
				this.bookMetadata = new BookMetadata();
			} else {
				this.bookMetadata = new BookMetadata(this.user.getEmail(), this.status);
			}
		}
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
		if (this.getAssetId() == null || this.getAssetId().isEmpty()) {
			this.assetId = assetId;
		}
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
		this.getMetadata().setStatus(status);
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
		if (user != null) {
			if (this.getUser() != null && this.getUser().getListBooks().equals(user.getId())) {
				this.setLastTransferSuccess(new Date());
			}
			this.user = user;
			this.getMetadata().setCurrentKeeper(user.getEmail());
		} else {
			// this code handle for case remove book. that mean book doesn't keeping by anyone
			this.user = null;
		}
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

	public Date getLastTransferSuccess() {
		return lastTransferSuccess;
	}

	public void setLastTransferSuccess(Date lastTransferSuccess) {
		this.lastTransferSuccess = lastTransferSuccess;
	}

}
