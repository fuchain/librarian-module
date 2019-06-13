package com.fpt.edu.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fpt.edu.configs.CustomLocalDateTimeSerializer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractTimestampEntity {

    @CreationTimestamp
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Column(name = "created_date", nullable = true)
	private Date createDate;
    @UpdateTimestamp
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @Column(name = "update_date")
    private Date updateDate;

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
