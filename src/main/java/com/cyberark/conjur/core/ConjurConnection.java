package com.cyberark.conjur.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyberark.conjur.authentication.AccessTokenProvider;
import com.cyberark.conjur.authentication.AccessTokenProviderImpl;
import com.cyberark.conjur.constant.ConjurConstant;
import com.cyberark.conjur.domain.ConjurConfiguration;
import com.cyberark.conjur.sdk.AccessToken;
import com.cyberark.conjur.sdk.ApiClient;
import com.cyberark.conjur.sdk.Configuration;
import com.cyberark.conjur.sdk.endpoint.SecretsApi;

public class ConjurConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConjurConnection.class);

	public static ApiClient getConnection(ConjurConfiguration config) {

		LOGGER.info("Start: Calling getConnection()");
		AccessToken accessToken = null;

		ApiClient client = Configuration.getDefaultApiClient();
		AccessTokenProvider accessTokenProvider = new AccessTokenProviderImpl();

		client.setAccount(config.getConjurAccount());
		client.setBasePath(config.getConjurApplianceUrl());
		client.setUsername(config.getConjurAuthnLogin());
		client.setApiKey(config.getConjurApiKey());

		InputStream sslInputStream = null;
		String sslCertificate = config.getConjurSslCertificate();
		String certFile = config.getConjurCertFile();
		String conjurApiKey = config.getConjurApiKey();

		String token = "";

		try {
			if (StringUtils.isNotEmpty(sslCertificate)) {
				sslInputStream = new FileInputStream(sslCertificate);
			} else {
				if (StringUtils.isNotEmpty(certFile))
					sslInputStream = new FileInputStream(certFile);
			}

			if (sslInputStream != null) {
				client.setSslCaCert(sslInputStream);
				sslInputStream.close();
			}
			if (conjurApiKey != null && !conjurApiKey.isEmpty()) {
				accessToken = accessTokenProvider.getNewAccessToken(client);
				if (accessTokenNotNull(accessToken)) {

					token = accessToken.getHeaderValue();
					client.setAccessToken(token);
					Configuration.setDefaultApiClient(client);
					LOGGER.info("Connection with Conjur is successful");

				} else {
					token = accessToken.getHeaderValue();
					client.setAccessToken(token);
					Configuration.setDefaultApiClient(client);
				}
			}
		} catch (IOException ex) {
			LOGGER.error("Error setting up Conjur connection: {}", ex.getMessage());

		}

		return client;
	}

	public static String getAccount(SecretsApi secretApi) {
		ApiClient apiClient = secretApi.getApiClient();
		return (apiClient != null) ? apiClient.getAccount() : ConjurConstant.CONJUR_ACCOUNT;
	}

	private static boolean accessTokenNotNull(AccessToken token) {
		return true;
	}

}
