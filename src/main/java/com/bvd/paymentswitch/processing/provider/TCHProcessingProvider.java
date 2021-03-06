package com.bvd.paymentswitch.processing.provider;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.protocol.STX_ETX_Decoder;
import com.bvd.paymentswitch.protocol.TCHEncoder;

public class TCHProcessingProvider extends PriorPostAbstractProcessingProvider {

	@Override
	public void setProtocolCodecs() {
		encoder = new TCHEncoder();
		decoder = new STX_ETX_Decoder();
	}

	@Override
	public String getCardKey() {
		return "CARD";
	}

	@Override
	public String getCardValue(ProcessorAuthorization processorRequest) {
		// TODO Auto-generated method stub
		return processorRequest.getCardNumber();
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
		processorRequest.setSellingPrice(sellingPrice);
	}


	@Override
	public boolean setFuelLimit(BigDecimal maxVolume, BigDecimal maxAmount, BigDecimal sellingPrice, PosAuthorization posResponse,ProcessorAuthorization processorResponse) {
		boolean fuelMatched = false;
		BigDecimal maxByNCDV = processorResponse.getNonCADVTotal();
		BigDecimal comparator = null;
		BigDecimal maxLimit = null;
		
		if (maxVolume != null && maxVolume.floatValue() > 0) {
			comparator = (maxVolume.multiply(sellingPrice)).setScale(2, RoundingMode.FLOOR);
			fuelMatched=true;
		} else if (maxAmount != null &&  maxAmount.floatValue() > 0) {
			comparator = maxAmount;
			fuelMatched = true;
		} else {
			fuelMatched = false;
		}
		
		
		if (fuelMatched) {
			if (maxByNCDV != null) {
				maxLimit = (comparator.compareTo(maxByNCDV) < 0) ? comparator:maxByNCDV;
			} else {
				maxLimit = comparator;
			}
			posResponse.setDollarLimit(maxLimit);
		}
		
		return fuelMatched;
		
	}
	
	@Override
	public boolean validatePOSRequest(PosAuthorization posRequest) {
//		if (posRequest.getUnitNumber() == null || posRequest.getUnitNumber().trim().length() == 0
//				|| posRequest.getDriverId() == null || posRequest.getDriverId().trim().length() == 0) {
//			return false;
//		} else { 
//			return true;
//		}
		return true;
	}

	@Override
	public void setRequiredPrompts(PosAuthorization posRequest, PosAuthorization posResponse) {
		posResponse.addPrompt("M2", "L,X16");
		posResponse.addPrompt("DI", "L,X16");
		
	}

}
