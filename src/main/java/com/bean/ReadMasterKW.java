package com.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "ReadMasterKW")
@Table(name = "read_master_kw")
public class ReadMasterKW {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "read_master_id")
    private long readMasterId;

    @Column(name = "meter_md")
    private BigDecimal meterMD;

    @Column(name = "multiplied_md")
    private BigDecimal multipliedMD;

    @Column(name = "billing_demand")
    private BigDecimal billingDemand;

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

    public BigDecimal getMeterMD() {
        if(this.meterMD != null){
            return new BigDecimal(String.valueOf(this.meterMD.doubleValue()));
        }
        return meterMD;
    }

    public void setMeterMD(BigDecimal meterMD) {
        this.meterMD = meterMD;
    }

    public BigDecimal getMultipliedMD() {
        if(this.multipliedMD != null){
            return new BigDecimal(String.valueOf(this.multipliedMD.doubleValue()));
        }
        return multipliedMD;
    }

    public void setMultipliedMD(BigDecimal multipliedMD) { this.multipliedMD = multipliedMD; }

    public BigDecimal getBillingDemand() {
        if(this.billingDemand != null){
            return new BigDecimal(String.valueOf(this.billingDemand.doubleValue()));
        }
        return billingDemand;
    }

    public void setBillingDemand(BigDecimal billingDemand) {
        this.billingDemand = billingDemand;
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
