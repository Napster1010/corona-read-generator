package com.bean;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "ConsumerConnectionInformation")
@Table(name = "consumer_connection_information")
public class ConsumerConnectionInformation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "consumer_no")
    private String consumerNo;

    @Column(name = "tariff_category")
    private String tariffCategory;

    @Column(name = "connection_type")
    private String connectionType;

    @Column(name = "metering_status")
    private String meteringStatus;

    @Column(name = "premise_type")
    private String premiseType;

    @Column(name = "sanctioned_load")
    private BigDecimal sanctionedLoad;

    @Column(name = "sanctioned_load_unit")
    private String sanctionedLoadUnit;

    @Column(name = "contract_demand")
    private BigDecimal contractDemand;

    @Column(name = "contract_demand_unit")
    private String contractDemandUnit;

    @Column(name = "is_seasonal")
    private boolean isSeasonal;

    @Column(name = "purpose_of_installation_id")
    private Long purposeOfInstallationId;

    @Column(name = "purpose_of_installation")
    private String purposeOfInstallation;

    @Column(name = "tariff_code")
    private String tariffCode;

    @Column(name = "sub_category_code")
    private long subCategoryCode;

    @Column(name = "phase")
    private String phase;

    @Column(name = "is_government")
    private boolean isGovernment;

    @Column(name = "plot_size")
    private BigDecimal plotSize;

    @Column(name = "plot_size_unit")
    private String plotSizeUnit;

    @Column(name = "location_code")
    private String locationCode;

    @Column(name = "is_xray")
    private boolean isXray;

    @Column(name = "is_welding_transformer_surcharge")
    private boolean isWeldingTransformerSurcharge;

    @Column(name = "is_capacitor_surcharge")
    private boolean isCapacitorSurcharge;

    @Column(name = "is_demandside")
    private boolean isDemandside;

    @Column(name = "is_beneficiary")
    private boolean isBeneficiary;

    @Column(name = "connection_date")
    @Temporal(TemporalType.DATE)
    private Date connectionDate;

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

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getTariffCategory() {
        return tariffCategory;
    }

    public void setTariffCategory(String tariffCategory) {
        this.tariffCategory = tariffCategory;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getMeteringStatus() {
        return meteringStatus;
    }

    public void setMeteringStatus(String meteringStatus) {
        this.meteringStatus = meteringStatus;
    }

    public String getPremiseType() {
        return premiseType;
    }

    public void setPremiseType(String premiseType) {
        this.premiseType = premiseType;
    }

    public BigDecimal getSanctionedLoad() {
        if(this.sanctionedLoad != null){
            return new BigDecimal(String.valueOf(this.sanctionedLoad.doubleValue()));
        }
        return sanctionedLoad;
    }

    public void setSanctionedLoad(BigDecimal sanctionedLoad) {
        this.sanctionedLoad = sanctionedLoad;
    }

    public String getSanctionedLoadUnit() {
        return sanctionedLoadUnit;
    }

    public void setSanctionedLoadUnit(String sanctionedLoadUnit) {
        this.sanctionedLoadUnit = sanctionedLoadUnit;
    }

    public BigDecimal getContractDemand() {
        if(this.contractDemand != null){
            return new BigDecimal(String.valueOf(this.contractDemand.doubleValue()));
        }
        return contractDemand;
    }

    public void setContractDemand(BigDecimal contractDemand) {
        this.contractDemand = contractDemand;
    }

    public String getContractDemandUnit() {
        return contractDemandUnit;
    }

    public void setContractDemandUnit(String contractDemandUnit) {
        this.contractDemandUnit = contractDemandUnit;
    }

    public boolean getIsSeasonal() {
        return isSeasonal;
    }

    public void setIsSeasonal(boolean isSeasonal) {
        this.isSeasonal = isSeasonal;
    }

    public Long getPurposeOfInstallationId() {
        return purposeOfInstallationId;
    }

    public void setPurposeOfInstallationId(Long purposeOfInstallationId) {
        this.purposeOfInstallationId = purposeOfInstallationId;
    }

    public String getPurposeOfInstallation() {
        return purposeOfInstallation;
    }

    public void setPurposeOfInstallation(String purposeOfInstallation) {
        this.purposeOfInstallation = purposeOfInstallation;
    }

    public String getTariffCode() {
        return tariffCode;
    }

    public void setTariffCode(String tariffCode) {
        this.tariffCode = tariffCode;
    }

    public long getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(long subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public boolean getIsGovernment() {
        return isGovernment;
    }

    public void setIsGovernment(boolean isGovernment) {
        this.isGovernment = isGovernment;
    }

    public BigDecimal getPlotSize() {
        if(this.plotSize != null){
            return new BigDecimal(String.valueOf(this.plotSize.doubleValue()));
        }
        return plotSize;
    }

    public void setPlotSize(BigDecimal plotSize) {
        this.plotSize = plotSize;
    }

    public String getPlotSizeUnit() {
        return plotSizeUnit;
    }

    public void setPlotSizeUnit(String plotSizeUnit) {
        this.plotSizeUnit = plotSizeUnit;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public boolean getIsXray() {
        return isXray;
    }

    public void setIsXray(boolean isXray) {
        this.isXray = isXray;
    }

    public boolean getIsWeldingTransformerSurcharge() {
        return isWeldingTransformerSurcharge;
    }

    public void setIsWeldingTransformerSurcharge(boolean isWeldingTransformerSurcharge) {
        this.isWeldingTransformerSurcharge = isWeldingTransformerSurcharge;
    }

    public boolean getIsCapacitorSurcharge() {
        return isCapacitorSurcharge;
    }

    public void setIsCapacitorSurcharge(boolean isCapacitorSurcharge) {
        this.isCapacitorSurcharge = isCapacitorSurcharge;
    }

    public boolean getIsDemandside() {
        return isDemandside;
    }

    public void setIsDemandside(boolean isDemandside) {
        this.isDemandside = isDemandside;
    }

    public boolean getIsBeneficiary() {
        return isBeneficiary;
    }

    public void setIsBeneficiary(boolean isBeneficiary) {
        this.isBeneficiary = isBeneficiary;
    }

    public Date getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "ConsumerConnectionInformation{" +
                "id=" + id +
                ", consumerNo='" + consumerNo + '\'' +
                ", tariffCategory='" + tariffCategory + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ", meteringStatus='" + meteringStatus + '\'' +
                ", premiseType='" + premiseType + '\'' +
                ", sanctionedLoad=" + sanctionedLoad +
                ", sanctionedLoadUnit='" + sanctionedLoadUnit + '\'' +
                ", contractDemand=" + contractDemand +
                ", contractDemandUnit='" + contractDemandUnit + '\'' +
                ", isSeasonal=" + isSeasonal +
                ", purposeOfInstallationId=" + purposeOfInstallationId +
                ", purposeOfInstallation='" + purposeOfInstallation + '\'' +
                ", tariffCode='" + tariffCode + '\'' +
                ", subCategoryCode=" + subCategoryCode +
                ", phase='" + phase + '\'' +
                ", isGovernment=" + isGovernment +
                ", plotSize=" + plotSize +
                ", plotSizeUnit='" + plotSizeUnit + '\'' +
                ", locationCode='" + locationCode + '\'' +
                ", isXray=" + isXray +
                ", isWeldingTransformerSurcharge=" + isWeldingTransformerSurcharge +
                ", isCapacitorSurcharge=" + isCapacitorSurcharge +
                ", isDemandside=" + isDemandside +
                ", isBeneficiary=" + isBeneficiary +
                ", connectionDate=" + connectionDate +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedOn=" + updatedOn +
                '}';
    }
}