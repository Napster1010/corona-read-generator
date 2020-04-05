package com.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "ReadMasterPF")
@Table(name = "read_master_pf")
public class ReadMasterPF {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "read_master_id")
    private long readMasterId;

    @Column(name = "meter_pf")
    private BigDecimal meterPF;

    @Column(name = "billing_pf")
    private BigDecimal billingPF;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReadMasterId() {
        return readMasterId;
    }

    public void setReadMasterId(long readMasterId) {
        this.readMasterId = readMasterId;
    }

    public BigDecimal getMeterPF() {
        if(this.meterPF != null){
            return new BigDecimal(String.valueOf(this.meterPF.doubleValue()));
        }
        return meterPF;
    }

    public void setMeterPF(BigDecimal meterPF) {
        this.meterPF = meterPF;
    }

    public BigDecimal getBillingPF() {
        if(this.billingPF != null){
            return new BigDecimal(String.valueOf(this.billingPF.doubleValue()));
        }
        return billingPF;
    }

    public void setBillingPF(BigDecimal billingPF) {
        this.billingPF = billingPF;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}