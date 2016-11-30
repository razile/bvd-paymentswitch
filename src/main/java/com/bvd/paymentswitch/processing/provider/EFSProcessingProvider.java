package com.bvd.paymentswitch.processing.provider;

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

}
