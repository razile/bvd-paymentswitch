package com.bvd.paymentswitch.jpa.service;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bvd.paymentswitch.models.RejectedAuthorization;

public interface RejectedAuthorizationRepository extends CrudRepository<RejectedAuthorization, String> {
	
	List<RejectedAuthorization> findByTransactionDateTimeBetween(Timestamp startTS, Timestamp endTS);
	
	List<RejectedAuthorization> findByTransactionDateTimeAfter(Timestamp startTS);
	
	
}
