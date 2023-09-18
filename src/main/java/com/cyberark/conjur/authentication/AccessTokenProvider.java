package com.cyberark.conjur.authentication;

import com.cyberark.conjur.sdk.AccessToken;
import com.cyberark.conjur.sdk.ApiClient;

public interface AccessTokenProvider {
	
	public AccessToken getNewAccessToken(ApiClient client);
	public AccessToken getJwtAccessToken(ApiClient conjurClient, String jwtTokenPath, String authenticatorId);


}
