package com.cyberark.conjur.authentication;

import com.cyberark.conjur.sdk.AccessToken;
import com.cyberark.conjur.sdk.ApiClient;
import com.cyberark.conjur.sdk.ApiException;

public interface AccessTokenProvider {
	
	public AccessToken getNewAccessToken(ApiClient client) throws ApiException;
	public AccessToken getJwtAccessToken(ApiClient conjurClient, String jwtTokenPath, String authenticatorId);


}
