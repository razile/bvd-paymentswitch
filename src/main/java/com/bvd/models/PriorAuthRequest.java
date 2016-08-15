package com.bvd.models;

public class PriorAuthRequest {
	Integer length;
	String type;
	String location;
	String versionNumber;
	Integer count;
	String softwareSystem;    	// Character (5)	SSRV:XXXXX		
	String customerInformation;	// Character (1)	CUST:I			
	String POSCurrency;   		// Character (3)	CNC:USD or CAD	
	String Language;			// Character (2)	LNG:1			
	String unitofMeasure;		// Character (1) 	UOM:1 or 2		
	String cardToken;			// Character (20)	CDSW:			
	String registerIndicator;	// Character (1)	FROM:C or R		
	String invoiceNumber;		// Character (10)	INVN:			
	String unitPrompt;			// Character (12)	UNIT:			
	String fuelToken;			// unlimited		FUEL: 			
	
	String hubReading;			// Character (6)	HBRD:
	String trailerNumber;		// Character (12)	TRLR:
	String unitNumber;			// Character (12)	UNIT:
	String driverID;			// Character (12)	DRID:
	String CDLNumber;			// Character (20)	DLIC:

	
	public PriorAuthRequest() {
		super();
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getSoftwareSystem() {
		return softwareSystem;
	}

	public void setSoftwareSystem(String softwareSystem) {
		this.softwareSystem = softwareSystem;
	}

	public String getCustomerInformation() {
		return customerInformation;
	}

	public void setCustomerInformation(String customerInformation) {
		this.customerInformation = customerInformation;
	}

	public String getPOSCurrency() {
		return POSCurrency;
	}

	public void setPOSCurrency(String pOSCurrency) {
		POSCurrency = pOSCurrency;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public String getUnitofMeasure() {
		return unitofMeasure;
	}

	public void setUnitofMeasure(String unitofMeasure) {
		this.unitofMeasure = unitofMeasure;
	}

	public String getCardToken() {
		return cardToken;
	}

	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}

	public String getRegisterIndicator() {
		return registerIndicator;
	}

	public void setRegisterIndicator(String registerIndicator) {
		this.registerIndicator = registerIndicator;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getUnitPrompt() {
		return unitPrompt;
	}

	public void setUnitPrompt(String unitPrompt) {
		this.unitPrompt = unitPrompt;
	}

	public String getFuelToken() {
		return fuelToken;
	}

	public void setFuelToken(String fuelToken) {
		this.fuelToken = fuelToken;
	}

	public String getHubReading() {
		return hubReading;
	}

	public void setHubReading(String hubReading) {
		this.hubReading = hubReading;
	}

	public String getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getDriverID() {
		return driverID;
	}

	public void setDriverID(String driverID) {
		this.driverID = driverID;
	}

	public String getCDLNumber() {
		return CDLNumber;
	}

	public void setCDLNumber(String cDLNumber) {
		CDLNumber = cDLNumber;
	}

	

}
