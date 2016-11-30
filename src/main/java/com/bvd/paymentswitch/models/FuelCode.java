package com.bvd.paymentswitch.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.persistence.UniqueConstraint;

@Entity
//@Table(
//		name="fuel_code",
//		uniqueConstraints = @UniqueConstraint(columnNames={"efs_code", "comdata_code"})
//	  )
public class FuelCode {

	@Id
	@Column(length=3)
	private String kardallCode;
	
	
	private int efsCode;
	
	@Column(length=3)
	private String comdataCode;
	
	@Column(length=32)
	private String description;
	

	public FuelCode() {
		super();
	}

	public FuelCode(String kardallCode, int efsCode, String comdataCode, String description) {
		super();
		this.kardallCode = kardallCode;
		this.efsCode = efsCode;
		this.comdataCode = comdataCode;
		this.description = description;
	}

	public String getKardallCode() {
		return kardallCode;
	}

	public void setKardallCode(String kardallCode) {
		this.kardallCode = kardallCode;
	}

	public int getEfsCode() {
		return efsCode;
	}

	public void setEfsCode(int efsCode) {
		this.efsCode = efsCode;
	}

	public String getComdataCode() {
		return comdataCode;
	}

	public void setComdataCode(String comdataCode) {
		this.comdataCode = comdataCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
