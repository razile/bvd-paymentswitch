package com.bvd.paymentswitch.jpa.service;

import com.bvd.paymentswitch.models.BinRange;
import com.bvd.paymentswitch.models.KardallHostAuthorization;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.PriorPostAuthorization;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("authorizationService")
@Transactional
public class AuthorizationServiceImpl implements AuthorizationService {

	private final KardallRepository kardallRepository;
	private final PrioPostRepository processorRepository;
	private final BinRangeRepository binRangeRepository;
	private final PaymentProcessorRepository paymentProcessorRepository;
	
	public AuthorizationServiceImpl(KardallRepository kardallRepository, PrioPostRepository processorRepository,
									BinRangeRepository binRangeRepository,PaymentProcessorRepository paymentProcessorRepository) {
		this.kardallRepository = kardallRepository;
		this.processorRepository = processorRepository;
		this.binRangeRepository = binRangeRepository;
		this.paymentProcessorRepository = paymentProcessorRepository;
	}
	
	
	@Override
	public KardallHostAuthorization saveAuthorization(KardallHostAuthorization k) {
		
		k.setCreateTimestamp(ProtocolUtils.getUTCTimestamp());
		k = kardallRepository.save(k);
		return k;
	}

	@Override
	public PriorPostAuthorization saveAuthorization(PriorPostAuthorization p) {
		p.setCreateTimestamp(ProtocolUtils.getUTCTimestamp());
		p = processorRepository.save(p);
		return p;
	}


	@Override
	public PaymentProcessor savePaymentProcessor(PaymentProcessor p) {
		p = paymentProcessorRepository.save(p);
		return p;
	}


	@Override
	public PaymentProcessor findPaymentProcessor(int bin) {
		// TODO Auto-generated method stub
		BinRange r = binRangeRepository.findByBin(bin);
		return r.getPaymentProcessor();
	}


	@Override
	public BinRange saveBinRange(BinRange b) {
		b = binRangeRepository.save(b);
		return b;
	}

}
