package com.bvd.paymentswitch.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(MerchantCodePK.class)
public class MerchantCode {
	
	@Id
	@Column(length=16)
	private String siteId;
	
	@Id
	private Short paymentProcessorId;
	
	
	@Column(length=16, nullable=false)
	private String merchantId;


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


	public String getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	
	
}
