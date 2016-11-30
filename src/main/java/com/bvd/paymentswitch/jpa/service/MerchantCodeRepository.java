package com.bvd.paymentswitch.jpa.service;

import org.springframework.data.repository.CrudRepository;

import com.bvd.paymentswitch.models.MerchantCode;
import com.bvd.paymentswitch.models.MerchantCodePK;

public interface MerchantCodeRepository extends CrudRepository<MerchantCode, MerchantCodePK> {

}
