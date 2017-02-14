package com.bvd.paymentswitch.processing.provider;

import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.PosAuthorization;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;
import io.netty.buffer.ByteBuf;

public abstract class PriorPostAbstractProcessingProvider extends AbstractProcessingProvider {
	
	public abstract String getCardKey();
	public abstract String getCardValue(ProcessorAuthorization processorRequest);
	
	public abstract void setPreAuthFuelTokens(PosAuthorization posRequest, ProcessorAuthorization processorRequest);
	public abstract void setPostAuthFuelTokens(PosAuthorization posRequest, ProcessorAuthorization processorRequest);
	
	public abstract boolean setFuelLimit(BigDecimal maxVolume, BigDecimal maxAmount, BigDecimal sellingPrice, PosAuthorization posResponse, ProcessorAuthorization processorResponse);
	
	
	
	@Override 
	public ByteBuf getFrameDelimiter() {
		return ASCIIChars.ETX_DELIMITER;
	}
	
	@Override
	public ProcessorAuthorization parseProcessorResponse(PosAuthorization posRequest, String response) {
		ProcessorAuthorization processorResponse = new ProcessorAuthorization();
		String[] fields = response.split("\\|");
		
		processorResponse.setType(fields[1]);
		processorResponse.setResponseCode(fields[1]);
		processorResponse.setLocation(fields[2]);
		processorResponse.setVersionNumber(fields[3]);
		processorResponse.setCount(Integer.parseInt(fields[4]));
		
		Map<String, String> tokens = new HashMap<String, String>();
		for (int i = 5; i<fields.length; i++) {
			AbstractMap.SimpleEntry<String, String> entry = ProtocolUtils.parseToken(fields[i]);
			if (entry != null)
				tokens.put(entry.getKey(), entry.getValue());
		}

		processorResponse.setSoftwareSystem(tokens.get("SSVR"));    	// Character (5)	SSRV:XXXXX		
		processorResponse.setPosCurrency(tokens.get("CNC"));
		processorResponse.setLanguage(tokens.get("LNG"));
		processorResponse.setUnitofMeasure(tokens.get("UOM"));
		processorResponse.setRegisterIndicator(tokens.get("FROM"));
		processorResponse.setCustomerInformation(tokens.get("CUST"));
		processorResponse.setInvoiceNumber(tokens.get("INVN"));
		processorResponse.setCardToken(tokens.get("CDSW"));
		
		String cardNumber = tokens.get("CARD");
		if (cardNumber != null) {
			processorResponse.setCardNumber(cardNumber.split(",")[0]);
		}
		
		processorResponse.setHubReading(tokens.get("HBRD"));
		processorResponse.setTrailerNumber(tokens.get("TRLR"));
		processorResponse.setUnitNumber(tokens.get("UNIT"));
		processorResponse.setDriverID(tokens.get("DRID"));
		processorResponse.setDriversLicenseNumber(tokens.get("DLIC"));
		processorResponse.setOdometerReading(tokens.get("ODRD"));
		processorResponse.setTrip(tokens.get("TRIP"));
		processorResponse.setVehiclePlateNumber(tokens.get("LICN"));
		processorResponse.setPoNumber(tokens.get("PONB"));
		
		processorResponse.setAuthorizationCode(tokens.get("AUTH"));
		processorResponse.setFuel(tokens.get("FUEL"));
		processorResponse.setFuelLimit(tokens.get("FLMT"));
		processorResponse.setReeferLimit(tokens.get("RFR"));
		
		if (tokens.get("TOTL") != null) processorResponse.setTotal(ProtocolUtils.getBigDecimal(tokens.get("TOTL"),2));
		if (tokens.get("NCDV") != null) processorResponse.setNonCADVTotal(ProtocolUtils.getBigDecimal(tokens.get("NCDV"),2));
		
		processorResponse.setErrorCode(tokens.get("ERCD"));
		processorResponse.setPrint(tokens.get("PRNT"));
		processorResponse.setMessage(tokens.get("MSG"));		
		
		
		return processorResponse;
	}

	@Override
	public ProcessorAuthorization createProcessorRequest(PosAuthorization posRequest, String merchantCode) {
		ProcessorAuthorization processorRequest = new ProcessorAuthorization();
    
		processorRequest.setLocation(merchantCode);
		processorRequest.setLanguage(paymentProcessor.getLanguage());
		processorRequest.setSoftwareSystem(paymentProcessor.getSoftwareSystem());
		processorRequest.setUnitofMeasure(paymentProcessor.getUnitOfMeasure());
        
       // if (posRequest.getFuelCode() != null) {
       // 	request.setFuelType(posRequest.getFuelCode().getEfsCode());
       // }

		// map common fields
		int transType = posRequest.getTransactionType();
		processorRequest.setInvoiceNumber(posRequest.getTrnNo());
		processorRequest.setCardNumber(posRequest.getCard1() + ",S");
		processorRequest.setCardToken(posRequest.getTrack2Data());
		processorRequest.setPosCurrency("CAD"); // canadian funds
		processorRequest.setRegisterIndicator("C"); // transaction originated from card reader
		processorRequest.setVersionNumber(paymentProcessor.getVersionNumber());   // 01.10
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


		// specific tokens by lifecycle
		if (transType == 0) {
			// this is a prior
			processorRequest.setType("IC");
			processorRequest.setCustomerInformation("I");
			
			setPreAuthFuelTokens(posRequest, processorRequest);
			
		} else {
			// this is a post (completed transaction)
			processorRequest.setType("AC");
			processorRequest.setAuthorizationCode(posRequest.getAuthId().trim());
			processorRequest.setCustomerInformation("I");
			
		    setPostAuthFuelTokens(posRequest, processorRequest);
		}

		return processorRequest;
	}
	
	@Override
	public String formatProcessorRequest(ProcessorAuthorization processorRequest) {
		String msg = "|" + processorRequest.getType() + "|" + processorRequest.getLocation() + "|" + processorRequest.getVersionNumber() + "|" +  processorRequest.getCount() + "|";
		
		List<String> tokens = new ArrayList<String>();
		
		ProtocolUtils.createToken("CUST", processorRequest.getCustomerInformation(), tokens);
		ProtocolUtils.createToken("SSVR", processorRequest.getSoftwareSystem(), tokens);   	
		ProtocolUtils.createToken("CNC", processorRequest.getPosCurrency(), tokens);
		ProtocolUtils.createToken("LNG", processorRequest.getLanguage(), tokens);
		ProtocolUtils.createToken("UOM", processorRequest.getUnitofMeasure(), tokens);
		ProtocolUtils.createToken("FROM", processorRequest.getRegisterIndicator(), tokens);
	
		ProtocolUtils.createToken("INVN", processorRequest.getInvoiceNumber(), tokens);
		
		ProtocolUtils.createToken(getCardKey(), getCardValue(processorRequest), tokens);
		//ProtocolUtils.createToken("CDSW", processorRequest.getCardToken(), tokens);
		//ProtocolUtils.createToken("CARD", processorRequest.getCardNumber(), tokens);
		
		ProtocolUtils.createToken("AUTH", processorRequest.getAuthorizationCode(), tokens);
		
	
		ProtocolUtils.createToken("FUEL", processorRequest.getFuel(), tokens);
		ProtocolUtils.createToken("FLMT", processorRequest.getFuelLimit(), tokens);

		if (processorRequest.getSellingPrice() != null) ProtocolUtils.createToken("PPRC", processorRequest.getSellingPrice().toPlainString(), tokens);
		if (processorRequest.getTotal() != null) ProtocolUtils.createToken("TOTL", processorRequest.getTotal().toPlainString(), tokens);
		
		ProtocolUtils.createToken("DISP", processorRequest.getDispensed(), tokens);
		ProtocolUtils.createToken("MERC", processorRequest.getMerchandise(), tokens);
		
		ProtocolUtils.createToken("HBRD", processorRequest.getHubReading(), tokens);
		ProtocolUtils.createToken("TRLR", processorRequest.getTrailerNumber(), tokens);
		ProtocolUtils.createToken("UNIT", processorRequest.getUnitNumber(), tokens);
		ProtocolUtils.createToken("DRID", processorRequest.getDriverID(), tokens);
		ProtocolUtils.createToken("DLIC", processorRequest.getDriversLicenseNumber(), tokens);
		ProtocolUtils.createToken("ODRD", processorRequest.getOdometerReading(), tokens);
		ProtocolUtils.createToken("TRIP", processorRequest.getTrip(), tokens);
		ProtocolUtils.createToken("LICN", processorRequest.getVehiclePlateNumber(), tokens);
		ProtocolUtils.createToken("PONB", processorRequest.getPoNumber(), tokens);

		ProtocolUtils.createToken("ERCD", processorRequest.getErrorCode(), tokens);
		ProtocolUtils.createToken("PRNT", processorRequest.getPrint(), tokens);
		ProtocolUtils.createToken("MSG", processorRequest.getMessage(), tokens);
		
		for (String s : tokens) {
			msg += s + "|";
		}
		msg = msg.substring(0, msg.length()-1);  // strip off the last | 
	
		return msg;
	}
	
	@Override
	public PosAuthorization createPosResponse(PosAuthorization posRequest, ProcessorAuthorization processorResponse) {
		PosAuthorization posResponse = new PosAuthorization(posRequest);
		posResponse.setResponseFlags(posRequest);
    	
    	String type = processorResponse.getType();
		boolean fuelMatched = false;
		String target = posRequest.getFuelTarget();
		
		if (type.equals("PC")) {
	
			if (processorResponse.getFuel() != null) {
				String[] fuel;
				if (target == null || target.equalsIgnoreCase("tractor")) {
				   // set limits based on FUEL
					fuel = processorResponse.getFuel().split(",");
				} else if (processorResponse.getReeferLimit() != null) {
					fuel = processorResponse.getReeferLimit().split(",");   
				} else {
					fuel = new String[] {"0","0"};
				}
				String litres = fuel[0];
				String dollars = fuel[1];
				BigDecimal maxQuantityLimit = ProtocolUtils.getBigDecimal(litres,3);
				BigDecimal maxDollarLimit = ProtocolUtils.getBigDecimal(dollars,2);
				
				fuelMatched = setFuelLimit(maxQuantityLimit, maxDollarLimit, posRequest.getSellingPrice(), posResponse, processorResponse);
				
				
			} else if (processorResponse.getFuelLimit() != null) {
				// set limits based on FLMT
				String[] flmt = processorResponse.getFuelLimit().split("/");
				// need to check this against fuel types
				FuelCode fc = posRequest.getFuelCode();
			
				if (fc != null) {
					for (int i=0; i < flmt.length; i++) {
						String[] fuel = flmt[i].split(",");
						int fuelType = Integer.parseInt(fuel[0]);
						if (fuelType == fc.getEfsCode()) {
							String litres = fuel[1];
							String dollars = fuel[2];
							BigDecimal maxQuantityLimit = ProtocolUtils.getBigDecimal(litres,3);
							BigDecimal maxDollarLimit = ProtocolUtils.getBigDecimal(dollars,2);
							fuelMatched = setFuelLimit(maxQuantityLimit, maxDollarLimit, posRequest.getSellingPrice(), posResponse, processorResponse);
							break;
						}
					}
				}
				
			} else {
				fuelMatched = false;
			}
			
			if (!fuelMatched) {
				processorResponse.setMessage("Fuel Not Authorized");
				processorResponse.setResponseCode("00050");
				processorResponse.setErrorCode("00050");
				posResponse.setDenied("00050", "Fuel Not Authorized");			
			} else {
				// this is a pre-auth approval
				posResponse.setAuthorized(processorResponse.getAuthorizationCode());
				posResponse.setMessage(processorResponse.getMessage());
				setPosPrompts(processorResponse, posResponse);
			}
			
    	} else if (type.equals("RC")) {
			// this is a post-auth approval
    		posResponse.setAuthorized(processorResponse.getAuthorizationCode());	
    		posResponse.setAmount(processorResponse.getTotal());
    		posResponse.setMessage(processorResponse.getMessage());
			
    	} else if (type.equals("XC")) {
    		// extended prompts
    		posResponse.setReauth(processorResponse.getMessage());
    		setPosPrompts(processorResponse, posResponse);
		} else {
			// this is a denial 
			String denialReason = ProtocolUtils.formatErrors(processorResponse.getErrorCode());
			posResponse.setDenied(denialReason, processorResponse.getMessage()); 
			
		}
    	
		
    	if (processorResponse.getPrint() != null) {
    		posResponse.setReceiptTrailer(processorResponse.getPrint());
    		posResponse.setReceiptTrailerFlag(1);
    	} else {
    		posResponse.setReceiptTrailerFlag(0);
    	}
    	
    	return posResponse;
	}
	
	@Override
	public void setPosPrompts(ProcessorAuthorization processorResponse, PosAuthorization posResponse) {
		posResponse.addPrompt("L1", formatPosPrompt(processorResponse.getDriversLicenseNumber()));
		posResponse.addPrompt("M2", formatPosPrompt(processorResponse.getUnitNumber()));
		posResponse.addPrompt("M3", formatPosPrompt(processorResponse.getVehiclePlateNumber()));
		posResponse.addPrompt("M6", formatPosPrompt(processorResponse.getPoNumber()));
		posResponse.addPrompt("TN", formatPosPrompt(processorResponse.getTrailerNumber()));
		posResponse.addPrompt("O1", formatPosPrompt(processorResponse.getOdometerReading() ));
		posResponse.addPrompt("P1", formatPosPrompt(processorResponse.getHubReading()));
		posResponse.addPrompt("P2", formatPosPrompt(processorResponse.getTrip()));
		posResponse.addPrompt("DI", formatPosPrompt(processorResponse.getDriverID()));
	}

	@Override
	public String formatPosPrompt(String processorValue) {
		if (processorValue != null) {
			String formattedValue = null;
			
			String minValue = null;
			String exactValue = null;
			String maxValue = null;
			
			boolean isDataVal = false;
			boolean isExact = false;
			boolean hasMin = false;
			boolean hasMax = false;
			
			String[] masks = processorValue.split(";");
			
			for (String mask:masks) {
					
				if (mask.equals("TN")) {
					isDataVal = true;
				}
				
				if (mask.startsWith("X")) {
					hasMax = true;
					maxValue = mask;
				}
				
				if (mask.startsWith("M")) {
					hasMin = true;
					minValue = mask;
				}
				
				if (mask.startsWith("V") || mask.startsWith("L")) {
					isExact = true;
					isDataVal = true;
					exactValue = mask.substring(1);
				}
			}
			
			if (isDataVal) {
				formattedValue = "V,";
			} else {
				formattedValue = "L,";
			}
			
			if (hasMin && hasMax) {
				formattedValue += minValue + ":" + maxValue;
			} else if (hasMin) {
				formattedValue += minValue;
			} else if (hasMax) {
				formattedValue += maxValue;
			} else if (isExact) {
				formattedValue += exactValue;
			}
			
			return formattedValue;
		} else {
			return null;
		}
	}


}
