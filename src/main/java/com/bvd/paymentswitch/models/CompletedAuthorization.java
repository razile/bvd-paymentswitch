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
@Table(name = "completed_authorizations")
@Immutable
@IdClass(AuthPK.class)
public class CompletedAuthorization {

	
	@Column(name="transaction_id")
	private String transactionID;
	
	@Column(name="outsource_id")
	private String outsourceID;
	
	private String companyNumber;
	private String accountNumber;

	private Timestamp processedDateTime;

	private String authorizationCode;
	
	@Id
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp transactionDateTime;
	private String payFlag;
	private String merchantNumber;
	private String cardNumber;
	private String cardType;
	private String recordType;
	private String transactionType;
	
	@Column(name="fuel_code1")
	private String fuelCode1;
	
	
	@Column(name="fuel_quantity1")
	private String fuelQuantity1;
	
	@Column(name="fuel_amount1")
	private String fuelAmount1;
	
	@Column(name="pos_vehicle_number")
	private String pOSVehicleNumber;
	
	@Column(name="pos_driver_id")
	private String pOSDriverID;
	
	@Column(name="pos_driver_cdl")
	private String pOSDriverCDL;
	
	@Column(name="cdl_state")
	private String cDLState;
	
	@Column(name="pos_trailer_number")
	private String pOSTrailerNumber;
	private String odometer;
	
	@Column(name="pos_trip_number")
	private String pOSTripNumber;
	
	@Column(name="po_number")
	private String poNumber;
	
	private String siteCurrency;
	private String billCurrency;
	
	@Id
	private String siteInvoiceNumber;
	private String cardSwiped;

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

	public Timestamp getProcessedDateTime() {
		return processedDateTime;
	}

	public void setProcessedDate(Timestamp processedDateTime) {
		this.processedDateTime = processedDateTime;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public Timestamp getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(Timestamp transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}


	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getMerchantNumber() {
		return merchantNumber;
	}

	public void setMerchantNumber(String merchantNumber) {
		this.merchantNumber = merchantNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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

	public String getpOSVehicleNumber() {
		return pOSVehicleNumber;
	}

	public void setpOSVehicleNumber(String pOSVehicleNumber) {
		this.pOSVehicleNumber = pOSVehicleNumber;
	}

	public String getpOSDriverID() {
		return pOSDriverID;
	}

	public void setpOSDriverID(String pOSDriverID) {
		this.pOSDriverID = pOSDriverID;
	}

	public String getpOSDriverCDL() {
		return pOSDriverCDL;
	}

	public void setpOSDriverCDL(String pOSDriverCDL) {
		this.pOSDriverCDL = pOSDriverCDL;
	}

	public String getcDLState() {
		return cDLState;
	}

	public void setcDLState(String cDLState) {
		this.cDLState = cDLState;
	}

	public String getpOSTrailerNumber() {
		return pOSTrailerNumber;
	}

	public void setpOSTrailerNumber(String pOSTrailerNumber) {
		this.pOSTrailerNumber = pOSTrailerNumber;
	}

	public String getOdometer() {
		return odometer;
	}

	public void setOdometer(String odometer) {
		this.odometer = odometer;
	}

	public String getpOSTripNumber() {
		return pOSTripNumber;
	}

	public void setpOSTripNumber(String pOSTripNumber) {
		this.pOSTripNumber = pOSTripNumber;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getSiteCurrency() {
		return siteCurrency;
	}

	public void setSiteCurrency(String siteCurrency) {
		this.siteCurrency = siteCurrency;
	}

	public String getBillCurrency() {
		return billCurrency;
	}

	public void setBillCurrency(String billCurrency) {
		this.billCurrency = billCurrency;
	}

	public String getSiteInvoiceNumber() {
		return siteInvoiceNumber;
	}

	public void setSiteInvoiceNumber(String siteInvoiceNumber) {
		this.siteInvoiceNumber = siteInvoiceNumber;
	}

	public String getCardSwiped() {
		return cardSwiped;
	}

	public void setCardSwiped(String cardSwiped) {
		this.cardSwiped = cardSwiped;
	}

}