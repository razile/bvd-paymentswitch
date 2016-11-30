package com.bvd.paymentswitch.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class PaymentProcessor {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Short id;
	
	@Column(length=64,nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String providerClass;
	
	@Column(nullable=false)
	private String host;
	
	@Column(nullable=false)
	private int port;
	
	@Column(nullable=false)
	private boolean clientDisconnect = false;
	
	@Column(nullable=false)
	private boolean sslRequired = false;
	
	@Column(length=16,nullable=false)
	private String softwareSystem;

	@Column(length=16)
	private String language;
	@Column(length=16)
	private String unitOfMeasure;
	
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="PAYMENTPROCESSOR_ID")
	private List<BinPaymentProcessor> bins;
	

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getProviderClass() {
		return providerClass;
	}

	public void setProviderClass(String providerClass) {
		this.providerClass = providerClass;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	

	public boolean isSslRequired() {
		return sslRequired;
	}

	public void setSslRequired(boolean sslRequired) {
		this.sslRequired = sslRequired;
	}

	public boolean isClientDisconnect() {
		return clientDisconnect;
	}

	public void setClientDisconnect(boolean clientDisconnect) {
		this.clientDisconnect = clientDisconnect;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	
	public String getSoftwareSystem() {
		return softwareSystem;
	}

	public void setSoftwareSystem(String softwareSystem) {
		this.softwareSystem = softwareSystem;
	}

	public List<BinPaymentProcessor> getBins() {
		return bins;
	}

	public void setBins(List<BinPaymentProcessor> bins) {
		this.bins = bins;
	}
	
	

	

}
