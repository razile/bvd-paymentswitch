package com.bvd.models;

import java.math.BigDecimal;

public class PriorAuthResponse {

	Integer length;	 					// Character (4) ASCII string, padded with leading zeroes.	
	String type;						// Character (2)	PC for approved, XC or EC for declined
	String location;					// Character (6)	99999
	String versionNumber;				// Character (5)	
	Integer count;						// Character (1) ASCII string	
	String invoiceNumber;				// Character (10)	INVN:
	String authorizationCode;			// Character (10)	AUTH:
	String customerInformation;			// Token	Multiple data elements
	String customerName;				// = Character (25)
	String city;						// = Character (15)
	String state;						// = Character (2)
	String accountCode;					// = Numeric (10) max, usually Numeric (6) is used.	
	BigDecimal cashAdvance;				// Numeric (7)	Max 99999.99	CADV:
	BigDecimal total;					// N/A	TOTL:
	BigDecimal fuel;					// N/A	FUEL:
	// fuelTypesAllowed	N/A (see bit mapping in type table)	FTYP:
	// fuelUseAllowed	N/A (see bit mapping in type table)	FUSE:
	// fuelService		N/A (see bit mapping in type table)	SERV:
	BigDecimal reefer; 					// or Non-Highway Fuel Limit	N/A	RFR:
	String driverID;					// Character (25)	DRID:
	String hubReading;					// N/A	HBRD:
	String fuelLimit;					// N/A	FLMT:
	String errorCode;					// ERCD: 

	
	public PriorAuthResponse() {
		super();
	}

}
