package com.bvd.paymentswitch.processing.handler;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;

@Component
@Scope("prototype")
public class ComdataAuthorizationHandler extends AuthorizationHandler {

	
	@Override
	public void handleResponse(ProcessorAuthorization processorResponse) {
		
		PosAuthorization posResponse = new PosAuthorization(posRequest);
		posResponse.setResponseFlags(posRequest);
    	
    	String type = processorResponse.getType();
    	String responseCode = processorResponse.getResponseCode();
		
		if (type.equals("SP00007")) {
			// this was a pre-edit request
			if (responseCode.equals("00000")) {
				posResponse.setReauthForComdata("SP00014");
				setPosPrompts(processorResponse, posResponse);
			} else {
				posResponse.setDenied(responseCode, processorResponse.getMessage());
			}
			
    	} else if (type.equals("SP00014")) {
    		if (responseCode.equals("00000")) {
				posResponse.setAuthorized(processorResponse.getInvoiceNumber());
				posResponse.setDollarLimit(processorResponse.getTotal());
			} else {
				posResponse.setDenied(responseCode, processorResponse.getMessage());
			}
			
    	} else if (type.equals("SP00011")) {
    		if (responseCode.equals("00000")) {
				posResponse.setAuthorized(processorResponse.getAuthorizationCode());
				posResponse.setAmount(processorResponse.getTotal());
				posResponse.setMessage(processorResponse.getMessage());
			} else {
				posResponse.setDenied(responseCode, processorResponse.getMessage());
			}
    		
		} else {
			// this is a failed transaction
			posResponse.setDenied(responseCode, processorResponse.getMessage()); 
		}
    	
		
    	if (processorResponse.getPrint() != null) {
    		posResponse.setReceiptTrailer(processorResponse.getPrint());
    		posResponse.setReceiptTrailerFlag(1);
    	} else {
    		posResponse.setReceiptTrailerFlag(0);
    	}
    	
    	String bvdResp = posResponse.toString();
    	logger.debug("SEND: " + bvdResp);
    	posCtx.write(bvdResp);

 		// close the channel once the content is fully written
    	posCtx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    	
	}
	
	

	public void setPosPrompts(ProcessorAuthorization processorResponse, PosAuthorization posResponse) {
		posResponse.addPrompt("L1", formatPosPrompt(processorResponse.getDriversLicenseNumber()));
		posResponse.addPrompt("M3", formatPosPrompt(processorResponse.getVehiclePlateNumber()));
		posResponse.addPrompt("M6", formatPosPrompt(processorResponse.getPoNumber()));
		posResponse.addPrompt("TN", formatPosPrompt(processorResponse.getTrailerNumber()));
		posResponse.addPrompt("O1", formatPosPrompt(processorResponse.getOdometerReading() ));
		posResponse.addPrompt("P1", formatPosPrompt(processorResponse.getHubReading()));
		posResponse.addPrompt("P2", formatPosPrompt(processorResponse.getTrip()));
		posResponse.addPrompt("DI", formatPosPrompt(processorResponse.getDriverID()));
		posResponse.addPrompt("P3", formatPosPrompt(processorResponse.getDriversLicenseState()));
		posResponse.addPrompt("P4", formatPosPrompt(processorResponse.getTrailerHubReading()));
		posResponse.addPrompt("P5", formatPosPrompt(processorResponse.getTrailerHours()));
	}

	
	

	@Override
	public String formatPosPrompt(String processorValue) {
		
		return processorValue;
	}
}
