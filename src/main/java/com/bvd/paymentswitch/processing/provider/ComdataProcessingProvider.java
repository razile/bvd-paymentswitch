package com.bvd.paymentswitch.processing.provider;

import java.math.BigDecimal;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.processing.handler.AuthorizationHandler;
import com.bvd.paymentswitch.processing.handler.ComdataAuthorizationHandler;
import com.bvd.paymentswitch.protocol.ComdataDecoder;
import com.bvd.paymentswitch.protocol.ComdataEncoder;
import com.bvd.paymentswitch.utils.ASCIIChars;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ComdataProcessingProvider extends AbstractProcessingProvider {


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
	public ProcessorAuthorization parseProcessorResponse(String response) {
		ProcessorAuthorization processorResponse = new ProcessorAuthorization();
		String header = response.substring(0,17);
		String body = response.substring(18);
		
		processorResponse.setLocation(header.substring(0,5));
		processorResponse.setVersionNumber(header.substring(5,6));
		processorResponse.setSoftwareSystem(header.substring(6,10));
		
		String report = header.substring(10,17);
		processorResponse.setType(report);
		
		if (report.equalsIgnoreCase("SP00007")) {
			parseSP7(processorResponse, body);
		} else if (report.equalsIgnoreCase("SP00011")) {
			parseSP11(processorResponse, body);
		} else {
			parseSP14(processorResponse, body);
		}
		
		return processorResponse;
	}

	private void parseSP14(ProcessorAuthorization processorResponse, String body) {
		// TODO Auto-generated method stub
		
	}

	private void parseSP11(ProcessorAuthorization processorResponse, String body) {
		// TODO Auto-generated method stub
		
	}

	private void parseSP7(ProcessorAuthorization processorResponse, String body) {
		
		String[] fields = body.split(String.valueOf(ASCIIChars.ASC47));
		String responseCode = fields[0];
		processorResponse.setResponseCode(responseCode);
		
		String reply = fields[1];
		
		if (responseCode.equals("00000")) {
			// good response
			processorResponse.setDriverID(reply.substring(0, 17));
			processorResponse.setTrailerNumber(reply.substring(17,28));
			processorResponse.setHubReading(reply.substring(28,41));
			//String trailerHub = reply.substring(41,42);
			//String trailerHours = reply.substring(42,43);
			
			processorResponse.setTrip(reply.substring(43,56));
			processorResponse.setDriversLicenseNumber(reply.substring(56,77));
			//String dlState = reply.substring(77,79);
			processorResponse.setPoNumber(reply.substring(79,90));
			
			//String lastName = reply.substring(90,110);
			//String firstName = reply.substring(110,125);
			
		} else {
			// failed response
			processorResponse.setMessage(reply);
			processorResponse.setErrorCode(responseCode);
		}
		
	}

	@Override
	public String formatProcessorRequest(ProcessorAuthorization processorRequest) {
		
		String report = processorRequest.getType();
		
		char fs = ASCIIChars.ASC47;
	
		String msg = processorRequest.getLocation() + "T" + paymentProcessor.getSoftwareSystem() + report 
				+ fs + "00036" + fs + "A" + processorRequest.getCardToken() + fs + processorRequest.getUnitNumber();
		
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

		BigDecimal sellingPrice = posRequest.getSellingPrice();
		int fuelType = posRequest.getFuelCode().getEfsCode(); 
		// specific tokens by lifecycle
		if (transType == 0) {
			// this is a prior
			processorRequest.setType("SP00007");
			processorRequest.setCustomerInformation("I");
			String fuelToken = "1.000," + sellingPrice + ",0.00," + fuelType +",1,1";
			//processorRequest.setFuel(fuelToken);
		} else {
			// this is a post (completed transaction)
			processorRequest.setType("SP00011");
			processorRequest.setAuthorizationCode(posRequest.getAuthId().trim());
			processorRequest.setCustomerInformation("I");
			
			BigDecimal quantity = posRequest.getQuantityNet();
			BigDecimal amount = posRequest.getAmount();

			String fuelToken = quantity + "," + sellingPrice + "," + amount + "," + fuelType + ",1,1";
			processorRequest.setTotal(amount);
			processorRequest.setFuel(fuelToken);
		}

		return processorRequest;
	}

}
