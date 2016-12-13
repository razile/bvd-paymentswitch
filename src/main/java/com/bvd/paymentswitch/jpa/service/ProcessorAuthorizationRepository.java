package com.bvd.paymentswitch.jpa.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bvd.paymentswitch.models.ProcessorAuthorization;;

public interface ProcessorAuthorizationRepository extends CrudRepository<ProcessorAuthorization, Long> {
	
	List<ProcessorAuthorization> findByLocation(String siteId);
	
	List<ProcessorAuthorization> findByAuthorizationCode(String authId);
	
	ProcessorAuthorization findByTypeAndInvoiceNumberAndCardNumberAndResponseCode(String type, String invoiceNumber, String CardNumber, String responseCode);
}
