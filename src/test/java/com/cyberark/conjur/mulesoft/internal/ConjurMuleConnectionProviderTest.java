
package com.cyberark.conjur.mulesoft.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import com.cyberark.conjur.constant.ConjurConstant;
import com.cyberark.conjur.core.ConjurConnection;
import com.cyberark.conjur.domain.ConjurConfiguration;
import com.cyberark.conjur.sdk.ApiClient;
import com.cyberark.conjur.sdk.ApiException;
import com.cyberark.conjur.sdk.endpoint.SecretsApi;

import com.cyberark.conjur.service.ConjurServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ConjurMuleConnectionProviderTest {

	@InjectMocks
	public ConjurMuleConnectionProvider connectionProvider;

	public SecretsApi secretsApi;

	public ApiClient apiClient;

	@InjectMocks
	public ConjurServiceImpl conjurService;

	String key = "jenkins-app/dbUserName,jenkins-app/dbPassword,jenkins-app/dbUrl";

	String[] keys = key.split(",");

	@InjectMocks
	public ConjurConfiguration conjurConfig;

	@Before
	public void setUp() throws UnknownHostException {
		java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
		mock(ConjurConfiguration.class);
		apiClient = mock(ApiClient.class);
		secretsApi = mock(SecretsApi.class);
		conjurConfig.setConjurAccount(System.getenv().getOrDefault("CONJUR_ACCOUNT", null));
		conjurConfig.setConjurApplianceUrl(System.getenv().getOrDefault("CONJUR_APPLIANCE_URL", null));
		conjurConfig.setConjurAuthnLogin(
				System.getenv().getOrDefault("CONJUR_AUTHN_LOGIN", null) + localMachine.getHostName());
		conjurConfig.setConjurApiKey(System.getenv().getOrDefault("CONJUR_AUTHN_API_KEY", null));
		conjurConfig.setConjurSslCertificate(System.getenv().getOrDefault("CONJUR_SSL_CERTIFICATE", null));
		conjurConfig.setConjurCertFile(System.getenv().getOrDefault("CONJUR_CERT_FILE", null));

	}

	@Test
	public void conjurConnection() throws ApiException {

		try (MockedStatic<ConjurConnection> getConnectionMockStatic = mockStatic(ConjurConnection.class)) {

			getConnectionMockStatic.when(() -> ConjurConnection.getConnection(conjurConfig)).thenReturn(apiClient);

			assertEquals(apiClient, ConjurConnection.getConnection(conjurConfig));
		}

	}

	@Test
	public void checkConjurAccount() {
		conjurConfig = mock(ConjurConfiguration.class);
		when(conjurConfig.getConjurAccount()).thenReturn("myConjurAccount");
		assertEquals("myConjurAccount", conjurConfig.getConjurAccount());

	}

	@Test
	public void getAccount() {

		when(secretsApi.getApiClient()).thenReturn(apiClient);

		when(apiClient.getAccount()).thenReturn("myConjurAccount");

		String result = ConjurConnection.getAccount(secretsApi);

		verify(secretsApi, times(1)).getApiClient();

		verify(apiClient, times(1)).getAccount();

		assertEquals("myConjurAccount", result);

	}

	@Test
	public void getSecretVal() {

		Object val;
		String secretValue;
		try {
			apiClient = ConjurConnection.getConnection(conjurConfig);

			if (keys.length > 1) {

				try (MockedStatic<Object> getSecretsValMockStatic = mockStatic(Object.class)) {
					ConjurServiceImpl service = mock(ConjurServiceImpl.class);
					val = conjurService.getSecerts(key, conjurConfig.getConjurAccount());
					when(service.getSecerts(key, conjurConfig.getConjurAccount())).thenReturn(val);
					assertEquals(service.getSecerts(key, conjurConfig.getConjurAccount()), val);
				}

			} else {

				try (MockedStatic<Object> getSecretsValMockStatic = mockStatic(Object.class)) {

					ConjurServiceImpl service = mock(ConjurServiceImpl.class);

					secretValue = conjurService.getSecret(conjurConfig.getConjurAccount(), ConjurConstant.CONJUR_KIND,
							key);

					when(service.getSecret(conjurConfig.getConjurAccount(), ConjurConstant.CONJUR_KIND, key))
							.thenReturn(secretValue);
					assertEquals(service.getSecret(conjurConfig.getConjurAccount(), ConjurConstant.CONJUR_KIND, key),
							secretValue);
				}

			}

		} catch (ApiException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void conjurDisconnect() {

		ConjurMuleConnection connection = mock(ConjurMuleConnection.class);

		connectionProvider.disconnect(connection);

		verify(connection, times(1)).invalidate();
	}

}
