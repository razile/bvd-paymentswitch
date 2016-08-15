package com.bvd.authorization.formatter;

import com.bvd.models.KardallHostAuthorization;

public interface AuthorizationFormatter {
	
	String formatRequest(KardallHostAuthorization request);
	
	KardallHostAuthorization formatResponse(String response);
	
	String buildHeader(KardallHostAuthorization request);
	
	String buildSubHeader(KardallHostAuthorization request);
	
	String buildBody(KardallHostAuthorization request);
}
