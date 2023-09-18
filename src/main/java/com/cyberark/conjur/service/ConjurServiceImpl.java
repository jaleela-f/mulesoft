package com.cyberark.conjur.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cyberark.conjur.sdk.ApiException;
import com.cyberark.conjur.sdk.endpoint.SecretsApi;

public class ConjurServiceImpl implements ConjurService{
	
    private final Logger LOGGER = LoggerFactory.getLogger(ConjurServiceImpl.class);
	
	SecretsApi secretsApi = new SecretsApi();
	Object secrets = null;
	
	@Override
	public String getSecret(String account, String variable_const,String variableId) throws ApiException {
		
        LOGGER.info("Getting secret for account: {}", account);
		String secrets = secretsApi.getSecret(account, "variable", variableId);
        LOGGER.info("Secret retrieved successfully.");
		return secrets;
	}

	@Override
	public Object getSecerts(String variableIds, String account) throws ApiException {
		
        LOGGER.info("Getting secrets for account: {}", account);
		String[] keys = variableIds.split(",");
		StringBuilder encodeKey = new StringBuilder();
		
		

		for (String k : keys) {
			if (encodeKey.length() == 0) {

				encodeKey.append(account + ":");
				encodeKey.append("variable:");
				// encodeKey.append(URLEncoder.encode(k, StandardCharsets.UTF_8.toString()));
				encodeKey.append(k);

			} else {
				encodeKey.append(",");
				encodeKey.append(account + ":");
				encodeKey.append("variable:");
				// encodeKey.append(URLEncoder.encode(k, StandardCharsets.UTF_8.toString()));
				encodeKey.append(k);
			}
		}
		
		secrets = secretsApi.getSecrets(encodeKey.toString());
        LOGGER.info("Secrets retrieved successfully.");
		return secrets;
	}

}
