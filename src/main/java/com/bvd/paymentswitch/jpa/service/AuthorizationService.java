package com.bvd.paymentswitch.jpa.service;

import java.sql.Timestamp;
import java.util.List;

import com.bvd.paymentswitch.models.BinPaymentProcessor;
import com.bvd.paymentswitch.models.CompletedAuthorization;
import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.IncompleteAuthorization;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.MerchantCode;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.models.RejectedAuthorization;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;

public interface AuthorizationService {
	
	
	PaymentProcessor savePaymentProcessor(PaymentProcessor p);
	
	BinPaymentProcessor saveBin(BinPaymentProcessor b);
	
	// Iterable<PaymentProcessor> getAllPaymentProcessors();
	
	ProcessingProvider getProcessingProvider(String bin);
	
	String findMerchantID(String siteId, Short paymentProcessorId);
	
	MerchantCode saveMerchantCode(String siteId, Short paymentProcessorId, String merchantID);
	
	FuelCode findFuelCode(String code);
	
	void saveFuelCodes(List<FuelCode> codes);
	
	ProcessorAuthorization findProcessorAuthorization(String invoiceNumber, String cardNumber, String unitNumber, String type, String responseCode);

	List<CompletedAuthorization> getCompletedAuthorizations(Timestamp startTS, Timestamp endTS, String type);
	
	List<RejectedAuthorization> getRejectedAuthorizations(Timestamp startTS, Timestamp endTS);
	
	String getFuelCodeForAuthorization(String authId, Timestamp ts);
	
	List<IncompleteAuthorization> getIncompleteAuthorizations(Timestamp startTS, Timestamp endTS);

	void saveAuthorizationTransaction(PosAuthorization request, ProcessorAuthorization response,
			ProcessingProvider provider);
}
