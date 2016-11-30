package com.bvd.paymentswitch.processing.handler;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.utils.ProtocolUtils;

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
		
		if (type.equals("PC")) {
			// this is a pre-auth approval
			posResponse.setAuthorized(processorResponse.getAuthorizationCode());
			
			if (processorResponse.getFuel() != null) {
				// set limits based on FUEL
				String[] fuel = processorResponse.getFuel().split(",");
				String litres = fuel[0];
				String dollars = fuel[1];
				if (litres.equals("0")) {
					posResponse.setDollarLimit(ProtocolUtils.getBigDecimal(dollars,2));
				} else {
					posResponse.setVolumeLimit(ProtocolUtils.getBigDecimal(litres,3));
				}
				
			} else if (processorResponse.getFuelLimit() != null) {
				// set limits based on FLMT
				String[] flmt = processorResponse.getFuelLimit().split("/");
				// need to check this against fuel types
				FuelCode fc = posRequest.getFuelCode();
				boolean fuelMatched = false;
				if (fc != null) {
					for (int i=0; i < flmt.length; i++) {
						String[] fuel = flmt[i].split(",");
						int fuelType = Integer.parseInt(fuel[0]);
						if (fuelType == fc.getEfsCode()) {
							String litres = fuel[1];
							String dollars = fuel[2];
							if (litres.equals("0")) {
								posResponse.setDollarLimit(ProtocolUtils.getBigDecimal(dollars,2));
							} else {
								posResponse.setVolumeLimit(ProtocolUtils.getBigDecimal(litres,3));
							}
							fuelMatched=true;
							break;
						}
					}
				}
				
				if (!fuelMatched) {
					posResponse.setDollarLimit(processorResponse.getTotal());
				}

			} else {
				posResponse.setDollarLimit(processorResponse.getTotal());
			}
			setPosPrompts(processorResponse, posResponse);
			
    	} else if (type.equals("RC")) {
			// this is a post-auth approval
    		posResponse.setAuthorized(processorResponse.getAuthorizationCode());	
    		posResponse.setAmount(processorResponse.getTotal());
			
		} else {
			// this is a denial or extended prompts (XC)
			String denialReason = ProtocolUtils.formatErrors(processorResponse.getErrorCode());
			posResponse.setDenied(denialReason, processorResponse.getMessage()); 
			setPosPrompts(processorResponse, posResponse);
		}
    	
		
    	if (processorResponse.getPrint() != null) {
    		posResponse.setReceiptTrailer(processorResponse.getPrint());
    		posResponse.setReceiptTrailerFlag(1);
    	} else {
    		posResponse.setReceiptTrailerFlag(0);
    	}
    	
    	posResponse.setMessage(processorResponse.getMessage());
    	String bvdResp = posResponse.toString();
    	logger.debug("SEND: " + bvdResp);
    	posCtx.write(bvdResp);

 		// close the channel once the content is fully written
    	posCtx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    	
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
				
				if (mask.startsWith("V")) {
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
