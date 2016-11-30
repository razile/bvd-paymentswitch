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
		String body = response.substring(17);
		
		processorResponse.setLocation(header.substring(0,5));
		processorResponse.setVersionNumber(header.substring(5,6));
		processorResponse.setSoftwareSystem(header.substring(6,10));
		
		String report = header.substring(10,17);
		processorResponse.setType(report);
		
		String[] fields = null;
		if (report.equalsIgnoreCase("SP00014")) {
			fields = body.split(String.valueOf(ASCIIChars.ASC47));
		} else {
			fields = body.split(String.valueOf(ASCIIChars.ASC28));
		}
		
		processorResponse.setResponseCode(fields[0]);
		processorResponse.setMessage(fields[1]);
		return null;
	}

	@Override
	public String formatProcessorRequest(ProcessorAuthorization processorRequest) {
		
		String report = processorRequest.getType();
		
		char fs = ASCIIChars.ASC28;
		if (report.equalsIgnoreCase("SP00014")) {
			fs = ASCIIChars.ASC47;
		}
		String msg = processorRequest.getLocation() + " " + paymentProcessor.getSoftwareSystem() + report 
				+ fs + "00036" + fs + "A" + processorRequest.getCardNumber() + fs + processorRequest.getUnitNumber();
		
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
