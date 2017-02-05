package com.bvd.paymentswitch.processing.provider;

import com.bvd.paymentswitch.models.PosAuthorization;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.processing.handler.AuthorizationHandler;
import com.bvd.paymentswitch.processing.handler.PriorPostAuthorizationHandler;
import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
public abstract class PriorPostAbstractProcessingProvider extends AbstractProcessingProvider {
	
	public abstract String getCardKey();
	public abstract String getCardValue(ProcessorAuthorization processorRequest);
	
	public abstract void setPreAuthFuelTokens(PosAuthorization posRequest, ProcessorAuthorization processorRequest);
	public abstract void setPostAuthFuelTokens(PosAuthorization posRequest, ProcessorAuthorization processorRequest);

	
	
	
	@Override 
	public ByteBuf getFrameDelimiter() {
		return ASCIIChars.ETX_DELIMITER;
	}
	
	@Override
	public AuthorizationHandler getAuthorizationHandler(PosAuthorization posRequest, ChannelHandlerContext posCtx) {
		// TODO Auto-generated method stub
		AuthorizationHandler handler = new PriorPostAuthorizationHandler();
		handler.initializePOSContext(posRequest, this, posCtx);
		return handler;
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


}
