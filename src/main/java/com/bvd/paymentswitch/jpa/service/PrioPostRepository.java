package com.bvd.paymentswitch.jpa.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bvd.paymentswitch.models.PriorPostAuthorization;;

public interface PrioPostRepository extends CrudRepository<PriorPostAuthorization, Long> {
	
	List<PriorPostAuthorization> findByLocation(String siteId);
	
	List<PriorPostAuthorization> findByAuthorizationCode(String authId);
}
