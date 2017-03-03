package com.bvd.paymentswitch.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class AuthPK implements Serializable {

	private static final long serialVersionUID = 7211553413631799717L;

	private String siteInvoiceNumber;
	
	private Timestamp transactionDateTime;

	
	
	public AuthPK() {
		super();
	}


	public AuthPK(String siteInvoiceNumber, Timestamp transactionDateTime) {
		super();
		this.siteInvoiceNumber = siteInvoiceNumber;
		this.transactionDateTime = transactionDateTime;
	}
	
	
	@Override
	public boolean equals(Object object) {
		   if (object instanceof AuthPK) {
			   AuthPK pk = (AuthPK)object;
	            return siteInvoiceNumber.equals(pk.siteInvoiceNumber)  && (transactionDateTime.compareTo(pk.transactionDateTime)==0) ;
	        } else {
	            return false;
	        }
	}
	
	@Override
	public int hashCode() {
		return siteInvoiceNumber.hashCode() + transactionDateTime.hashCode();
	}


	public String getSiteInvoiceNumber() {
		return siteInvoiceNumber;
	}


	public void setSiteInvoiceNumber(String siteInvoiceNumber) {
		this.siteInvoiceNumber = siteInvoiceNumber;
	}


	public Timestamp getTransactionDateTime() {
		return transactionDateTime;
	}


	public void setTransactionDateTime(Timestamp transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}


	
}
