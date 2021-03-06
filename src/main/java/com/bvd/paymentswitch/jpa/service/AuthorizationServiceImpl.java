package com.bvd.paymentswitch.jpa.service;

import com.bvd.paymentswitch.models.BinPaymentProcessor;
import com.bvd.paymentswitch.models.CompletedAuthorization;
import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.IncompleteAuthorization;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.MerchantCode;
import com.bvd.paymentswitch.models.MerchantCodePK;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.models.RejectedAuthorization;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthorizationServiceImpl implements AuthorizationService {
	
	static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
	static final Logger persistenceLogger = LoggerFactory.getLogger("Persistence-Logger");
	
	private final POSAuthorizationRepository posRepository;
	private final ProcessorAuthorizationRepository processorRepository;
	private final BinPaymentProcessorRepository binRepository;
	private final PaymentProcessorRepository paymentProcessorRepository;
	private final MerchantCodeRepository merchantCodeRepository;
	private final FuelCodeRepository fuelCodeRepository;
	private final CompletedAuthorizationRepository completedAuthRepository;
	private final RejectedAuthorizationRepository rejectedAuthRepository;
	private final IncompleteAuthorizationRepository incompleteAuthRepository;
	
	public AuthorizationServiceImpl(POSAuthorizationRepository posRepository, ProcessorAuthorizationRepository processorRepository,
									BinPaymentProcessorRepository binRepository,PaymentProcessorRepository paymentProcessorRepository,
									MerchantCodeRepository merchantCodeRepository, FuelCodeRepository fuelCodeRepository, 
									CompletedAuthorizationRepository completedAuthRepository, RejectedAuthorizationRepository rejectedAuthRepository,
									IncompleteAuthorizationRepository incompleteAuthRepository) {
		this.posRepository = posRepository;
		this.processorRepository = processorRepository;
		this.binRepository = binRepository;
		this.paymentProcessorRepository = paymentProcessorRepository;
		this.merchantCodeRepository = merchantCodeRepository;
		this.fuelCodeRepository = fuelCodeRepository;
		this.completedAuthRepository = completedAuthRepository;
		this.rejectedAuthRepository = rejectedAuthRepository;
		this.incompleteAuthRepository = incompleteAuthRepository;
	}
	
	

	@Override
	@Async("threadPoolTaskExecutor")
	public void saveAuthorizationTransaction(PosAuthorization request, ProcessorAuthorization response, ProcessingProvider provider) {
		Timestamp create_ts = ProtocolUtils.getUTCTimestamp();
		Long req_id = null;
		try {
			
			request.setCreateTimestamp(create_ts);
			request = posRepository.save(request);
			req_id = request.getId();
			response.setRequestId(req_id);
			response.setCreateTimestamp(create_ts);
			response.setCardNumber(request.getCard1());
			response.setCardToken(request.getTrack2Data());
			response.setUnitNumber(request.getUnitNumber());
			response.setDriverID(request.getDriverId());
			response.setInvoiceNumber(request.getTrnNo());
			
			String authCode = response.getAuthorizationCode();
			if ( authCode == null || authCode.length()==0 ) {
				response.setAuthorizationCode(request.getAuthId());
			}
			
			processorRepository.save(response);
		} catch (Exception e) {
			// write to a file if failed
			logger.error("Unable to persist transactions for Trn: " + response.getInvoiceNumber() + ", Error: " +  e.getMessage());
			persistenceLogger.info("TS:" + create_ts + ",ID:" + req_id + ",POS:" + request.toString() + ",PROC:" +  provider.formatProcessorRequest(response));
		}
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
	@Cacheable("merchants")
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
	public void saveFuelCodes(List<FuelCode> codes) {
		fuelCodeRepository.save(codes);
	}


//	@Override
//	public Iterable<PaymentProcessor> getAllPaymentProcessors() {
//		return paymentProcessorRepository.findAll();
//	}
	@Override
	@Cacheable("providers")
	public ProcessingProvider getProcessingProvider(String bin) {
	
		BinPaymentProcessor binpay = binRepository.findOne(bin);
		
		if (binpay != null) {
			PaymentProcessor p = binpay.getPaymentProcessor();
			try {
				logger.debug("Loading " + p.getName() + " provider for BIN: " + bin);
				Class<?> clazz = Class.forName(p.getProviderClass());
				ProcessingProvider provider = (ProcessingProvider) clazz.newInstance();
				provider.initialize(p, this);
				return provider;
			} catch (Exception e) {
				logger.error("Unable to initialize " + p.getName() + " provider for BIN: " + bin, e.getMessage());
				return null;
			}
		} else {
			return null;
		}
	}


	@Override
	public ProcessorAuthorization findProcessorAuthorization(String invoiceNumber, String cardNumber, String unitNumber, String type, String responseCode) {
		return processorRepository.findByInvoiceNumberAndCardNumberAndUnitNumberAndTypeAndResponseCode(invoiceNumber, cardNumber, unitNumber, type, responseCode);
	}


	@Override
	public List<CompletedAuthorization> getCompletedAuthorizations(Timestamp startTS, Timestamp endTS, String type) {
		
		if (startTS != null) {
			if (endTS != null) {
				return  (type.equalsIgnoreCase("transaction")?
						completedAuthRepository.findByTransactionDateTimeBetween(startTS, endTS):
						completedAuthRepository.findByProcessedDateTimeBetween(startTS, endTS));			
			} else {
				return  (type.equalsIgnoreCase("transaction")?
						completedAuthRepository.findByTransactionDateTimeAfter(startTS):
						completedAuthRepository.findByProcessedDateTimeAfter(startTS));
			} 
		} else {
			return (List<CompletedAuthorization>) completedAuthRepository.findAll();
		}
	}


	@Override
	public List<RejectedAuthorization> getRejectedAuthorizations(Timestamp startTS, Timestamp endTS) {
		if (startTS != null) {
			if (endTS != null) {
				return  rejectedAuthRepository.findByTransactionDateTimeBetween(startTS, endTS);			
			} else {
				return rejectedAuthRepository.findByTransactionDateTimeAfter(startTS);
			} 
		} else {
			return (List<RejectedAuthorization>) rejectedAuthRepository.findAll();
		}
	}


	@Override
	public String getFuelCodeForAuthorization(String authId, Timestamp ts) {
		try  {
			return posRepository.findByAuthIdAndDateTime(authId, ts);
		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public List<IncompleteAuthorization> getIncompleteAuthorizations(Timestamp startTS, Timestamp endTS) {
		if (startTS != null) {
			if (endTS != null) {
				return  incompleteAuthRepository.findByCreateTimestampBetween(startTS, endTS);		
			} else {
				return incompleteAuthRepository.findByCreateTimestampAfter(startTS);
			} 
		} else {
			return (List<IncompleteAuthorization>) incompleteAuthRepository.findAll();
		}
	}
	
	
	
	

}
