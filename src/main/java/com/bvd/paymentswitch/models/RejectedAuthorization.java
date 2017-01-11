package com.bvd.paymentswitch.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonRootName(value = "Transaction")
@Entity
@Table(name = "rejected_authorizations")
@Immutable
@IdClass(AuthPK.class)
public class RejectedAuthorization {

	@Column(name = "transaction_id")
	private String transactionID;

	@Column(name = "outsource_id")
	private String outsourceID;

	private String companyNumber;
	private String accountNumber;

	@Id
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp transactionDateTime;
	
	private String recordType;
	private String transactionType;
	private String errorNumber;
	private String errorMessage;
	private String cardNumber;
	private String siteNumber;
	private String cardType;

	@Column(name = "pos_vehicle_number")
	private String pOSVehicleNumber;

	@Column(name = "pos_trailer_number")
	private String pOSTrailerNumber;

	@Column(name = "pos_driver_cdl")
	private String pOSDriverCDL;

	@Column(name = "cdl_state")
	private String driverCDLSt;

	@Column(name = "pos_driver_id")
	private String pOSDriverID;

	private String odometer;

	@Column(name = "pos_trip_number")
	private String pOSTrip;

	@Column(name = "po_number")
	private String pONumber;

	@Column(name = "unit_of_measure")
	private String unitofMeasure;

	@Column(name = "fuel_code1")
	private String fuelCode1;

	@Column(name = "fuel_quantity1")
	private String fuelQuantity1;

	@Column(name = "fuel_amount1")
	private String fuelAmount1;

	private String siteCurrency;
	
	@Id
	private String siteInvoiceNumber;

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getOutsourceID() {
		return outsourceID;
	}

	public void setOutsourceID(String outsourceID) {
		this.outsourceID = outsourceID;
	}

	public String getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Timestamp getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(Timestamp transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(String errorNumber) {
		this.errorNumber = errorNumber;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getpOSVehicleNumber() {
		return pOSVehicleNumber;
	}

	public void setpOSVehicleNumber(String pOSVehicleNumber) {
		this.pOSVehicleNumber = pOSVehicleNumber;
	}

	public String getpOSTrailerNumber() {
		return pOSTrailerNumber;
	}

	public void setpOSTrailerNumber(String pOSTrailerNumber) {
		this.pOSTrailerNumber = pOSTrailerNumber;
	}

	public String getpOSDriverCDL() {
		return pOSDriverCDL;
	}

	public void setpOSDriverCDL(String pOSDriverCDL) {
		this.pOSDriverCDL = pOSDriverCDL;
	}

	public String getDriverCDLSt() {
		return driverCDLSt;
	}

	public void setDriverCDLSt(String driverCDLSt) {
		this.driverCDLSt = driverCDLSt;
	}

	public String getpOSDriverID() {
		return pOSDriverID;
	}

	public void setpOSDriverID(String pOSDriverID) {
		this.pOSDriverID = pOSDriverID;
	}

	public String getOdometer() {
		return odometer;
	}

	public void setOdometer(String odometer) {
		this.odometer = odometer;
	}

	public String getpOSTrip() {
		return pOSTrip;
	}

	public void setpOSTrip(String pOSTrip) {
		this.pOSTrip = pOSTrip;
	}

	public String getpONumber() {
		return pONumber;
	}

	public void setpONumber(String pONumber) {
		this.pONumber = pONumber;
	}

	public String getUnitofMeasure() {
		return unitofMeasure;
	}

	public void setUnitofMeasure(String unitofMeasure) {
		this.unitofMeasure = unitofMeasure;
	}

	public String getFuelCode1() {
		return fuelCode1;
	}

	public void setFuelCode1(String fuelCode1) {
		this.fuelCode1 = fuelCode1;
	}

	public String getFuelQuantity1() {
		return fuelQuantity1;
	}

	public void setFuelQuantity1(String fuelQuantity1) {
		this.fuelQuantity1 = fuelQuantity1;
	}

	public String getFuelAmount1() {
		return fuelAmount1;
	}

	public void setFuelAmount1(String fuelAmount1) {
		this.fuelAmount1 = fuelAmount1;
	}

	public String getSiteCurrency() {
		return siteCurrency;
	}

	public void setSiteCurrency(String siteCurrency) {
		this.siteCurrency = siteCurrency;
	}

	public String getSiteInvoiceNumber() {
		return siteInvoiceNumber;
	}

	public void setSiteInvoiceNumber(String siteInvoiceNumber) {
		this.siteInvoiceNumber = siteInvoiceNumber;
	}

}
