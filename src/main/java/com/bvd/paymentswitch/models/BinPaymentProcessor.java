package com.bvd.paymentswitch.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BinPaymentProcessor {

	@Id
	private String bin;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PAYMENTPROCESSOR_ID")
	private PaymentProcessor paymentProcessor;


	public PaymentProcessor getPaymentProcessor() {
		return paymentProcessor;
	}
	

	public String getBin() {
		return bin;
	}


	public void setBin(String bin) {
		this.bin = bin;
	}


	public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
		this.paymentProcessor = paymentProcessor;
	}
	
	
}
