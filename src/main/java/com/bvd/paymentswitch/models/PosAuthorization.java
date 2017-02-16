package com.bvd.paymentswitch.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="pos_authorization",
indexes = {@Index(name="idx_pos_invoice", columnList="trn_no", unique=false),
			@Index(name="idx_pos_create_ts", columnList="create_timestamp",unique=false) 
			})
public class PosAuthorization {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(length=16,nullable=false)
	private String procotolId;
	private int source;
	private int authorization;
	private int pinFlag;
	private int hasStandardPrompts;
	private int hasCustomPrompts;
	private int reAuthorizationFlag;
	private int receiptTrailerFlag;
	private int trainingExclusionTimes;
	private int transactionType;
	
	@Column(length=16,nullable=false)
	private String siteId;
	
	@Column(nullable=false)
	private Timestamp dateTime;
	
	@Column(length=16,nullable=false)
	private String authId;
	
	@Column(name="trn_no",length=16,nullable=false)
	private String trnNo;
	
	@Column(length=16)
	private String reqId;

	@Column(precision=11,scale=2)
	private BigDecimal amount;
	
	@Column(length=32)
	private String card1;
	@Column(length=32)
	private String card2;
	
	@Column(length=16)
	private String cdlNumber;
	
	@Column(length=8)
	private String cardType;
	
	@Column(length=16)
	private String documentNumber;
	
	@Column(precision=11,scale=3)
	private BigDecimal priceDiscount;
	
	@Column(length=16)
	private String driverId;
	
	@Column(length=32)
	private String emailTrigger;
	
	@Column(length=4)
	private String fuelType;
	
	@Column(length=4)
	private String hoseNumber;
	
	@Column(length=32)
	private String driversLicense;
	
	@Column(length=32)
	private String propaneLicense;
	
	@Column(length=16)
	private String misc;
	
	@Column(length=16)
	private String unitNumber;
	
	@Column(length=16)
	private String truckNumber;
	
	@Column(length=16)
	private String dipReading;
	
	@Column(length=8)
	private String temperature;
	
	@Column(length=16)
	private String poNumber;
	
	@Column(length=4)
	private String compartmentNumber;
	
	@Column(length=16)
	private String flightNumber;
	
	@Column(length=16)
	private String registrationNumber;
	
	@Column(length=16)
	private String tailNumber;
	
	@Column(length=256)
	private String message;
	
	@Column(precision=11,scale=2)
	private BigDecimal dollarLimit;
	
	@Column(precision=11,scale=3)
	private BigDecimal volumeLimit;
	
	@Column(length=8)
	private String pin;
	
	@Column(length=16)
	private String odometer;

	@Column(precision=11,scale=3)
	private BigDecimal quantityNet;
	
	@Column(length=16)
	private String quantityGross;
	
	@Column(length=4)
	private String pumpNumber;
	
	@Column(precision=11,scale=3)
	private BigDecimal sellingPrice;
	
	@Column(length=1024)
	private String denialReason;
	
	@Column(length=256)
	private String receiptTrailer;
	
	@Column(length=1)
	private String trainingPrompt;
	
	@Column(length=16)
	private String totalizingDollars;
	
	@Column(length=256)
	private String trainingExclusion;
	
	@Column(length=32)
	private String totalizerVolume;
	
	@Column(length=32)
	private String trailerNumber;
	
	@Column(length=16)
	private String hubometer;
	
	@Column(length=16)
	private String tripNumber;
	
	@Column(length=64)
	private String track2Data;
	
	@Column(name="create_timestamp")
	private Timestamp createTimestamp;
	
	// the following are comdata specific fields - they need to get added to the Kardall request
	
	@Column(length=16)    // will use P3 for this element
	private String driversLicenseState;
	
	@Column(length=16)	 // will user P4 for this element
	private  String trailerHubReading;
				
	@Column(length=16) 		// will use P5 for this element
	private String trailerHours;
	
	// end of comdata fields
	
	@Column(length=16)
	private String fuelTarget;
	
	
	@Column(length=16)
	private String vehiclePlateNumber;
	
	
	@Transient
	private Map<String,String> prompts;
	
	@Transient
	private FuelCode fuelCode;
	
	@Transient
	private String comdataCTL;

	public PosAuthorization() {
		super();
	}
	
	public PosAuthorization(PosAuthorization request) {
		this.id = request.id;
		this.procotolId = request.getProcotolId();
		this.siteId = request.siteId;
		this.dateTime = request.dateTime;
		this.authId = request.authId;
		this.trnNo = request.trnNo;
		this.reqId = request.reqId;
		this.fuelCode = request.getFuelCode();
		this.fuelTarget = request.fuelTarget;
		
		//this.transactionType = request.getTransactionType();
		//this.source = source;
		//this.hasStandardPrompts = 0;
		//this.hasCustomPrompts = 0;
		//this.pinFlag = request.pinFlag;
		//this.trainingExclusionTimes = request.trainingExclusionTimes;
	}

	public PosAuthorization(String request) {
		// message starts with a 68 byte header
		String header = request.substring(0, 72);

		/*
		 * Parse fields from header as per spec Field Length ProtocolID 11 an
		 * ReqBitMap1 1 an ReqBitMap2 1 an SiteID 12 AN DateTime 14 n AuthID 12
		 * an TrnNo 5 n ReqID 12 an
		 */

		this.procotolId = header.substring(0, 11);

		// String reqBitMap1 =
		// Integer.toBinaryString(Character.valueOf(header.charAt(11)));
		String reqBitMap1 = ProtocolUtils.getBinary(header.substring(11,14));  // Integer.toBinaryString(ProtocolUtils.getCodePoint(header.charAt(11)));

		String reqBitMap2 = ProtocolUtils.getBinary(header.substring(14,17)); //Integer.toBinaryString(ProtocolUtils.getCodePoint(header.charAt(12)));
	
		this.source = Integer.valueOf(String.valueOf(reqBitMap1.charAt(7)));
		this.authorization = Integer.valueOf(String.valueOf(reqBitMap1.charAt(6)));
		this.pinFlag = Integer.valueOf(String.valueOf(reqBitMap1.charAt(5)));
		this.hasStandardPrompts = Integer.valueOf(String.valueOf(reqBitMap1.charAt(4)));
		this.hasCustomPrompts = Integer.valueOf(String.valueOf(reqBitMap1.charAt(3)));
		this.reAuthorizationFlag = Integer.valueOf(String.valueOf(reqBitMap1.charAt(2)));
		this.receiptTrailerFlag = Integer.valueOf(String.valueOf(reqBitMap1.charAt(1)));
		this.transactionType = Integer.valueOf(String.valueOf(reqBitMap2.charAt(7)));
		this.trainingExclusionTimes = Integer.valueOf(String.valueOf(reqBitMap2.charAt(6)));

		this.siteId = header.substring(17, 29);
		this.dateTime = ProtocolUtils.stringToTimestamp(header.substring(29, 43));
		this.authId = header.substring(43, 55);
		this.trnNo = header.substring(55, 60);
		this.reqId = header.substring(60, 72);

		// now get the xml-ish fields, which start at position 68 and end before
		// the check digit
		String xml = request.substring(72, request.length() - 1);
		String[] elements = xml.split("<|/>");

		// build a map of element names (keys) and values... we'll use this to
		// initilialize the fields
		Map<String, String> xmlFields = new HashMap<String, String>();
		for (String e : elements) {
			AbstractMap.SimpleEntry<String, String> entry = ProtocolUtils.parseXmlField(e);
			if (entry != null)
				xmlFields.put(entry.getKey(), entry.getValue());
		}


		this.amount = ProtocolUtils.getBigDecimal(xmlFields.get("A1"),2);
		this.card1 = xmlFields.get("C1");
		this.card2 = xmlFields.get("C2");
		this.cardType = xmlFields.get("CT");
		this.pin = xmlFields.get("N1");
		this.documentNumber = xmlFields.get("D1");
		this.emailTrigger = xmlFields.get("EM");
		this.fuelType = xmlFields.get("F1");
		this.hoseNumber = xmlFields.get("H1");
		this.driversLicense = xmlFields.get("L1");
		this.propaneLicense = xmlFields.get("L2");
		this.misc = xmlFields.get("M1");
		this.unitNumber = xmlFields.get("M2");
		this.truckNumber = xmlFields.get("M3");
		this.vehiclePlateNumber = xmlFields.get("L3");
		this.dipReading = xmlFields.get("M4");
		this.temperature = xmlFields.get("M5");
		this.poNumber = xmlFields.get("M6");
		this.compartmentNumber = xmlFields.get("M7");
		this.flightNumber = xmlFields.get("M8");
		this.registrationNumber = xmlFields.get("M9");
		this.tailNumber = xmlFields.get("M0");
		this.message = xmlFields.get("MG");
		this.dollarLimit = ProtocolUtils.getBigDecimal(xmlFields.get("LD"),2);
		this.volumeLimit = ProtocolUtils.getBigDecimal(xmlFields.get("LV"),3);
		this.odometer = xmlFields.get("O1");
		this.quantityNet = ProtocolUtils.getBigDecimal(xmlFields.get("Q1"),3);
		this.quantityGross = xmlFields.get("Q2");
		this.pumpNumber = xmlFields.get("PN");
		this.sellingPrice =  ProtocolUtils.getBigDecimal(xmlFields.get("PR"),3);
		this.denialReason = xmlFields.get("R1");
		this.receiptTrailer = xmlFields.get("RT");
		this.trainingPrompt = xmlFields.get("T1");
		this.totalizingDollars = xmlFields.get("TD");
		this.trainingExclusion = xmlFields.get("TE");
		this.totalizerVolume =  xmlFields.get("TV");
		this.trailerNumber = xmlFields.get("TN");
		this.hubometer = xmlFields.get("P1");
		this.tripNumber = xmlFields.get("P2");
		this.cdlNumber = xmlFields.get("CD");
		this.driverId = xmlFields.get("DI");
		this.priceDiscount = ProtocolUtils.getBigDecimal(xmlFields.get("DC"),3);
		this.track2Data = xmlFields.get("TR");
		this.driversLicenseState = xmlFields.get("P3");
		this.trailerHubReading = xmlFields.get("P4");
		this.trailerHours = xmlFields.get("P5");
		this.fuelTarget = xmlFields.get("FT");
	}

	@Override
	public String toString() {
		
		String reqbmp1 = "1" + this.receiptTrailerFlag + this.reAuthorizationFlag + this.hasCustomPrompts + this.hasStandardPrompts + this.pinFlag + this.authorization + this.source;
		String reqbmp2 = "111111" + this.trainingExclusionTimes + this.transactionType;
		
		String auth = this.authId;
		while (auth.length() < 12) {
			auth += " ";
		}
		String msg = this.procotolId + ProtocolUtils.encodeInteger(ProtocolUtils.getInteger(reqbmp1)) + ProtocolUtils.encodeInteger(ProtocolUtils.getInteger(reqbmp2)) + this.siteId + ProtocolUtils.timestampToString(dateTime) + auth + this.trnNo + this.reqId;
		
		List<String> fields = new ArrayList<String>();
		
		if (amount != null) ProtocolUtils.createXmlField("A1", amount.toPlainString(), fields);
		ProtocolUtils.createXmlField("C1", this.card1, fields);
		ProtocolUtils.createXmlField("C2", this.card2, fields);
		ProtocolUtils.createXmlField("CT", this.cardType, fields);
		ProtocolUtils.createXmlField("TR", this.track2Data, fields);
		ProtocolUtils.createXmlField("N1", this.pin, fields);
		ProtocolUtils.createXmlField("EM", this.emailTrigger, fields);
		ProtocolUtils.createXmlField("F1", this.fuelType, fields);
		ProtocolUtils.createXmlField("FT", this.fuelTarget, fields);
		ProtocolUtils.createXmlField("H1", this.hoseNumber, fields);
		ProtocolUtils.createXmlField("L2", this.propaneLicense, fields);
		ProtocolUtils.createXmlField("MG", this.message, fields);
		ProtocolUtils.createXmlField("M1", this.misc , fields);
		ProtocolUtils.createXmlField("M4", this.dipReading, fields);
		ProtocolUtils.createXmlField("M5", this.temperature, fields);
		ProtocolUtils.createXmlField("M7", this.compartmentNumber, fields);
		ProtocolUtils.createXmlField("M8", this.flightNumber, fields);
		ProtocolUtils.createXmlField("M9", this.registrationNumber, fields);
		ProtocolUtils.createXmlField("M0", this.tailNumber, fields);
		ProtocolUtils.createXmlField("D1", this.documentNumber, fields);
		if (priceDiscount != null) ProtocolUtils.createXmlField("DC", priceDiscount.toPlainString(), fields);
		 
		if (prompts == null) {
			ProtocolUtils.createXmlField("L1", this.driversLicense, fields);
			ProtocolUtils.createXmlField("M2", this.unitNumber, fields);
			ProtocolUtils.createXmlField("M3", this.truckNumber, fields);
			ProtocolUtils.createXmlField("L3", this.vehiclePlateNumber, fields);
			ProtocolUtils.createXmlField("M6", this.poNumber, fields);
			ProtocolUtils.createXmlField("TN", this.trailerNumber, fields);
			ProtocolUtils.createXmlField("O1", this.odometer, fields);
			ProtocolUtils.createXmlField("P1", this.hubometer, fields);
			ProtocolUtils.createXmlField("P2", this.tripNumber, fields);
			ProtocolUtils.createXmlField("DI", this.driverId, fields);
			ProtocolUtils.createXmlField("CD", this.cdlNumber, fields);
			ProtocolUtils.createXmlField("P3", this.driversLicenseState, fields);
			ProtocolUtils.createXmlField("P4", this.trailerHubReading, fields);
			ProtocolUtils.createXmlField("P5", this.trailerHours, fields);
			
		}  else {
			for (String s: prompts.keySet()) {
				ProtocolUtils.createXmlField(s,prompts.get(s),fields);
			}
		}
		
		if (dollarLimit != null) ProtocolUtils.createXmlField("LD", dollarLimit.toPlainString(), fields);
		if (volumeLimit != null) ProtocolUtils.createXmlField("LV", volumeLimit.toPlainString(), fields);
		if (quantityNet != null) ProtocolUtils.createXmlField("Q1", quantityNet.toPlainString(), fields);
		ProtocolUtils.createXmlField("Q2", this.quantityGross, fields);
		ProtocolUtils.createXmlField("PN", this.pumpNumber, fields);
		if (sellingPrice != null) ProtocolUtils.createXmlField("PR", sellingPrice.toPlainString(), fields);
		ProtocolUtils.createXmlField("R1", this.denialReason, fields);
		ProtocolUtils.createXmlField("RT", this.receiptTrailer, fields);
		ProtocolUtils.createXmlField("T1", this.trainingPrompt, fields);
		ProtocolUtils.createXmlField("TD", this.totalizingDollars, fields);
		ProtocolUtils.createXmlField("TE", this.trainingExclusion, fields);
		ProtocolUtils.createXmlField("TV", this.totalizerVolume, fields);
		ProtocolUtils.createXmlField("CL", this.comdataCTL, fields);
		
		for (String s: fields) {
			msg += s;
		}
		
		msg = msg + ProtocolUtils.encodeInteger(ProtocolUtils.calculateCheckDigit(msg));
		return msg;
	}
	
	public void addPrompt(String prompt, String value) {
		
		if (value == null) return;
		
		if (prompts == null) { 
			prompts = new HashMap<String,String>();
			this.setHasStandardPrompts(1);
		}
		prompts.put(prompt, value);
	}
	
	
	public void setResponseFlags(PosAuthorization request) {
		setSource(1);
    	setHasStandardPrompts(0);
    	setHasCustomPrompts(0);
    	if (request != null) {
    		setPinFlag(request.getPinFlag());
    		setTransactionType(request.getTransactionType());
        	setTrainingExclusionTimes(request.getTrainingExclusionTimes());
    	} else {
    		setPinFlag(0);
    		setTransactionType(0);
        	setTrainingExclusionTimes(0);
    	}
	}
	
	public void setAuthorized(String authCode) {
		setAuthorization(1);
		setReAuthorizationFlag(0);
		setAuthId(authCode);
	}
	
	public void setDenied(String denialReason, String message) {
		setAuthorization(0);
		setReAuthorizationFlag(1);
		setDenialReason(denialReason);
		setMessage(message);
	}
	
	public void setReauth(String message) {
		setAuthorization(0);
		setReAuthorizationFlag(1);
		//setMessage(message);
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcotolId() {
		return procotolId;
	}

	public void setProcotolId(String procotolId) {
		this.procotolId = procotolId;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getAuthorization() {
		return authorization;
	}

	public void setAuthorization(int authorization) {
		this.authorization = authorization;
	}

	public int getPinFlag() {
		return pinFlag;
	}

	public void setPinFlag(int pinFlag) {
		this.pinFlag = pinFlag;
	}

	public int getHAsStandardPrompts() {
		return hasStandardPrompts;
	}

	public void setHasStandardPrompts(int standardPrompts) {
		this.hasStandardPrompts = standardPrompts;
	}

	public int getHasCustomPrompts() {
		return hasCustomPrompts;
	}

	public void setHasCustomPrompts(int customPrompts) {
		this.hasCustomPrompts = customPrompts;
	}

	public int getReAuthorizationFlag() {
		return reAuthorizationFlag;
	}

	public void setReAuthorizationFlag(int reAuthorizationFlag) {
		this.reAuthorizationFlag = reAuthorizationFlag;
	}

	public int getReceiptTrailerFlag() {
		return receiptTrailerFlag;
	}

	public void setReceiptTrailerFlag(int receiptTrailerFlag) {
		this.receiptTrailerFlag = receiptTrailerFlag;
	}

	public int getTrainingExclusionTimes() {
		return trainingExclusionTimes;
	}

	public void setTrainingExclusionTimes(int trainingExclusionTimes) {
		this.trainingExclusionTimes = trainingExclusionTimes;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getTrnNo() {
		return trnNo;
	}

	public void setTrnNo(String trnNo) {
		this.trnNo = trnNo;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCard1() {
		return card1;
	}

	public void setCard1(String card1) {
		this.card1 = card1;
	}

	public String getCard2() {
		return card2;
	}

	public void setCard2(String card2) {
		this.card2 = card2;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String document) {
		this.documentNumber = document;
	}

	public String getEmailTrigger() {
		return emailTrigger;
	}

	public void setEmailTrigger(String emailTrigger) {
		this.emailTrigger = emailTrigger;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getHoseNumber() {
		return hoseNumber;
	}

	public void setHoseNumber(String hoseNumber) {
		this.hoseNumber = hoseNumber;
	}

	public String getDriversLicense() {
		return driversLicense;
	}

	public void setDriversLicense(String driversLicense) {
		this.driversLicense = driversLicense;
	}

	public String getPropaneLicense() {
		return propaneLicense;
	}

	public void setPropaneLicense(String propaneLicense) {
		this.propaneLicense = propaneLicense;
	}

	public String getMisc() {
		return misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unit) {
		this.unitNumber = unit;
	}

	public String getTruckNumber() {
		return truckNumber;
	}

	public void setTruckNumber(String truck) {
		this.truckNumber = truck;
	}

	public String getDipReading() {
		return dipReading;
	}

	public void setDipReading(String dipReading) {
		this.dipReading = dipReading;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getCompartmentNumber() {
		return compartmentNumber;
	}

	public void setCompartmentNumber(String compartment) {
		this.compartmentNumber = compartment;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flight) {
		this.flightNumber = flight;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registration) {
		this.registrationNumber = registration;
	}

	public String getTailNumber() {
		return tailNumber;
	}

	public void setTailNumber(String tail) {
		this.tailNumber = tail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BigDecimal getDollarLimit() {
		return dollarLimit;
	}

	public void setDollarLimit(BigDecimal dollarLimit) {
		this.dollarLimit = dollarLimit;
	}

	public BigDecimal getVolumeLimit() {
		return volumeLimit;
	}

	public void setVolumeLimit(BigDecimal volumeLimit) {
		this.volumeLimit = volumeLimit;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getOdometer() {
		return odometer;
	}

	public void setOdometer(String odometer) {
		this.odometer = odometer;
	}

	public BigDecimal getQuantityNet() {
		return quantityNet;
	}

	public void setQuantityNet(BigDecimal quantityNet) {
		this.quantityNet = quantityNet;
	}

	public String getQuantityGross() {
		return quantityGross;
	}

	public void setQuantityGross(String quantityGross) {
		this.quantityGross = quantityGross;
	}

	public String getPumpNumber() {
		return pumpNumber;
	}

	public void setPump(String pump) {
		this.pumpNumber = pump;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getDenialReason() {
		return denialReason;
	}

	public void setDenialReason(String denialReason) {
		this.denialReason = denialReason;
	}

	public String getReceiptTrailer() {
		return receiptTrailer;
	}

	public void setReceiptTrailer(String receiptTrailer) {
		this.receiptTrailer = receiptTrailer;
	}

	public String getTrainingPrompt() {
		return trainingPrompt;
	}

	public void setTrainingPrompt(String trainingPrompt) {
		this.trainingPrompt = trainingPrompt;
	}

	public String getTotalizingDollars() {
		return totalizingDollars;
	}

	public void setTotalizingDollars(String totalizingDollars) {
		this.totalizingDollars = totalizingDollars;
	}

	public String getTrainingExclusion() {
		return trainingExclusion;
	}

	public void setTrainingExclusion(String trainingExclusion) {
		this.trainingExclusion = trainingExclusion;
	}

	public String getTotalizerVolume() {
		return totalizerVolume;
	}

	public void setTotalizerVolume(String totalizerVolume) {
		this.totalizerVolume = totalizerVolume;
	}

	public String getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public String getHubometer() {
		return hubometer;
	}

	public void setHubometer(String hubometer) {
		this.hubometer = hubometer;
	}

	public String getTripNumber() {
		return tripNumber;
	}

	public void setTripNumber(String tripNumber) {
		this.tripNumber = tripNumber;
	}

	public int getHasStandardPrompts() {
		return hasStandardPrompts;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getCdlNumber() {
		return cdlNumber;
	}

	public void setCdlNumber(String cdlNumber) {
		this.cdlNumber = cdlNumber;
	}

	public BigDecimal getPriceDiscount() {
		return priceDiscount;
	}

	public void setPriceDiscount(BigDecimal priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getTrack2Data() {
		return track2Data;
	}

	public void setTrack2Data(String track2Data) {
		this.track2Data = track2Data;
	}

	public void setPumpNumber(String pumpNumber) {
		this.pumpNumber = pumpNumber;
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

	public String getFuelTarget() {
		return fuelTarget;
	}

	public void setFuelTarget(String fuelTarget) {
		this.fuelTarget = fuelTarget;
	}

	public String getVehiclePlateNumber() {
		return vehiclePlateNumber;
	}

	public void setVehiclePlateNumber(String vehiclePlateNumber) {
		this.vehiclePlateNumber = vehiclePlateNumber;
	}

	public String getComdataCTL() {
		return comdataCTL;
	}

	public void setComdataCTL(String comdataCTL) {
		this.comdataCTL = comdataCTL;
	}
	
	
}
