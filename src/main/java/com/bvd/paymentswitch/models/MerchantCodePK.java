package com.bvd.paymentswitch.models;

import java.io.Serializable;

public class MerchantCodePK implements Serializable {

	private static final long serialVersionUID = 7211553413631799715L;

	private String siteId;
	
	private Short paymentProcessorId;

	
	
	public MerchantCodePK() {
		super();
	}


	public MerchantCodePK(String siteId, Short paymentProcessorId) {
		super();
		this.siteId = siteId;
		this.paymentProcessorId = paymentProcessorId;
	}
	
	
	@Override
	public boolean equals(Object object) {
		   if (object instanceof MerchantCodePK) {
			   MerchantCodePK pk = (MerchantCodePK)object;
	            return paymentProcessorId.shortValue() == pk.paymentProcessorId.shortValue() && siteId.equals(pk.siteId);
	        } else {
	            return false;
	        }
	}
	
	@Override
	public int hashCode() {
		return siteId.hashCode() + paymentProcessorId.shortValue();
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public Short getPaymentProcessorId() {
		return paymentProcessorId;
	}

	public void setPaymentProcessorId(Short paymentProcessorId) {
		this.paymentProcessorId = paymentProcessorId;
	}
	
	
}
