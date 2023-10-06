package com.cyberark.conjur.mulesoft.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class ConjurMuleOperations {
    private final Logger LOGGER = LoggerFactory.getLogger(ConjurMuleOperations.class);

    @MediaType(value = ANY, strict = false)
    public String retrieveSecret(@Config ConjurMuleConfiguration configuration, @Connection ConjurMuleConnection connection) {
        LOGGER.info("Inside Retrieve Secrets call");
        try {
              return "Using  Connection id[" + connection.getId() + "]";
        } catch (Exception e) {
            LOGGER.error("Error in retrieve Secret: " + e.getMessage(), e);
            return "Error retrieving Secret: " + e.getMessage();
        }
    }


}
