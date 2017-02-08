package com.bvd.paymentswitch.jpa.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bvd.paymentswitch.models.PosAuthorization;

public interface POSAuthorizationRepository extends CrudRepository<PosAuthorization, Long> {
	
	List<PosAuthorization> findBySiteId(String siteId);
	
	List<PosAuthorization> findByAuthId(String authId);
	
	List<PosAuthorization> findByDateTime(Timestamp dateTime);
	
	@Query(value="SELECT fuel_type from pos_authorization where auth_id = :authId and Date(date_time) = Date(:dateTime) limit 1", nativeQuery=true)
	String findByAuthIdAndDateTime(@Param(value = "authId") String authId, @Param(value = "dateTime") Timestamp dateTime);
}
