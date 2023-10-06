package com.cyberark.conjur.mulesoft.internal;

import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyberark.conjur.constant.ConjurConstant;
import com.cyberark.conjur.core.ConjurConnection;
import com.cyberark.conjur.domain.ConjurConfiguration;
import com.cyberark.conjur.sdk.ApiException;
import com.cyberark.conjur.sdk.endpoint.SecretsApi;
import com.cyberark.conjur.service.ConjurService;
import com.cyberark.conjur.service.ConjurServiceImpl;

/**
 * This class (as it's name implies) provides connection instances and the
 * funcionality to disconnect and validate those connections.
 * <p>
 * All connection related parameters (values required in order to create a
 * connection) must be declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares
 * that connections resolved by this provider will be pooled and reused. There
 * are other implementations like {@link CachedConnectionProvider} which lazily
 * creates and caches connections or simply {@link ConnectionProvider} if you
 * want a new connection each time something requires one.
 */

public class ConjurMuleConnectionProvider implements ConnectionProvider<ConjurMuleConnection> {

	private final Logger LOGGER = LoggerFactory.getLogger(ConjurMuleConnectionProvider.class);

	/**
	 * A parameter that is always required to be configured.
	 */
	@Parameter
	private String conjurAccount;
	@Parameter
	private String conjurApplianceUrl;
	@Parameter
	@Optional
	private String conjurAuthnLogin;
	@Parameter
	@Optional
	private String conjurApiKey;
	@Parameter
	@Optional
	private String conjurSslCertificate;
	@Parameter
	@Optional
	private String conjurCertFile;
	@Parameter
	private String key;

	private SecretsApi secretsApi = new SecretsApi();
	ConjurService conjurService = new ConjurServiceImpl();

	Object secretValue;

	@Override
	public ConjurMuleConnection connect() throws ConnectionException {

		LOGGER.info("Calling Demo Connection Provider connect()");

		ConjurConfiguration config = new ConjurConfiguration();

		config.setConjurAccount(conjurAccount);
		config.setConjurApplianceUrl(conjurApplianceUrl);
		config.setConjurAuthnLogin(conjurAuthnLogin);
		config.setConjurApiKey(conjurApiKey);
		config.setConjurSslCertificate(conjurSslCertificate);
		config.setConjurCertFile(conjurCertFile);

		ConjurConnection.getConnection(config);
		String account = ConjurConnection.getAccount(secretsApi);
		String[] keys = key.split(",");
		try {

			if (keys.length > 1) {

				secretValue = conjurService.getSecerts(key, account);

			} else {
				secretValue = conjurService.getSecret(account, ConjurConstant.CONJUR_KIND, key);

			}

		}

		catch (ApiException e) {

			LOGGER.info("An error occurred while fetching secrets: " + e.getMessage());
			e.printStackTrace();
		}

		return new ConjurMuleConnection(secretValue.toString());

	}

	@Override
	public void disconnect(ConjurMuleConnection connection) {
		try {
			connection.invalidate();
		} catch (Exception e) {
			LOGGER.error("Error while disconnecting [" + connection.getId() + "]: " + e.getMessage(), e);
		}
	}

	@Override
	public ConnectionValidationResult validate(ConjurMuleConnection connection) {
		return ConnectionValidationResult.success();
	}

}
