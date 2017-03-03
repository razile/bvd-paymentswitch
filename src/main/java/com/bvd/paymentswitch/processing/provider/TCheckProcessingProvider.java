package com.bvd.paymentswitch.processing.provider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.protocol.PrePostEncoder;
import com.bvd.paymentswitch.protocol.STX_ETX_Decoder;

public class TCheckProcessingProvider extends PriorPostAbstractProcessingProvider {

	@Override
	public void setProtocolCodecs() {
		encoder = new PrePostEncoder();
		decoder = new STX_ETX_Decoder();
	}

	@Override
	public String getCardKey() {
		return "CDSW";
	}

	@Override
	public String getCardValue(ProcessorAuthorization processorRequest) {
		// TODO Auto-generated method stub
		return processorRequest.getCardToken();
	}

	@Override
	public void setPreAuthFuelTokens(PosAuthorization posRequest, ProcessorAuthorization processorRequest) {
		
		BigDecimal sellingPrice = posRequest.getSellingPrice();
		int fuelType = posRequest.getFuelCode().getEfsCode(); 
		if (sellingPrice != null) {
			String fuelUse = "1";
			String target = posRequest.getFuelTarget();
			if (target != null && target.equalsIgnoreCase("trailer")) {
				if (fuelType != 4194304) {
					fuelUse = "2";	
				}
			}
			String fuelToken = "1.000," + sellingPrice + ",0.00," + fuelType +"," + fuelUse + ",1";
			processorRequest.setFuel(fuelToken);
			
		/*	if (fuelType == 4194304) {
				String def = this.getPaymentProcessor().getDefIndicator();
				if (def != null) {
					if (def.equalsIgnoreCase("DISP")) {
						BigDecimal maxVol = ProtocolUtils.getBigDecimal("100.000", 3);
						BigDecimal maxDoll = maxVol.multiply(sellingPrice);
						maxDoll = maxDoll.setScale(2, RoundingMode.HALF_UP);
						String dToken = "DEF," + sellingPrice + "," + maxVol + "," + maxDoll + ",L,I";
						processorRequest.setDispensed(dToken);				
					} else if (def.equalsIgnoreCase("MERC")) {
						String mToken = "DE:1,100.00";
						processorRequest.setMerchandise(mToken);
					}
				} 
			} */
		}
		
	}

	@Override
	public void setPostAuthFuelTokens(PosAuthorization posRequest, ProcessorAuthorization processorRequest) {
		
		int fuelType = posRequest.getFuelCode().getEfsCode(); 
		BigDecimal sellingPrice = posRequest.getSellingPrice();
		BigDecimal quantity = posRequest.getQuantityNet();
		BigDecimal amount = posRequest.getAmount();

		String fuelUse = "1";
		String target = posRequest.getFuelTarget();
		if (target != null && target.equalsIgnoreCase("trailer")) {
			if (fuelType != 4194304) {
				fuelUse = "2";	
			}
		}
		
		String fuelToken = quantity + "," + sellingPrice + "," + amount + "," + fuelType + "," + fuelUse + ",1";
		processorRequest.setTotal(amount);
		processorRequest.setFuel(fuelToken);
		
		/*if (fuelType == 4194304) {
			String def = this.getPaymentProcessor().getDefIndicator();
			if (def != null) {
				if (def.equalsIgnoreCase("DISP")) {
					String dToken = "DEF," + sellingPrice + "," + quantity + "," + amount + ",L,I";
					processorRequest.setDispensed(dToken);					
				} else if (def.equalsIgnoreCase("MERC")) {
					String mToken = "DE:1," + amount;
					processorRequest.setMerchandise(mToken);
				}
			}
		}*/
		
	}
	


	@Override
	public boolean setFuelLimit(BigDecimal maxVolume, BigDecimal maxAmount, BigDecimal sellingPrice,
			PosAuthorization posResponse, ProcessorAuthorization processorResponse) {
		boolean fuelMatched = false;
		BigDecimal maxLimit = null;
		
		if (maxVolume != null && maxVolume.floatValue() > 0) {
			maxLimit = (maxVolume.multiply(sellingPrice)).setScale(2, RoundingMode.FLOOR);
			fuelMatched=true;
		} else if (maxAmount != null &&  maxAmount.floatValue() > 0) {
			maxLimit = maxAmount;
			fuelMatched = true;
		} else {
			fuelMatched = false;
		}
		
		
		if (fuelMatched) {
			posResponse.setDollarLimit(maxLimit);
		}
		
		return fuelMatched;
	}
	
	@Override
	public boolean validatePOSRequest(PosAuthorization posRequest) {
		if (posRequest.getUnitNumber() == null || posRequest.getUnitNumber().trim().length() == 0
				|| posRequest.getDriverId() == null || posRequest.getDriverId().trim().length() == 0) {
			return false;
		} 
		
		if (posRequest.getFuelTarget() != null && posRequest.getFuelTarget().equalsIgnoreCase("trailer")) {
			if (posRequest.getTrailerNumber() == null || posRequest.getTrailerNumber().trim().length() == 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void setRequiredPrompts(PosAuthorization posRequest, PosAuthorization posResponse) {
		posResponse.addPrompt("M2", "L,X16");
		posResponse.addPrompt("DI", "L,X16");
		
		if (posRequest.getFuelTarget() != null && posRequest.getFuelTarget().equalsIgnoreCase("trailer")) {
			posResponse.addPrompt("TN", "L,X32");
			
		}
		
	}

}
