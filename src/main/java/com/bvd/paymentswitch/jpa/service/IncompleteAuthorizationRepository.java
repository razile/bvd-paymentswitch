package com.bvd.paymentswitch.jpa.service;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bvd.paymentswitch.models.IncompleteAuthorization;

public interface IncompleteAuthorizationRepository extends CrudRepository<IncompleteAuthorization, String> {
	
	List<IncompleteAuthorization> findByCreateTimestampBetween(Timestamp startTS, Timestamp endTS);
	
	List<IncompleteAuthorization> findByCreateTimestampAfter(Timestamp startTS);
	
	
}
