package com.bvd.paymentswitch.processing.provider;

import java.math.BigDecimal;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.protocol.EFSEncoder;
import com.bvd.paymentswitch.protocol.STX_ETX_Decoder;

public class EFSProcessingProvider extends PriorPostAbstractProcessingProvider {

	@Override
	public void setProtocolCodecs() {
		encoder = new EFSEncoder();
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
			String fuelToken = "1.000," + sellingPrice + ",0.00," + fuelType +",1,1";
			processorRequest.setFuel(fuelToken);
		}

	}

	@Override
	public void setPostAuthFuelTokens(PosAuthorization posRequest, ProcessorAuthorization processorRequest) {
		
		int fuelType = posRequest.getFuelCode().getEfsCode(); 
		BigDecimal sellingPrice = posRequest.getSellingPrice();
		BigDecimal quantity = posRequest.getQuantityNet();
		BigDecimal amount = posRequest.getAmount();

		String fuelToken = quantity + "," + sellingPrice + "," + amount + "," + fuelType + ",1,1";
		processorRequest.setTotal(amount);
		processorRequest.setFuel(fuelToken);
	}

}
