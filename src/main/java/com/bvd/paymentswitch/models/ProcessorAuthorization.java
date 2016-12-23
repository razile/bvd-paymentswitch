package com.bvd.paymentswitch.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="processor_authorization",
		indexes = {@Index(name="idx_comdata_query", columnList="invoice_number,card_number,unit_number,type,response_code", unique=true)})
public class ProcessorAuthorization {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	// required fields - header
	
	@Column(name="type", length=7)
	private String type;				// Character (2)	Request: IC (pre), AC (post)  VC (void), Response: PC (pre-approved), RC (post/void-approved), XC or EC (declined)
										// Comdata: SP00007, 00014, 00011
	@Column(length=16)
	private String location;
	
	@Column(length=5)
	private String versionNumber;	
	
	@Transient
	private int count;
	
	// sub-  header fields   - technically all optional according to PriorPost spec, but there may be vendor specific requirements
	
	@Column(length=8)
	private String softwareSystem;    	// Character (5)	SSRV:XXXXX		
	
	@Column(length=256)
	private String customerInformation;	// Character (1)	Request: CUST:I,  Response: tokenized fields	
	
	@Column(length=3)
	private String posCurrency;   		// Character (3)	CNC:CAD	
	
	@Column(length=3)
	private String language;			// Character (2)	LNG:1		
	
	@Column(length=8)
	private String unitofMeasure;		// Character (1) 	UOM:1 or 2		
	
	@Column(length=1)
	private String registerIndicator;	// Character (1)	FROM:C or R							
	
	// body fields 
	@Column(name="invoice_number",length=10)
	private String invoiceNumber;		// Character (10)	INVN:	
	
	@Column(length=64)
	private String cardToken;			// Character (20)	CDSW:	only send one of CARD/CDSW
	
	@Column(name="card_number",length=32)
	private String cardNumber;			// Character		CARD:   card number and how entered (S=swiped)  |CARD:1234567890,S|
	
	// Optional Extended Fields Prompts
	@Column(length=32)
	private String hubReading;			// Character (6)	HBRD:
	
	@Column(length=32)
	private String trailerNumber;		// Character (12)	TRLR:
	
	@Column(name="unit_number",length=32)
	private String unitNumber;			// Character (12)	UNIT:
	
	@Column(length=32)
	private String driverID;			// Character (12)	DRID:
	
	@Column(length=32)
	private String driversLicenseNumber;// Character (20)	DLIC:
	
	@Column(length=32)
	private String odometerReading;		// Character		ODRD:
	
	@Column(length=32)
	private String trip;				// Character		TRIP:	trip number
	
	@Column(length=32)
	private String vehiclePlateNumber;	//					LICN:  Vehicle license plate number
	
	@Column(length=32)
	private String poNumber;			//					PONB:	purchase order number
	
	
	// these are only in the response (and completion requests)
	@Column(length=16)
	private String authorizationCode;	// Character (10)	AUTH:
	
	@Column(precision=11,scale=2)
	private BigDecimal total;			// N/A	TOTL:  limit amount associated with total transaction
	
	@Column(length=256)
	private String fuel;					// N/A	FUEL:    total fuel limit by volume,currency 
	// fuelTypesAllowed	N/A (see bit mapping in type table)	FTYP:
	// fuelUseAllowed	N/A (see bit mapping in type table)	FUSE:
	// fuelService		N/A (see bit mapping in type table)	SERV:
	//String reefer; 			// or Non-Highway Fuel Limit	N/A	RFR:
	@Column(length=512)
	private String fuelLimit;			// N/A	FLMT:   limit by type,volume,currency
	
	@Column(length=1024)
	private String errorCode;			// ERCD: 
	
	@Column(length=256)
	private String message;				// MSG:
	
	@Column(length=256)
	private String print;				// PRNT:
	
	@Column(name="response_code",length=5) 
	private String responseCode;
	
	// the following are comdata specific fields
	
	@Column(length=16)    
	private String driversLicenseState;
	
	@Column(length=16)	 
	private  String trailerHubReading;
				
	@Column(length=16) 		
	private String trailerHours;
	
	// end of comdata fields
	
	private Timestamp createTimestamp;
	
	@Transient
	private FuelCode fuelCode;

	
	public ProcessorAuthorization() {
		super();
	}
	
    
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
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


	public String getPosCurrency() {
		return posCurrency;
	}


	public void setPosCurrency(String pOSCurrency) {
		posCurrency = pOSCurrency;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getUnitofMeasure() {
		return unitofMeasure;
	}


	public void setUnitofMeasure(String unitofMeasure) {
		this.unitofMeasure = unitofMeasure;
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


	public String getCardToken() {
		return cardToken;
	}


	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}


	public String getCardNumber() {
		return cardNumber;
	}


	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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


	public String getDriversLicenseNumber() {
		return driversLicenseNumber;
	}


	public void setDriversLicenseNumber(String driversLicenseNumber) {
		this.driversLicenseNumber = driversLicenseNumber;
	}


	public String getOdometerReading() {
		return odometerReading;
	}


	public void setOdometerReading(String odometerReading) {
		this.odometerReading = odometerReading;
	}


	public String getTrip() {
		return trip;
	}


	public void setTrip(String trip) {
		this.trip = trip;
	}


	public String getVehiclePlateNumber() {
		return vehiclePlateNumber;
	}


	public void setVehiclePlateNumber(String vehiclePlateNumber) {
		this.vehiclePlateNumber = vehiclePlateNumber;
	}


	public String getPoNumber() {
		return poNumber;
	}


	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}


	public String getAuthorizationCode() {
		return authorizationCode;
	}


	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}



	public BigDecimal getTotal() {
		return total;
	}


	public void setTotal(BigDecimal total) {
		this.total = total;
	}


	public String getFuel() {
		return fuel;
	}


	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getFuelLimit() {
		return fuelLimit;
	}


	public void setFuelLimit(String fuelLimit) {
		this.fuelLimit = fuelLimit;
	}


	public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getPrint() {
		return print;
	}


	public void setPrint(String print) {
		this.print = print;
	}


	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}


	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}



	public String getResponseCode() {
		return responseCode;
	}



	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}



	public FuelCode getFuelCode() {
		return fuelCode;
	}



	public void setFuelCode(FuelCode fuelCode) {
		this.fuelCode = fuelCode;
	}



	public String getDriversLicenseState() {
		return driversLicenseState;
	}



	public void setDriversLicenseState(String driversLicenseState) {
		this.driversLicenseState = driversLicenseState;
	}



	public String getTrailerHubReading() {
		return trailerHubReading;
	}



	public void setTrailerHubReading(String trailerHubReading) {
		this.trailerHubReading = trailerHubReading;
	}



	public String getTrailerHours() {
		return trailerHours;
	}



	public void setTrailerHours(String trailerHours) {
		this.trailerHours = trailerHours;
	}
	
	


}
