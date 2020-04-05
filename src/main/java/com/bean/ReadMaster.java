package com.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "ReadMaster")
@Table(name = "read_master")
public class ReadMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "bill_month")
    private String billMonth;

    @Column(name = "group_no")
    private String groupNo;

    @Column(name = "reading_diary_no")
    private String readingDiaryNo;

    @Column(name = "consumer_no")
    private String consumerNo;

    @Column(name = "meter_identifier")
    private String meterIdentifier;

    @Column(name = "reading_date")
    @Temporal(TemporalType.DATE)
    private Date readingDate;

    @Column(name = "reading_type")
    private String readingType;

    @Column(name = "meter_status")
    private String meterStatus;

    @Column(name = "replacement_flag")
    private String replacementFlag;

    @Column(name = "source")
    private String source;

    @Column(name = "reading")
    private BigDecimal reading;

    @Column(name = "difference")
    private BigDecimal difference;

    @Column(name = "mf")
    private BigDecimal mf;

    @Column(name = "consumption")
    private BigDecimal consumption;

    @Column(name = "assessment")
    private BigDecimal assessment;

    @Column(name = "propagated_assessment")
    private BigDecimal propagatedAssessment;

    @Column(name = "total_consumption")
    private BigDecimal totalConsumption;

    @Column(name = "used_on_bill")
    private boolean usedOnBill;

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

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getReadingDiaryNo() {
        return readingDiaryNo;
    }

    public void setReadingDiaryNo(String readingDiaryNo) {
        this.readingDiaryNo = readingDiaryNo;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getMeterIdentifier() {
        return meterIdentifier;
    }

    public void setMeterIdentifier(String meterIdentifier) {
        this.meterIdentifier = meterIdentifier;
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }

    public String getReadingType() {
        return readingType;
    }

    public void setReadingType(String readingType) {
        this.readingType = readingType;
    }

    public String getMeterStatus() {
        return meterStatus;
    }

    public void setMeterStatus(String meterStatus) {
        this.meterStatus = meterStatus;
    }

    public String getReplacementFlag() {
        return replacementFlag;
    }

    public void setReplacementFlag(String replacementFlag) {
        this.replacementFlag = replacementFlag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BigDecimal getReading() {
        if(this.reading != null){
            return new BigDecimal(String.valueOf(this.reading.doubleValue()));
        }
        return reading;
    }

    public void setReading(BigDecimal reading) {
        this.reading = reading;
    }

    public BigDecimal getDifference() {
        if(this.difference != null){
            return new BigDecimal(String.valueOf(this.difference.doubleValue()));
        }
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getMf() {
        if(this.mf != null){
            return new BigDecimal(String.valueOf(this.mf.doubleValue()));
        }
        return mf;
    }

    public void setMf(BigDecimal mf) {
        this.mf = mf;
    }

    public BigDecimal getConsumption() {
        if(this.consumption != null){
            return new BigDecimal(String.valueOf(this.consumption.doubleValue()));
        }
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public BigDecimal getAssessment() {
        if(this.assessment != null){
            return new BigDecimal(String.valueOf(this.assessment.doubleValue()));
        }
        return assessment;
    }

    public void setAssessment(BigDecimal assessment) {
        this.assessment = assessment;
    }

    public BigDecimal getPropagatedAssessment() {
        if(this.propagatedAssessment != null){
            return new BigDecimal(String.valueOf(this.propagatedAssessment.doubleValue()));
        }
        return propagatedAssessment;
    }

    public void setPropagatedAssessment(BigDecimal propagatedAssessment) {
        this.propagatedAssessment = propagatedAssessment;
    }

    public BigDecimal getTotalConsumption() {
        if(this.totalConsumption != null){
            return new BigDecimal(String.valueOf(this.totalConsumption.doubleValue()));
        }
        return totalConsumption;
    }

    public void setTotalConsumption(BigDecimal totalConsumption) {
        this.totalConsumption = totalConsumption;
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

    public boolean isUsedOnBill() {
        return usedOnBill;
    }

    public void setUsedOnBill(boolean usedOnBill) {
        this.usedOnBill = usedOnBill;
    }
}
