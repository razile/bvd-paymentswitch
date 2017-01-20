package com.bvd.paymentswitch.processing.handler;

import java.math.BigDecimal;

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
public class PriorPostAuthorizationHandler extends AuthorizationHandler {

	
	@Override
	public void handleResponse(ProcessorAuthorization processorResponse) {
		
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
				BigDecimal litVal = ProtocolUtils.getBigDecimal(litres,3);
				BigDecimal dolVal = ProtocolUtils.getBigDecimal(dollars,2);
				
				if ( (litVal != null && litVal.floatValue() > 0) || (dolVal != null && dolVal.floatValue() > 0) ) {
					fuelMatched = true;
					if (litres.equals("0")) {
						posResponse.setDollarLimit(dolVal);
					} else {
						posResponse.setVolumeLimit(litVal);
					}
				} 
				
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
    	
    	
    	String bvdResp = posResponse.toString();
    	logger.debug("SEND: " + bvdResp);
    	posCtx.write(bvdResp);

 		// close the channel once the content is fully written
    	posCtx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    	
	}


	
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
