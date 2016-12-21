package com.bvd.paymentswitch.processing.provider;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.processing.handler.AuthorizationHandler;
import com.bvd.paymentswitch.processing.handler.ComdataAuthorizationHandler;
import com.bvd.paymentswitch.protocol.ComdataDecoder;
import com.bvd.paymentswitch.protocol.ComdataEncoder;
import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ComdataProcessingProvider extends AbstractProcessingProvider {

	static final Logger logger = LoggerFactory.getLogger(ComdataProcessingProvider.class);

	@Override
	public ByteBuf getFrameDelimiter() {
		return ASCIIChars.ASC125_DELIMITER ;
	}

	@Override
	public void setProtocolCodecs() {
		encoder = new ComdataEncoder();
		decoder = new ComdataDecoder();
	}

	@Override
	public AuthorizationHandler getAuthorizationHandler(PosAuthorization posRequest, ChannelHandlerContext posCtx) {
		// TODO Auto-generated method stub
		AuthorizationHandler handler = new ComdataAuthorizationHandler();
		handler.initializePOSContext(posRequest, this, posCtx);
		return handler;
	}

	@Override
	public ProcessorAuthorization parseProcessorResponse(PosAuthorization posRequest, String response) {
		ProcessorAuthorization processorResponse = new ProcessorAuthorization();
		processorResponse.setCardNumber(posRequest.getCard1());
		processorResponse.setCardToken(posRequest.getTrack2Data());
		processorResponse.setUnitNumber(posRequest.getUnitNumber());
		processorResponse.setInvoiceNumber(posRequest.getTrnNo());
		
		String header = response.substring(0,17);
		String body = response.substring(18);
		
		processorResponse.setLocation(header.substring(0,5));
		processorResponse.setVersionNumber(header.substring(5,6));
		processorResponse.setSoftwareSystem(header.substring(6,10));
		
		String report = header.substring(10,17);
		processorResponse.setType(report);
		
		String[] fields = body.split(String.valueOf(ASCIIChars.ASC47));
		String responseCode = fields[0];
		processorResponse.setResponseCode(responseCode);
		
		String reply = fields[1];
		
		if (report.equalsIgnoreCase("SP00007")) {
			parseSP7(processorResponse, reply);
		} else if (report.equalsIgnoreCase("SP00014")) {
			parseSP14(processorResponse, posRequest, reply);
		} else {
			parseSP11(processorResponse, reply);
		}
		
		return processorResponse;
	}

	private void parseSP11(ProcessorAuthorization processorResponse, String reply) {
		if (processorResponse.getResponseCode().equals("00000")) {
			// good response
			int indexOfAuth = reply.indexOf("CTL#");
			int indexOfAmount = reply.indexOf("$");
			int indexOfTax = reply.indexOf("TAX");
			
			
			if (indexOfAuth > -1) {
				String authCode = reply.substring(indexOfAuth + 4, indexOfAuth + 10).trim();
				processorResponse.setAuthorizationCode(authCode);
			}
			
			if (indexOfAmount > -1) {
				int endIndex = ((indexOfAmount + 10) <= reply.length())?indexOfAmount+10:reply.length();
				String amount = reply.substring(indexOfAmount + 1, endIndex).trim();
				processorResponse.setTotal(ProtocolUtils.getBigDecimal(amount, 2));
			}
			
			if (indexOfTax > -1) {
				int endIndex = ((indexOfTax + 13) <= reply.length())?indexOfTax+13:reply.length();
				String message = reply.substring(indexOfTax, endIndex).trim();
				processorResponse.setMessage(message);
			} 

			
		} else {
			// failed response
			processorResponse.setMessage(reply);
			processorResponse.setErrorCode(processorResponse.getResponseCode());
		}
		
		
	}

	private void parseSP14(ProcessorAuthorization processorResponse, PosAuthorization posRequest, String reply) {
		if (processorResponse.getResponseCode().equals("00000")) {
			// good response
			
			
			String maxDollarLimit = reply.substring(0,7).trim();
			maxDollarLimit = maxDollarLimit.substring(0, maxDollarLimit.length()-2) + "." + maxDollarLimit.substring(maxDollarLimit.length()-2);
			BigDecimal maxByDollarLimit = ProtocolUtils.getBigDecimal(maxDollarLimit,2);
			
			String maxQuantityLimit = reply.substring(7,14).trim();
			maxQuantityLimit = maxQuantityLimit.substring(0, maxQuantityLimit.length()-2) + "." + maxQuantityLimit.substring(maxQuantityLimit.length()-2);
			BigDecimal maxByQuantityLimit = ProtocolUtils.getBigDecimal(maxQuantityLimit,2);
			
			BigDecimal maxBySellingPrice = maxByQuantityLimit.multiply(posRequest.getSellingPrice());
			BigDecimal max = (maxBySellingPrice.compareTo(maxByDollarLimit) < 0) ? maxBySellingPrice:maxByDollarLimit;
			
			
			String purchaseCategory = reply.substring(98,103).trim();
			
			String fuelDollarLimit = "0000000";
			String fuelQuantityLimit = "0000000";
			
			int productType =  posRequest.getFuelCode().getProductType();
			switch (productType) {
				case 0:
					if ("20004".equals(purchaseCategory)) {
						fuelDollarLimit = reply.substring(77,84).trim(); 
						fuelQuantityLimit = reply.substring(84,91).trim();
					}
					break;
				case 1: 
					if ("20002".equals(purchaseCategory)) {
						fuelDollarLimit = reply.substring(49,56).trim();
						fuelQuantityLimit = reply.substring(56,63).trim();
					}
					break;
				case 2:
					if ("20001".equals(purchaseCategory)) {
						fuelDollarLimit = reply.substring(35,42).trim();
						fuelQuantityLimit = reply.substring(42,49).trim();
					}
					break;
				case 3:
					if ("20003".equals(purchaseCategory)) {
						fuelDollarLimit = reply.substring(63,70).trim(); 
						fuelQuantityLimit = reply.substring(70,77).trim();
					}
					break;
				default:
					break;
			}
			
			
			fuelDollarLimit = fuelDollarLimit.substring(0, fuelDollarLimit.length()-2) + "." + fuelDollarLimit.substring(fuelDollarLimit.length()-2);
			fuelQuantityLimit = fuelQuantityLimit.substring(0, fuelQuantityLimit.length()-2) + "." + fuelQuantityLimit.substring(fuelQuantityLimit.length()-2);
			
			//logger.debug("Category: " + purchaseCategory + ", Product Type: " + productType + ", Limit: " + fuelDollarLimit);
			
			BigDecimal fuelByDollarLimit = ProtocolUtils.getBigDecimal(fuelDollarLimit,2);
			BigDecimal fuelByQuantityLimit = ProtocolUtils.getBigDecimal(fuelQuantityLimit,2);
			

			BigDecimal fuelMaxBySellingPrice = fuelByQuantityLimit.multiply(posRequest.getSellingPrice());
			BigDecimal fuelMax = (fuelMaxBySellingPrice.compareTo(fuelByDollarLimit) < 0) ? fuelMaxBySellingPrice:fuelByDollarLimit;
			
			
			
			BigDecimal fuelLimit = (fuelMax.compareTo(max) < 0) ? fuelMax:max;
			
			logger.debug("Category: " + purchaseCategory + ", Product Type: " + productType + ", Limit: " + fuelLimit);
			
			if (fuelLimit != null && fuelLimit.floatValue()  > 0) {
				processorResponse.setTotal(fuelLimit);
			} else {
				// deny
				processorResponse.setMessage("Fuel Not Authorized");
				processorResponse.setResponseCode("00050");
				processorResponse.setErrorCode("00050");
			}

		} else {
			// failed response
			processorResponse.setMessage(reply);
			processorResponse.setErrorCode(processorResponse.getResponseCode());
		}
		
		
	}

	private void parseSP7(ProcessorAuthorization processorResponse, String reply) {
		
		if (processorResponse.getResponseCode().equals("00000")) {
			// good response
			processorResponse.setDriverID(parsePrompt(reply.substring(0,1), reply.substring(1, 17),null,null,16));
			processorResponse.setTrailerNumber(parsePrompt(reply.substring(17,18),reply.substring(18,28),null,null,10));
			
			processorResponse.setHubReading(parsePrompt(reply.substring(28,29), null, reply.substring(29,35),reply.substring(35,41),6));
		
			processorResponse.setTrailerHubReading(parsePrompt(reply.substring(41,42),null,null,null,6));
			processorResponse.setTrailerHours(parsePrompt(reply.substring(42,43),null,null,null,6));
			
			processorResponse.setTrip(parsePrompt(reply.substring(43,44),reply.substring(46,56),null,null,10));
			processorResponse.setDriversLicenseNumber(parsePrompt(reply.substring(56,57),reply.substring(57,77),null,null,20));
			processorResponse.setDriversLicenseState(parsePrompt(reply.substring(56,57),reply.substring(77,79), null, null, 2));
			processorResponse.setPoNumber(parsePrompt(reply.substring(79,80),reply.substring(80,90),null,null,10));
			
			//String lastName = reply.substring(90,110);
			//String firstName = reply.substring(110,125);
			
		} else {
			// failed response
			processorResponse.setMessage(reply);
			processorResponse.setErrorCode(processorResponse.getResponseCode());
		}
		
	}
	
	
	private String parsePrompt(String indicator, String data, String low, String high, int maxLength) {
		if (indicator == null) { 
			return null;
		}
		indicator = indicator.trim();
		
		if (indicator.startsWith("N") || indicator.startsWith("O") || indicator.startsWith("X") || indicator.length() == 0) return null;
		
		if (indicator.startsWith("C")) {
			return "L," + maxLength;
		} else if (indicator.startsWith("V") || indicator.startsWith("E"))  {
			return "V," + ((data != null) ? data.trim():data);
		} else if (indicator.startsWith("R")) {
			return "V,M"+ low + ":X" + high;
		} else { 
			return null;
		}
	}
	
	
	
	

	
	@Override
	public String formatProcessorRequest(ProcessorAuthorization processorRequest) {
		
		String report = processorRequest.getType();
		
		String fs = String.valueOf(ASCIIChars.ASC47);
	
		String msg  = processorRequest.getLocation() + "T" + paymentProcessor.getSoftwareSystem() + report + fs;
		
		if (report.equalsIgnoreCase("SP00007")) {
			msg += "00036" + fs + "A" + processorRequest.getCardToken() + fs + processorRequest.getUnitNumber();
		} else if (report.equalsIgnoreCase("SP00011")) {
			
			FuelCode fc = processorRequest.getFuelCode();
			msg += "00048" + fs + "A" + processorRequest.getCardToken() + fs + processorRequest.getUnitNumber() 
			+ fs + fs + fs + fs + processorRequest.getInvoiceNumber() + fs + processorRequest.getFuel() 
			+ fs + fs + fs + fs + fs + fs + fs + fs + fs + fs + fs + processorRequest.getTotal() + fs  + processorRequest.getTrip() 
			+ fs + processorRequest.getHubReading() + fs + fs + processorRequest.getDriversLicenseNumber() + fs + processorRequest.getDriversLicenseState()
			+ fs + processorRequest.getTrailerNumber() + fs + processorRequest.getTrailerHubReading() + fs + processorRequest.getPoNumber()
			+ fs + processorRequest.getDriverID() + fs;  
			
			switch (fc.getProductType()) {
				case 0:
					msg += fs + fs + fs + fs + fs + fs + fs + fs + fs + fc.getComdataCode() + fs + fs + fs + fs ;
					break;
				case 1:
					msg += processorRequest.getFuel()  + fs + fs + fs + fs + fs + fs + fs + fs + fs + fc.getComdataCode() + fs + fs + fs ;
					break;
				case 2:
					msg += fs + fs + fs + fs + fs + fs + fs + fc.getComdataCode() + fs +  fs + fs + fs + fs + fs ;
					break;
				case 3:
					msg += fs + fs + fs + fs + fs + fs + fs + fs + fc.getComdataCode() + fs + fs + fs + fs + fs ;
					break;
				default:
					msg += fs + fs + fs + fs + fs + fs + fs + fs + fs + fc.getComdataCode() + fs + fs + fs + fs ;
					break;
			}
			
		
		} else {
			
			processorRequest.setTrailerHubReading("150000");
			processorRequest.setTrailerHours("2000");
			processorRequest.setDriversLicenseState("ON");
			
			msg += "00085" + fs + "A" + processorRequest.getCardToken() + fs + processorRequest.getDriverID() 
					+ fs + processorRequest.getUnitNumber() + fs + processorRequest.getTrailerNumber() + fs + processorRequest.getHubReading()
					+ fs + processorRequest.getTrailerHubReading() + fs + processorRequest.getTrailerHours() + fs +  processorRequest.getTrip() 
					+ fs + processorRequest.getDriversLicenseNumber() + fs + processorRequest.getDriversLicenseState()
					+ fs + processorRequest.getPoNumber() + fs + "P" + fs +  processorRequest.getFuelCode().getComdataCode();
		}
	
		
		return msg;
	}

	@Override
	public ProcessorAuthorization createProcessorRequest(PosAuthorization posRequest, String merchantCode) {
		ProcessorAuthorization processorRequest = new ProcessorAuthorization();
	    
		processorRequest.setLocation(merchantCode);
		processorRequest.setLanguage(paymentProcessor.getLanguage());
		processorRequest.setSoftwareSystem(paymentProcessor.getSoftwareSystem());
		processorRequest.setUnitofMeasure(paymentProcessor.getUnitOfMeasure());
   

		// map common fields
		int transType = posRequest.getTransactionType();
		processorRequest.setInvoiceNumber(posRequest.getTrnNo());
		processorRequest.setCardNumber(posRequest.getCard1());
		processorRequest.setCardToken(posRequest.getTrack2Data());
		processorRequest.setPosCurrency("CAD"); // canadian funds
		processorRequest.setRegisterIndicator("C"); // transaction originated from card reader
		processorRequest.setVersionNumber(" ");
		processorRequest.setCount(1);

		// prompts
		processorRequest.setDriverID(posRequest.getDriverId());
		processorRequest.setDriversLicenseNumber(posRequest.getDriversLicense());
		processorRequest.setUnitNumber(posRequest.getUnitNumber());
		processorRequest.setPoNumber(posRequest.getPoNumber());
		processorRequest.setOdometerReading(posRequest.getOdometer());
		processorRequest.setTrailerNumber(posRequest.getTrailerNumber());
		processorRequest.setHubReading(posRequest.getHubometer());
		processorRequest.setTrip(posRequest.getTripNumber());
		processorRequest.setVehiclePlateNumber(posRequest.getTruckNumber());
		processorRequest.setDriversLicenseState(posRequest.getDriversLicenseState());
		processorRequest.setTrailerHubReading(posRequest.getTrailerHubReading());
		processorRequest.setTrailerHours(posRequest.getTrailerHours());

		processorRequest.setFuelCode(posRequest.getFuelCode());  
		
		// specific tokens by lifecycle
		if (transType == 0) {
			// this is a prior
			// check to see if there has already been an SP00007
			
			ProcessorAuthorization sp7 = authService.findProcessorAuthorization(posRequest.getTrnNo(), posRequest.getCard1(), posRequest.getUnitNumber(), "SP00007","00000");
			
			if (sp7 == null) {
				processorRequest.setType("SP00007");
			} else {
				processorRequest.setType("SP00014");
			}
			
			//processorRequest.setFuel(fuelToken);
		} else {
			// this  is a post (completed transaction)
			processorRequest.setType("SP00011");
			processorRequest.setAuthorizationCode(posRequest.getAuthId().trim());
			processorRequest.setCustomerInformation("I");
			
			//BigDecimal sellingPrice = posRequest.getSellingPrice();
			BigDecimal quantity = posRequest.getQuantityNet();
			BigDecimal amount =  posRequest.getAmount();

			if (quantity != null) quantity = quantity.setScale(2);
			if (amount != null) amount = amount.setScale(2);
			
			String fuelToken = String.valueOf(quantity) + ASCIIChars.ASC47 + amount;
			processorRequest.setTotal(amount);
			processorRequest.setFuel(fuelToken);
		}

		return processorRequest;
	}

}
