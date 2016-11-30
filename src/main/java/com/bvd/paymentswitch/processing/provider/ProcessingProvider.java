package com.bvd.paymentswitch.processing.provider;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.processing.handler.AuthorizationHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;


public interface ProcessingProvider {
	
	void initialize(PaymentProcessor p, AuthorizationService authServ);
	
	void setProtocolCodecs();

	PaymentProcessor getPaymentProcessor() throws NullPointerException;
	
	ChannelOutboundHandler getProtocolEncoder() throws NullPointerException;
	
	ChannelInboundHandler getProtocolDecoder() throws NullPointerException;
	
	ByteBuf getFrameDelimiter();
	
	AuthorizationHandler getAuthorizationHandler(PosAuthorization posRequest, ChannelHandlerContext posCtx);
	
	ProcessorAuthorization parseProcessorResponse(String processorResponse);
	
	String formatProcessorRequest(ProcessorAuthorization processorRequest);
	
	ProcessorAuthorization createProcessorRequest(PosAuthorization posRequest, String merchantCode);
	
	
	
	
	void saveProcessorResponse(ProcessorAuthorization processorResponse);
	
	
}
