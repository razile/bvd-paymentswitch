package com.bvd.paymentswitch.web.service.models.reject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonRootName(value="Transaction")
public class RejectedAuthorization {

	private String transactionID;
	private String outsourceID;
	private String companyNumber;
	private String accountNumber;
	
	private String cardGroup;
	private String referenceDateField;
	private String referenceTimeField;
	private String referenceValueField;
	private String transactionDate;
	private String transactionTime;
	private String recordType;
	private String transactionType;
	private String errorNumber;
	private String errorMessage;
	private String cardNumber;
	private String siteNumber;
	private String groupNumber;
	private String affiliate1;
	private String affiliate2;
	private String affiliate3;
	private String affiliateBilling;
	private String siteName;
	private String siteCity;
	private String siteStateProv;
	private String cardType;
	private String pOSVehicleNumber;
	private String vehicleNumber;
	private String pOSTrailerNumber;
	private String trailerNumber;
	private String pOSDriverCDL;
	private String driverCDL;
	private String driverCDLSt;
	private String pOSDriverID;
	private String driverID;
	private String driverName;
	private String odometer;
	private String previousOdometer;
	private String pOSTrip;
	private String trip;
	private String pONumber;
	private String networkOverride;
	private String unitofMeasure;
	private String fuelCode1;
	private String fuelQuantity1;
	private String fuelAmount1;
	private String fuel1PPU;
	private String fuelCode2;
	private String fuelQuantity2;
	private String fuelAmount2;
	private String fuel2PPU;
	private String nonhighwayCode;
	private String nonhighwayQuantity;
	private String nonhighwayAmount;
	private String nonhighwayPPU;
	private String oilQuantity;
	private String oilAmount;
	private String oilPPU;
	private String other1Code;
	private String other1Amount;
	private String other2Code;
	private String other2Amount;
	private String cash;
	private String dashCash;
	private String payChekAmount;
	private String discountType;
	private String discountAmount;
	private String discountValue;
	private String siteCurrency;
	private String siteInvoiceAmount;
	private String billInvoiceAmount;
	private String exchangeRate;
	private String invoiceNumber;
	private String fee;
	private String payChekFee;
	private String payFlag;
	private String draftNumber;

	
	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getOutsourceID() {
		return outsourceID;
	}

	public void setOutsourceID(String outsourceID) {
		this.outsourceID = outsourceID;
	}

	public String getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getCardGroup() {
		return cardGroup;
	}

	public void setCardGroup(String cardGroup) {
		this.cardGroup = cardGroup;
	}

	public String getReferenceDateField() {
		return referenceDateField;
	}

	public void setReferenceDateField(String referenceDateField) {
		this.referenceDateField = referenceDateField;
	}

	public String getReferenceTimeField() {
		return referenceTimeField;
	}

	public void setReferenceTimeField(String referenceTimeField) {
		this.referenceTimeField = referenceTimeField;
	}

	public String getReferenceValueField() {
		return referenceValueField;
	}

	public void setReferenceValueField(String referenceValueField) {
		this.referenceValueField = referenceValueField;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(String errorNumber) {
		this.errorNumber = errorNumber;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getAffiliate1() {
		return affiliate1;
	}

	public void setAffiliate1(String affiliate1) {
		this.affiliate1 = affiliate1;
	}

	public String getAffiliate2() {
		return affiliate2;
	}

	public void setAffiliate2(String affiliate2) {
		this.affiliate2 = affiliate2;
	}

	public String getAffiliate3() {
		return affiliate3;
	}

	public void setAffiliate3(String affiliate3) {
		this.affiliate3 = affiliate3;
	}

	public String getAffiliateBilling() {
		return affiliateBilling;
	}

	public void setAffiliateBilling(String affiliateBilling) {
		this.affiliateBilling = affiliateBilling;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteCity() {
		return siteCity;
	}

	public void setSiteCity(String siteCity) {
		this.siteCity = siteCity;
	}

	public String getSiteStateProv() {
		return siteStateProv;
	}

	public void setSiteStateProv(String siteStateProv) {
		this.siteStateProv = siteStateProv;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getpOSVehicleNumber() {
		return pOSVehicleNumber;
	}

	public void setpOSVehicleNumber(String pOSVehicleNumber) {
		this.pOSVehicleNumber = pOSVehicleNumber;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getpOSTrailerNumber() {
		return pOSTrailerNumber;
	}

	public void setpOSTrailerNumber(String pOSTrailerNumber) {
		this.pOSTrailerNumber = pOSTrailerNumber;
	}

	public String getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public String getpOSDriverCDL() {
		return pOSDriverCDL;
	}

	public void setpOSDriverCDL(String pOSDriverCDL) {
		this.pOSDriverCDL = pOSDriverCDL;
	}

	public String getDriverCDL() {
		return driverCDL;
	}

	public void setDriverCDL(String driverCDL) {
		this.driverCDL = driverCDL;
	}

	public String getDriverCDLSt() {
		return driverCDLSt;
	}

	public void setDriverCDLSt(String driverCDLSt) {
		this.driverCDLSt = driverCDLSt;
	}

	public String getpOSDriverID() {
		return pOSDriverID;
	}

	public void setpOSDriverID(String pOSDriverID) {
		this.pOSDriverID = pOSDriverID;
	}

	public String getDriverID() {
		return driverID;
	}

	public void setDriverID(String driverID) {
		this.driverID = driverID;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getOdometer() {
		return odometer;
	}

	public void setOdometer(String odometer) {
		this.odometer = odometer;
	}

	public String getPreviousOdometer() {
		return previousOdometer;
	}

	public void setPreviousOdometer(String previousOdometer) {
		this.previousOdometer = previousOdometer;
	}

	public String getpOSTrip() {
		return pOSTrip;
	}

	public void setpOSTrip(String pOSTrip) {
		this.pOSTrip = pOSTrip;
	}

	public String getTrip() {
		return trip;
	}

	public void setTrip(String trip) {
		this.trip = trip;
	}

	public String getpONumber() {
		return pONumber;
	}

	public void setpONumber(String pONumber) {
		this.pONumber = pONumber;
	}

	public String getNetworkOverride() {
		return networkOverride;
	}

	public void setNetworkOverride(String networkOverride) {
		this.networkOverride = networkOverride;
	}

	public String getUnitofMeasure() {
		return unitofMeasure;
	}

	public void setUnitofMeasure(String unitofMeasure) {
		this.unitofMeasure = unitofMeasure;
	}

	public String getFuelCode1() {
		return fuelCode1;
	}

	public void setFuelCode1(String fuelCode1) {
		this.fuelCode1 = fuelCode1;
	}

	public String getFuelQuantity1() {
		return fuelQuantity1;
	}

	public void setFuelQuantity1(String fuelQuantity1) {
		this.fuelQuantity1 = fuelQuantity1;
	}

	public String getFuelAmount1() {
		return fuelAmount1;
	}

	public void setFuelAmount1(String fuelAmount1) {
		this.fuelAmount1 = fuelAmount1;
	}

	public String getFuel1PPU() {
		return fuel1PPU;
	}

	public void setFuel1PPU(String fuel1ppu) {
		fuel1PPU = fuel1ppu;
	}

	public String getFuelCode2() {
		return fuelCode2;
	}

	public void setFuelCode2(String fuelCode2) {
		this.fuelCode2 = fuelCode2;
	}

	public String getFuelQuantity2() {
		return fuelQuantity2;
	}

	public void setFuelQuantity2(String fuelQuantity2) {
		this.fuelQuantity2 = fuelQuantity2;
	}

	public String getFuelAmount2() {
		return fuelAmount2;
	}

	public void setFuelAmount2(String fuelAmount2) {
		this.fuelAmount2 = fuelAmount2;
	}

	public String getFuel2PPU() {
		return fuel2PPU;
	}

	public void setFuel2PPU(String fuel2ppu) {
		fuel2PPU = fuel2ppu;
	}

	public String getNonhighwayCode() {
		return nonhighwayCode;
	}

	public void setNonhighwayCode(String nonhighwayCode) {
		this.nonhighwayCode = nonhighwayCode;
	}

	public String getNonhighwayQuantity() {
		return nonhighwayQuantity;
	}

	public void setNonhighwayQuantity(String nonhighwayQuantity) {
		this.nonhighwayQuantity = nonhighwayQuantity;
	}

	public String getNonhighwayAmount() {
		return nonhighwayAmount;
	}

	public void setNonhighwayAmount(String nonhighwayAmount) {
		this.nonhighwayAmount = nonhighwayAmount;
	}

	public String getNonhighwayPPU() {
		return nonhighwayPPU;
	}

	public void setNonhighwayPPU(String nonhighwayPPU) {
		this.nonhighwayPPU = nonhighwayPPU;
	}

	public String getOilQuantity() {
		return oilQuantity;
	}

	public void setOilQuantity(String oilQuantity) {
		this.oilQuantity = oilQuantity;
	}

	public String getOilAmount() {
		return oilAmount;
	}

	public void setOilAmount(String oilAmount) {
		this.oilAmount = oilAmount;
	}

	public String getOilPPU() {
		return oilPPU;
	}

	public void setOilPPU(String oilPPU) {
		this.oilPPU = oilPPU;
	}

	public String getOther1Code() {
		return other1Code;
	}

	public void setOther1Code(String other1Code) {
		this.other1Code = other1Code;
	}

	public String getOther1Amount() {
		return other1Amount;
	}

	public void setOther1Amount(String other1Amount) {
		this.other1Amount = other1Amount;
	}

	public String getOther2Code() {
		return other2Code;
	}

	public void setOther2Code(String other2Code) {
		this.other2Code = other2Code;
	}

	public String getOther2Amount() {
		return other2Amount;
	}

	public void setOther2Amount(String other2Amount) {
		this.other2Amount = other2Amount;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getDashCash() {
		return dashCash;
	}

	public void setDashCash(String dashCash) {
		this.dashCash = dashCash;
	}

	public String getPayChekAmount() {
		return payChekAmount;
	}

	public void setPayChekAmount(String payChekAmount) {
		this.payChekAmount = payChekAmount;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(String discountValue) {
		this.discountValue = discountValue;
	}

	public String getSiteCurrency() {
		return siteCurrency;
	}

	public void setSiteCurrency(String siteCurrency) {
		this.siteCurrency = siteCurrency;
	}

	public String getSiteInvoiceAmount() {
		return siteInvoiceAmount;
	}

	public void setSiteInvoiceAmount(String siteInvoiceAmount) {
		this.siteInvoiceAmount = siteInvoiceAmount;
	}

	public String getBillInvoiceAmount() {
		return billInvoiceAmount;
	}

	public void setBillInvoiceAmount(String billInvoiceAmount) {
		this.billInvoiceAmount = billInvoiceAmount;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getPayChekFee() {
		return payChekFee;
	}

	public void setPayChekFee(String payChekFee) {
		this.payChekFee = payChekFee;
	}

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getDraftNumber() {
		return draftNumber;
	}

	public void setDraftNumber(String draftNumber) {
		this.draftNumber = draftNumber;
	}

}
