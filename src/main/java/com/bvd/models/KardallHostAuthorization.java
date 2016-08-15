package com.bvd.models;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bvd.utils.ProtocolUtils;

@Entity
@Table(name="KardallAuthRequest")
public class KardallHostAuthorization {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@Column
	private String procotolId; 
	@Column
	private int source;
	@Column
	private int authorization;
	@Column
	private int pinFlag;
	@Column
	private int standardPrompts;
	@Column
	private int customPrompts;
	@Column
	private int reAuthorizationFlag;
	@Column
	private int receiptTrailerFlag;
	@Column
	private int trainingExclusionTimes;
	@Column
	private int transactionType;
	@Column
	private String siteId;
	@Column
	private String dateTime;
	@Column
	private String authId;
	@Column
	private String trnNo;
	@Column
	private String reqId;
	@Column
	private BigDecimal amount;
	@Column
	private String card1;
	@Column
	private String card2;
	@Column
	private String cardType;
	@Column
	private String document;
	@Column
	private String emailTrigger;
	@Column
	private String fuelType;
	@Column
	private String hoseNumber;
	@Column
	private String driversLicense;
	@Column
	private String propaneLicense;
	@Column
	private String misc;
	@Column
	private String unit;
	@Column
	private String truck;
	@Column
	private String dipReading;
	@Column
	private String temperature;
	@Column
	private String po;
	@Column
	private String compartment;
	@Column
	private String flight;
	@Column
	private String registration;
	@Column
	private String tail;
	@Column
	private String message;
	@Column
	private BigDecimal dollarLimit;
	@Column
	private BigDecimal volumeLimit;
	@Column
	private String pin;
	@Column
	private Integer odometer;
	@Column
	private BigDecimal quantityNet;
	@Column
	private BigDecimal quantityGross;
	@Column
	private String pump;
	@Column
	private BigDecimal sellingPrice;
	@Column
	private String denialReason;
	@Column
	private String receiptTrailer;
	@Column
	private String trainingPrompt;
	@Column
	private BigDecimal totalizingDollars;
	@Column
	private String trainingExclusion;
	@Column
	private BigDecimal totalizerVolume;

	 

	public KardallHostAuthorization() {
		super();
	}
	
	public KardallHostAuthorization(String message) {
		init(message);
	}

	private void init(String message) {
		// message starts with a 68 byte header
		String header = message.substring(0,68);

		/*  Parse fields  from header as per spec
				  	Field	Length
					ProtocolID	11 an
					ReqBitMap1	1 an
					ReqBitMap2	1 an
					SiteID	12 AN
					DateTime	14 n
					AuthID	12 an
					TrnNo	5 n
					ReqID	12 an
		 */

		this.procotolId = header.substring(0,11);
		
		String reqBitMap1 = Integer.toBinaryString(Character.valueOf(header.charAt(11)));
		while (reqBitMap1.length() < 8) {
			reqBitMap1 = "0" + reqBitMap1;
		}
		
		String reqBitMap2 = Integer.toBinaryString(Character.valueOf(header.charAt(12)));
		while (reqBitMap2.length() < 8) {
			reqBitMap2 = "0" + reqBitMap2;
		}
		
		this.source = Integer.valueOf(String.valueOf(reqBitMap1.charAt(7)));
		this.authorization = Integer.valueOf(String.valueOf(reqBitMap1.charAt(6)));
		this.pinFlag = Integer.valueOf(String.valueOf(reqBitMap1.charAt(5)));
		this.standardPrompts = Integer.valueOf(String.valueOf(reqBitMap1.charAt(4)));
		this.customPrompts = Integer.valueOf(String.valueOf(reqBitMap1.charAt(3))); 
		this.reAuthorizationFlag = Integer.valueOf(String.valueOf(reqBitMap1.charAt(2)));
		this.receiptTrailerFlag = Integer.valueOf(String.valueOf(reqBitMap1.charAt(1))); 
		this.transactionType = Integer.valueOf(String.valueOf(reqBitMap2.charAt(7)));
		this.trainingExclusionTimes = Integer.valueOf(String.valueOf(reqBitMap2.charAt(6)));
	
		this.siteId = header.substring(13,25);
		this.dateTime = header.substring(25,39);
		this.authId = header.substring(39,51);
		this.trnNo = header.substring(51,56);
		this.reqId = header.substring(56,68);

		// now get the xml-ish fields, which start at position 68 and end before the check digit
		String xml = message.substring(68,message.length()-1);
		String[] elements = xml.split("<|/>");
		
		// build a map of element names (keys) and values... we'll use this to initilialize the fields
		Map<String, String> xmlFields = new HashMap<String,String>();
		for (String e:elements) {
			AbstractMap.SimpleEntry<String, String> entry = ProtocolUtils.parseXmlField(e);
			if (entry != null) xmlFields.put(entry.getKey(),entry.getValue());
		}
		
		String a1 = xmlFields.get("A1");
		this.amount = a1 != null ? new BigDecimal(a1):null;
		
		this.card1 = xmlFields.get("C1");
		this.card2 = xmlFields.get("C2");
		this.cardType = xmlFields.get("CT");
		this.document = xmlFields.get("D1");
		this.emailTrigger = xmlFields.get("EM");
		this.fuelType = xmlFields.get("F1");
		this.hoseNumber = xmlFields.get("H1");
		this.driversLicense = xmlFields.get("L1");
		this.propaneLicense = xmlFields.get("L2");
		this.misc = xmlFields.get("M1");
		this.unit= xmlFields.get("M2");
		this.truck = xmlFields.get("M3");
		this.dipReading = xmlFields.get("M4");
		this.temperature = xmlFields.get("M5");
		this.po = xmlFields.get("M6");
		this.compartment = xmlFields.get("M7");
		this.flight = xmlFields.get("M8");
		this.registration = xmlFields.get("M9");
		this.tail = xmlFields.get("M0");
		this.message = xmlFields.get("MG");
		
		String ld = xmlFields.get("LD");
		this.dollarLimit = ld != null ? new BigDecimal(ld):null;
		
		String lv = xmlFields.get("LV");
		this.volumeLimit = lv != null ? new BigDecimal(lv):null;
		
		this.pin = xmlFields.get("N1");
		
		String o1 = xmlFields.get("O1");
		this.odometer = o1 != null ? new Integer(o1):null;
	
		String q1 = xmlFields.get("Q1");
		this.quantityNet = q1 != null ? new BigDecimal(q1):null;
		String q2 = xmlFields.get("Q2");
		this.quantityGross = q2 != null ? new BigDecimal(q2):null;
		
		this.pump = xmlFields.get("PN");
		
		String pr = xmlFields.get("PR");
		this.sellingPrice = pr != null ? new BigDecimal(pr):null;
		this.denialReason = xmlFields.get("R1");
		this.receiptTrailer = xmlFields.get("RT");
		this.trainingPrompt = xmlFields.get("T1");
		
		String td = xmlFields.get("TD");
		this.totalizingDollars = td != null ? new BigDecimal(td):null;
		this.trainingExclusion = xmlFields.get("TE");
		
		String tv = xmlFields.get("TV");
		this.totalizerVolume = tv != null ? new BigDecimal(tv):null;
	}

	public String getProcotolId() {
		return procotolId;
	}

	public void setProcotolId(String procotolId) {
		this.procotolId = procotolId;
	}


	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
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

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTruck() {
		return truck;
	}

	public void setTruck(String truck) {
		this.truck = truck;
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

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public String getCompartment() {
		return compartment;
	}

	public void setCompartment(String compartment) {
		this.compartment = compartment;
	}

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
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

	public Integer getOdometer() {
		return odometer;
	}

	public void setOdometer(Integer odometer) {
		this.odometer = odometer;
	}

	public BigDecimal getQuantityNet() {
		return quantityNet;
	}

	public void setQuantityNet(BigDecimal quantityNet) {
		this.quantityNet = quantityNet;
	}

	public BigDecimal getQuantityGross() {
		return quantityGross;
	}

	public void setQuantityGross(BigDecimal quantityGross) {
		this.quantityGross = quantityGross;
	}

	public String getPump() {
		return pump;
	}

	public void setPump(String pump) {
		this.pump = pump;
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

	public BigDecimal getTotalizingDollars() {
		return totalizingDollars;
	}

	public void setTotalizingDollars(BigDecimal totalizingDollars) {
		this.totalizingDollars = totalizingDollars;
	}

	public String getTrainingExclusion() {
		return trainingExclusion;
	}

	public void setTrainingExclusion(String trainingExclusion) {
		this.trainingExclusion = trainingExclusion;
	}

	public BigDecimal getTotalizerVolume() {
		return totalizerVolume;
	}

	public void setTotalizerVolume(BigDecimal totalizerVolume) {
		this.totalizerVolume = totalizerVolume;
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

	public int getStandardPrompts() {
		return standardPrompts;
	}

	public void setStandardPrompts(int standardPrompts) {
		this.standardPrompts = standardPrompts;
	}

	public int getCustomPrompts() {
		return customPrompts;
	}

	public void setCustomPrompts(int customPrompts) {
		this.customPrompts = customPrompts;
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

	
}
