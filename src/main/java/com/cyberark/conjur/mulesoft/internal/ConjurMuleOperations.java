package com.cyberark.conjur.mulesoft.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class ConjurMuleOperations {
    private final Logger LOGGER = LoggerFactory.getLogger(ConjurMuleOperations.class);

    @MediaType(value = ANY, strict = false)
    public String retrieveSecret(@Config ConjurMuleConfiguration configuration, @Connection ConjurMuleConnection connection) {
        LOGGER.info("Inside Retrieve Secrets call");
        try {
            String accessToken = retrieveAccessToken(configuration, connection);
            return "Using Account[" + configuration.getConjurAccount() + "] with Connection id[" + connection.getId() + "]";
        } catch (Exception e) {
            LOGGER.error("Error in retrieve Secret: " + e.getMessage(), e);
            return "Error retrieving Secret: " + e.getMessage();
        }
    }

    private String retrieveAccessToken(@Config ConjurMuleConfiguration configuration, @Connection ConjurMuleConnection connection) throws Exception {
        LOGGER.info("Inside Retrieve AccessToken call");
        try {
            return "Using Configuration [" + configuration.getConfigId() + "] with Connection id [" + connection.getId() + "]";
        } catch (Exception e) {
            LOGGER.error("Error in Retrieve AccessToken: " + e.getMessage(), e);
            throw new Exception("Error Retrieve AccessToken: " + e.getMessage(), e);
        }
    }
}
