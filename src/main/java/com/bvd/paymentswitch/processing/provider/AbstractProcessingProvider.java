package com.bvd.paymentswitch.processing.provider;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.PaymentProcessor;
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
	public void saveProcessorAuthorization(ProcessorAuthorization auth) {
		authService.saveAuthorization(auth, this);
	}


}
