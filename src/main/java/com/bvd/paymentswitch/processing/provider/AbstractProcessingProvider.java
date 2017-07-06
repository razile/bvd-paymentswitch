package com.bvd.paymentswitch.processing.provider;

import java.math.BigDecimal;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;


public abstract class AbstractProcessingProvider implements ProcessingProvider {

	protected PaymentProcessor paymentProcessor;
	protected ChannelOutboundHandler encoder; 
	protected ChannelInboundHandler decoder;
	protected AuthorizationService authService;
	
	@Override
	public void initialize(PaymentProcessor p, AuthorizationService authService) {
		this.paymentProcessor = p;
		this.authService = authService;
		this.setProtocolCodecs();
	}

	@Override
	public PaymentProcessor getPaymentProcessor() throws NullPointerException {
		if (paymentProcessor == null) throw new NullPointerException(this.getClass().getName() + " is not initialized");
		return paymentProcessor;
	}

	@Override
	public ChannelOutboundHandler getProtocolEncoder() throws NullPointerException {
		if (encoder == null) throw new NullPointerException(this.getClass().getName() + " is not initialized");
		return encoder;
	}

	@Override
	public ChannelInboundHandler getProtocolDecoder() throws NullPointerException {
		if (decoder == null) throw new NullPointerException(this.getClass().getName() + " is not initialized");
		return decoder;
	}
	
	@Override 
	public void saveAuthorization(PosAuthorization request, ProcessorAuthorization response) {
		authService.saveAuthorizationTransaction(request, response, this);
	}
	
	

	@Override 
	public boolean validateCompletionAmount(PosAuthorization posRequest) {
		if (posRequest.getTransactionType() == 1) {
			BigDecimal sellingPrice = posRequest.getSellingPrice();
			BigDecimal quantity = posRequest.getQuantityNet();
			BigDecimal amount = posRequest.getAmount();
			
			if (sellingPrice == null || quantity == null || amount == null) return false;
			
			
			if (amount.compareTo(BigDecimal.ZERO) <= 0 || quantity.compareTo(BigDecimal.ZERO) <= 0|| sellingPrice.compareTo(BigDecimal.ZERO) <= 0) return false;
			
		}
		
		return true;
	}


}
