package com.bvd.paymentswitch.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.bvd.paymentswitch.utils.ProtocolUtils;

@Entity
public class PriorPostAuthorization {
	
	@Id
	private Long id;
	// required fields - header
	
	@Column(length=2)
	private String type;				// Character (2)	Request: IC (pre), AC (post)  VC (void), Response: PC (pre-approved), RC (post/void-approved), XC or EC (declined)
	
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
	@Column(length=10)
	private String invoiceNumber;		// Character (10)	INVN:	
	
	@Column(length=32)
	private String cardToken;			// Character (20)	CDSW:	only send one of CARD/CDSW
	
	@Column(length=32)
	private String cardNumber;			// Character		CARD:   card number and how entered (S=swiped)  |CARD:1234567890,S|
	
	// Optional Extended Fields Prompts
	@Column(length=16)
	private String hubReading;			// Character (6)	HBRD:
	
	@Column(length=16)
	private String trailerNumber;		// Character (12)	TRLR:
	
	@Column(length=16)
	private String unitNumber;			// Character (12)	UNIT:
	
	@Column(length=32)
	private String driverID;			// Character (12)	DRID:
	
	@Column(length=32)
	private String driversLicenseNumber;// Character (20)	DLIC:
	
	@Column(length=16)
	private String odometerReading;		// Character		ODRD:
	
	@Column(length=8)
	private String trip;				// Character		TRIP:	trip number
	
	@Column(length=16)
	private String vehiclePlateNumber;	//					LICN:  Vehicle license plate number
	
	@Column(length=16)
	private String poNumber;			//					PONB:	purchase order number
	
	
	// these are only in the response (and completion requests)
	@Column(length=16)
	private String authorizationCode;	// Character (10)	AUTH:
	
	@Column(precision=11,scale=2)
	private BigDecimal total;			// N/A	TOTL:  limit amount associated with total transaction
	
	@Column(length=32)
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
	
	private Timestamp createTimestamp;

	
	public PriorPostAuthorization() {
		super();
	}
	
	
	public PriorPostAuthorization(KardallHostAuthorization request) {
		
		// map common fields
		this.id = request.getId();
		int transType = request.getTransactionType();
		this.invoiceNumber = request.getTrnNo();
		this.cardNumber = request.getCard1() + ",S";  
		this.cardToken = request.getCard2();
		this.posCurrency = "CAD";		// canadian funds
		this.registerIndicator = "C"; 	// transaction originated from card reader
		this.location = "T2222A";
		this.versionNumber = "01.10";
		this.count = 1;
		
		
		// prompts
		this.driverID = request.getDocument();
		this.driversLicenseNumber = request.getDriversLicense();
		this.unitNumber = request.getUnit();
		this.poNumber = request.getPo();
		this.odometerReading = request.getOdometer();
		this.trailerNumber = request.getTrailerNumber();
		this.hubReading = request.getHubometer();
		this.trip = request.getTripNumber();
		this.vehiclePlateNumber = request.getTruck();
		
		
		BigDecimal sellingPrice = request.getSellingPrice();
		String fuelType = "2"; // request.getFuelType();
		// specific tokens by lifecycle
		if (transType == 0) {  
			// this is a prior
			this.type = "IC";
			this.customerInformation = "I";
			//this.fuel = "1.000," + request.getSellingPrice() + ",0.00," + fuelType + ",1,1";
			
		} else {
			// this is a post (completed transaction)
			this.type = "AC";
			this.authorizationCode = request.getAuthId().trim();
			this.customerInformation = "I";
			
			BigDecimal quantity = request.getQuantityNet();
			BigDecimal amount = request.getAmount();
			
			String fuelToken = quantity + "," + sellingPrice + "," + amount + "," + fuelType + ",1,1";
			this.total = amount;
			this.fuel = fuelToken;  
		}
	}
	
	public PriorPostAuthorization(Long id, String response) {
		
		this.id = id;
		String[] fields = response.split("\\|");
		
		this.type = fields[1];
		this.location = fields[2];
		this.versionNumber = fields[3];
		this.count = Integer.parseInt(fields[4]);
		
		Map<String, String> tokens = new HashMap<String, String>();
		for (int i = 5; i<fields.length; i++) {
			AbstractMap.SimpleEntry<String, String> entry = ProtocolUtils.parseToken(fields[i]);
			if (entry != null)
				tokens.put(entry.getKey(), entry.getValue());
		}

		this.softwareSystem = tokens.get("SSVR");    	// Character (5)	SSRV:XXXXX		
		this.posCurrency = tokens.get("CNC");
		this.language = tokens.get("LNG");
		this.unitofMeasure = tokens.get("UOM");
		this.registerIndicator = tokens.get("FROM");
		this.customerInformation = tokens.get("CUST");
		this.invoiceNumber = tokens.get("INVN");
		this.cardToken = tokens.get("CDSW");
		
		String cardNumber = tokens.get("CARD");
		if (cardNumber != null) {
			this.cardNumber = cardNumber.split(",")[0];
		}
		
		this.hubReading = tokens.get("HBRD");
		this.trailerNumber = tokens.get("TRLR");
		this.unitNumber = tokens.get("UNIT");
		this.driverID = tokens.get("DRID");
		this.driversLicenseNumber = tokens.get("DLIC");
		this.odometerReading = tokens.get("ODRD");
		this.trip = tokens.get("TRIP");
		this.vehiclePlateNumber = tokens.get("LICN");
		this.poNumber = tokens.get("PONB");
		
		this.authorizationCode = tokens.get("AUTH");
		this.fuel = tokens.get("FUEL");
		this.fuelLimit = tokens.get("FLMT");
		if (tokens.get("TOTL") != null) this.total = ProtocolUtils.getBigDecimal(tokens.get("TOTL"),2);

		this.errorCode = tokens.get("ERCD");
		this.print = tokens.get("PRNT");
		this.message = tokens.get("MSG");

	}
	
	@Override
	public String toString() {
		String msg = "|" + this.type + "|" + this.location + "|" + this.versionNumber + "|" +  this.count + "|";
		
		List<String> tokens = new ArrayList<String>();
		
		ProtocolUtils.createToken("CUST", this.customerInformation, tokens);
		ProtocolUtils.createToken("SSVR", this.softwareSystem, tokens);   	
		ProtocolUtils.createToken("CNC", this.posCurrency, tokens);
		ProtocolUtils.createToken("LNG", this.language, tokens);
		ProtocolUtils.createToken("UOM", this.unitofMeasure, tokens);
		ProtocolUtils.createToken("FROM", this.registerIndicator, tokens);
	
		ProtocolUtils.createToken("INVN", this.invoiceNumber, tokens);
		ProtocolUtils.createToken("CDSW", this.cardToken, tokens);
		ProtocolUtils.createToken("CARD", this.cardNumber, tokens);
		ProtocolUtils.createToken("AUTH", this.authorizationCode, tokens);
		
	
		ProtocolUtils.createToken("FUEL", this.fuel, tokens);
		ProtocolUtils.createToken("FLMT", this.fuelLimit, tokens);
		if (this.total != null) ProtocolUtils.createToken("TOTL", total.toPlainString(), tokens);
		
		ProtocolUtils.createToken("HBRD", this.hubReading, tokens);
		ProtocolUtils.createToken("TRLR", this.trailerNumber, tokens);
		ProtocolUtils.createToken("UNIT", this.unitNumber, tokens);
		ProtocolUtils.createToken("DRID", this.driverID, tokens);
		ProtocolUtils.createToken("DLIC", this.driversLicenseNumber, tokens);
		ProtocolUtils.createToken("ODRD", this.odometerReading, tokens);
		ProtocolUtils.createToken("TRIP", this.trip, tokens);
		ProtocolUtils.createToken("LICN", this.vehiclePlateNumber, tokens);
		ProtocolUtils.createToken("PONB", this.poNumber, tokens);

		ProtocolUtils.createToken("ERCD", this.errorCode, tokens);
		ProtocolUtils.createToken("PRNT", this.print, tokens);
		ProtocolUtils.createToken("MSG", this.message, tokens);
		
		for (String s : tokens) {
			msg += s + "|";
		}
		msg = msg.substring(0, msg.length()-1);  // strip off the last | 
		
		// determine length of message
		int length = msg.length() + 9;   // the 9 extra are for the 4 bytes of length, PV|, and the STX and ETX
		String lstr = String.valueOf(length);
		while (lstr.length() < 4) {
			lstr = "0" + lstr;
		}
		
		return (lstr + msg);
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
	
	

}
