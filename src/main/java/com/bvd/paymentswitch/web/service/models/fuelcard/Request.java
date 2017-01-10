package com.bvd.paymentswitch.web.service.models.fuelcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonRootName(value="Transaction")
public class Request {
	
	private String transactionID;
	private String outsourceID;
	private String companyNumber;
	private String accountNumber;
	
	private String cardGroup;
	private String processedDate;
	private String processedTime;
	private String referenceAuthNumber;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	
	
	
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

	public String getCardGroup() {
		return cardGroup;
	}
	
	public void setCardGroup(String cardGroup) {
		this.cardGroup = cardGroup;
	}
	
	public String getProcessedDate() {
		return processedDate;
	}
	
	public void setProcessedDate(String processedDate) {
		this.processedDate = processedDate;
	}
	
	public String getProcessedTime() {
		return processedTime;
	}
	
	public void setProcessedTime(String processedTime) {
		this.processedTime = processedTime;
	}
	
	public String getReferenceAuthNumber() {
		return referenceAuthNumber;
	}
	
	public void setReferenceAuthNumber(String referenceAuthNumber) {
		this.referenceAuthNumber = referenceAuthNumber;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
