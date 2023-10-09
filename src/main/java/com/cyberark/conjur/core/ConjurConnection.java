package com.cyberark.conjur.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.param.stereotype.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyberark.conjur.authentication.AccessTokenProvider;
import com.cyberark.conjur.authentication.AccessTokenProviderImpl;
import com.cyberark.conjur.constant.ConjurConstant;
import com.cyberark.conjur.domain.ConjurConfiguration;
import com.cyberark.conjur.error.ConjurErrorProvider;
import com.cyberark.conjur.error.ConjurErrorTypes;
import com.cyberark.conjur.sdk.AccessToken;
import com.cyberark.conjur.sdk.ApiClient;
import com.cyberark.conjur.sdk.ApiException;
import com.cyberark.conjur.sdk.Configuration;
import com.cyberark.conjur.sdk.endpoint.SecretsApi;

public class ConjurConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConjurConnection.class);

	public static ApiClient getConnection(ConjurConfiguration config) throws ApiException {

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
		} catch (IOException ex) {
			LOGGER.error("Error processing CERT_FILE: {}", ex.getMessage());

		}

		if (conjurApiKey != null && !conjurApiKey.isEmpty()) {
			try {
			accessToken = accessTokenProvider.getNewAccessToken(client);

			if (accessToken != null) {

				token = accessToken.getHeaderValue();
				client.setAccessToken(token);
				Configuration.setDefaultApiClient(client);
				LOGGER.info("StatusCode:200_OK"+ConjurConstant.CODE_200_CONNECTION);

			}
			}catch(ApiException ex)
			{
				int statusCode = ex.getCode();
				if(statusCode==401)
				{
					LOGGER.info("The request lacks valid authentication credentials.");

					if (StringUtils.isNoneEmpty(client.getAccount())) {
						LOGGER.debug("Using Account" + checkParamValue(client.getAccount()));
					}
					if (StringUtils.isNoneEmpty(config.getConjurApplianceUrl())) {
						LOGGER.debug("Using ApplianceUrl" + checkParamValue(config.getConjurApplianceUrl()));
					}
					if (StringUtils.isNoneEmpty(config.getConjurApiKey())) {
						LOGGER.debug("Using API Key" + checkParamValue(config.getConjurApiKey()));
					}
					if (StringUtils.isNoneEmpty(config.getConjurSslCertificate())) {
						LOGGER.debug("Using SSL_CERTIFICATE" + checkParamValue(config.getConjurSslCertificate()));
					}
					if (StringUtils.isNotEmpty(config.getConjurCertFile())) {
						LOGGER.debug("Using Certificate" + checkParamValue(config.getConjurCertFile()));
					}
					throw new ApiException(ex.getCode()+":"+ex.getResponseBody());

				}else if(statusCode==403)
				{
					throw new ApiException(ex.getCode()+":"+ex.getResponseBody());
				}
			}
		}

		return client;
	}

	public static String getAccount(SecretsApi secretApi) {
		ApiClient apiClient = secretApi.getApiClient();
		return (apiClient != null) ? apiClient.getAccount() : ConjurConstant.CONJUR_ACCOUNT;
	}

	private static String checkParamValue(String str) {

		if (StringUtils.isNoneEmpty(str) && str.length() > 2) {
			int len = str.length();
			char first = str.charAt(0);
			char last = str.charAt(len - 1);
			String middle = "*******";
			return first + middle + last;

		}
		return str;
	}

}
