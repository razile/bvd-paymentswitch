package com.bvd.paymentswitch.web.service.models.reject;

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
	private KeyFieldsSet keyFieldsSet;
	
	
	
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

	public KeyFieldsSet getKeyFieldsSet() {
		return keyFieldsSet;
	}

	public void setKeyFieldsSet(KeyFieldsSet keyFieldsSet) {
		this.keyFieldsSet = keyFieldsSet;
	}
	
	
	
	
}
