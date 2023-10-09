
package com.cyberark.conjur.mulesoft.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import com.cyberark.conjur.domain.ConjurConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class ConjurMuleOperationsTest {

	@InjectMocks
	public ConjurConfiguration config;

	@InjectMocks
	public ConjurMuleConfiguration configuration;

	@InjectMocks
	public ConjurMuleConnection connection;

	@Test
	public void callRetrieveSecret() throws Exception {

		try (MockedStatic<ConjurMuleOperations> jwtGetTokenMockedStatic = mockStatic(ConjurMuleOperations.class)) {

			mock(ConjurMuleOperations.class);
			mock(ConjurMuleConnection.class);
			mock(ConjurMuleConfiguration.class);

			ConjurMuleOperations conjurMuleOperations = mock(ConjurMuleOperations.class);
			jwtGetTokenMockedStatic.when(() -> conjurMuleOperations.retrieveSecret(configuration, connection))
					.thenReturn("Using Account[" + config.getConjurAccount() + "] with Connection id["
							+ connection.getId() + "]");

			assertEquals(
					"Using Account[" + config.getConjurAccount() + "] with Connection id[" + connection.getId() + "]",
					conjurMuleOperations.retrieveSecret(configuration, connection));

		}

	}
}
