package com.cyberark.conjur.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyberark.conjur.sdk.AccessToken;
import com.cyberark.conjur.sdk.ApiClient;
import com.cyberark.conjur.sdk.ApiException;

public class AccessTokenProviderImpl implements AccessTokenProvider  {

	private final Logger LOGGER = LoggerFactory.getLogger(AccessTokenProviderImpl.class);

	@Override
	public AccessToken getNewAccessToken(ApiClient client) throws ApiException{
		LOGGER.info("Start: creating new Access Token");
		return client.getNewAccessToken();

	}

	@Override
	public AccessToken getJwtAccessToken(ApiClient conjurClient, String jwtTokenPath, String authenticatorId) {

		LOGGER.warn("JWT Access Token retrieval is not implemented yet.");
		return null;
	}

}
