package com.bvd.paymentswitch.jpa.service;

import com.bvd.paymentswitch.models.BinPaymentProcessor;
import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.MerchantCode;
import com.bvd.paymentswitch.models.MerchantCodePK;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import java.util.List;
import java.util.concurrent.Future;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthorizationServiceImpl implements AuthorizationService {
	
	static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
	static final Logger requestLogger = LoggerFactory.getLogger("Request-Persistence-Logger");
	static final Logger responseLogger = LoggerFactory.getLogger("Response-Persistence-Logger");
	
	private final POSAuthorizationRepository kardallRepository;
	private final ProcessorAuthorizationRepository processorRepository;
	private final BinPaymentProcessorRepository binRepository;
	private final PaymentProcessorRepository paymentProcessorRepository;
	private final MerchantCodeRepository merchantCodeRepository;
	private final FuelCodeRepository fuelCodeRepository;
	
	public AuthorizationServiceImpl(POSAuthorizationRepository kardallRepository, ProcessorAuthorizationRepository processorRepository,
									BinPaymentProcessorRepository binRepository,PaymentProcessorRepository paymentProcessorRepository,
									MerchantCodeRepository merchantCodeRepository, FuelCodeRepository fuelCodeRepository) {
		this.kardallRepository = kardallRepository;
		this.processorRepository = processorRepository;
		this.binRepository = binRepository;
		this.paymentProcessorRepository = paymentProcessorRepository;
		this.merchantCodeRepository = merchantCodeRepository;
		this.fuelCodeRepository = fuelCodeRepository;
	}
	
	
	@Override
	@Async("threadPoolTaskExecutor")
	public Future<PosAuthorization> saveAuthorization(PosAuthorization k) {
		
		try {
			k.setCreateTimestamp(ProtocolUtils.getUTCTimestamp());
			k = kardallRepository.save(k);
		} catch (Exception e) {
			// write to a file if failed
			logger.error("Unable to persist KardallHostAuthorization for Trn: " + k.getTrnNo() + ", Error: " +  e.getMessage());
			requestLogger.info(k.toString());
		}
		return new AsyncResult<>(k);
	}

	@Override
	@Async("threadPoolTaskExecutor")
	public Future<ProcessorAuthorization> saveAuthorization(ProcessorAuthorization p, ProcessingProvider provider) {
		
		try {
			p.setCreateTimestamp(ProtocolUtils.getUTCTimestamp());
			p = processorRepository.save(p);
		} catch (Exception e) {
			// write to a file if failed
			logger.error("Unable to persist PriorPostAuthorization for Trn: " + p.getInvoiceNumber() + ", Error: " +  e.getMessage());
			responseLogger.info(provider.formatProcessorRequest(p));
		}
		return new AsyncResult<>(p);
	}


	@Override
	public PaymentProcessor savePaymentProcessor(PaymentProcessor p) {
		p = paymentProcessorRepository.save(p);
		return p;
	}


	@Override
	public BinPaymentProcessor saveBin(BinPaymentProcessor b) {
		b = binRepository.save(b);
		return b;
	}
	
	@Override
	public MerchantCode saveMerchantCode(String siteId, Short paymentProcessorId, String merchantId) {
	
		MerchantCode mc = new MerchantCode();
		mc.setMerchantId(merchantId);
		mc.setPaymentProcessorId(paymentProcessorId);
		mc.setSiteId(siteId);
		
		mc = merchantCodeRepository.save(mc);
		
		return mc;
	}


	@Override
	@Cacheable("merchantIds")
	public String findMerchantID(String siteId, Short paymentProcessorId) {
		// TODO Auto-generated method stub
		MerchantCodePK pk = new MerchantCodePK(siteId, paymentProcessorId);
		MerchantCode mc = merchantCodeRepository.findOne(pk);
		
		if (mc == null) return null;
		
		return mc.getMerchantId();
	}


	@Override
	@Cacheable("fuelcodes")
	public FuelCode findFuelCode(String code) {
		FuelCode fc = fuelCodeRepository.findOne(code);
		return fc;
	}


	@Override
	@Cacheable("fuelcodes")
	public FuelCode findEFSFuelCode(int code) {
		FuelCode fc = fuelCodeRepository.findByEfsCode(code);
		return fc;
	}


	@Override
	@Cacheable("fuelcodes")
	public FuelCode findComdataFuelCode(String code) {
		FuelCode fc = fuelCodeRepository.findByComdataCode(code);
		return fc;
	}


	@Override
	public void saveFuelCodes(List<FuelCode> codes) {
		fuelCodeRepository.save(codes);
	}


	@Override
	public Iterable<PaymentProcessor> getAllPaymentProcessors() {
		return paymentProcessorRepository.findAll();
	}


	@Override
	public ProcessorAuthorization findProcessorAuthorization(String type, String invoiceNumber, String cardNumber, String responseCode) {
		return processorRepository.findByTypeAndInvoiceNumberAndCardNumberAndResponseCode(type, invoiceNumber, cardNumber, responseCode);
	}
	
	

}
