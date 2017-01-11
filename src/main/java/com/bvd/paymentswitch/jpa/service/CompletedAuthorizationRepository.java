package com.bvd.paymentswitch.jpa.service;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bvd.paymentswitch.models.CompletedAuthorization;

public interface CompletedAuthorizationRepository extends CrudRepository<CompletedAuthorization, String> {
	
	List<CompletedAuthorization> findByTransactionDateTimeBetween(Timestamp startTS, Timestamp endTS);
	
	List<CompletedAuthorization> findByProcessedDateTimeBetween(Timestamp startTS, Timestamp endTS);
	
	List<CompletedAuthorization> findByTransactionDateTimeAfter(Timestamp startTS);
	
	List<CompletedAuthorization> findByProcessedDateTimeAfter(Timestamp startTS);
	
}
