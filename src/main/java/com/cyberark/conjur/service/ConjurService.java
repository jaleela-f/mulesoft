package com.cyberark.conjur.service;

import com.cyberark.conjur.sdk.ApiException;

public interface ConjurService {
	
	Object getSecret(String account,String variable_const,String variableId) throws ApiException;
	Object getSecerts(String variableIds,String account) throws ApiException;

}
