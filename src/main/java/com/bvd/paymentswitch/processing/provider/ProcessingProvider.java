package com.bvd.paymentswitch.processing.provider;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;


public interface ProcessingProvider {
	
	void initialize(PaymentProcessor p, AuthorizationService authServ);
	
	void setProtocolCodecs();
	
	PaymentProcessor getPaymentProcessor() throws NullPointerException;
	
	ChannelOutboundHandler getProtocolEncoder() throws NullPointerException;
	
	ChannelInboundHandler getProtocolDecoder() throws NullPointerException;
	
	ByteBuf getFrameDelimiter();
	
	ProcessorAuthorization parseProcessorResponse(PosAuthorization posRequest, String processorResponse);
	
	PosAuthorization createPosResponse(PosAuthorization posRequest, ProcessorAuthorization processorResponse);
	
	String formatProcessorRequest(ProcessorAuthorization processorRequest);
	
	ProcessorAuthorization createProcessorRequest(PosAuthorization posRequest, String merchantCode);

	void saveProcessorAuthorization(ProcessorAuthorization auth);

	void setPosPrompts(ProcessorAuthorization processorResponse, PosAuthorization posResponse);
	
	String formatPosPrompt(String processorValue);
	
	boolean validatePOSRequest(PosAuthorization posRequest);
	
	void setRequiredPrompts(PosAuthorization posRequest, PosAuthorization posResponse);
	
	
}
