package com.bvd.paymentswitch.jpa.service;

import java.util.List;
import java.util.concurrent.Future;

import com.bvd.paymentswitch.models.BinPaymentProcessor;
import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.MerchantCode;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;

public interface AuthorizationService {
	
	Future<PosAuthorization> saveAuthorization(PosAuthorization k);

	Future<ProcessorAuthorization> saveAuthorization(ProcessorAuthorization p, ProcessingProvider provider);
	
	PaymentProcessor savePaymentProcessor(PaymentProcessor p);
	
	BinPaymentProcessor saveBin(BinPaymentProcessor b);
	
	Iterable<PaymentProcessor> getAllPaymentProcessors();
	
	String findMerchantID(String siteId, Short paymentProcessorId);
	
	MerchantCode saveMerchantCode(String siteId, Short paymentProcessorId, String merchantID);
	
	FuelCode findFuelCode(String code);
	
	FuelCode findEFSFuelCode(int code);

	FuelCode findComdataFuelCode(String code);
	
	void saveFuelCodes(List<FuelCode> codes);
}
