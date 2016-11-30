package com.bvd.paymentswitch.processing.provider;

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

}
