package com.cyberark.conjur.constant;

public class ConjurConstant {
	
	/**
	 * Generic extension for properties define at conjur.properties.
	 */
	public static final String CONJUR_MAPPING = "conjur.mapping.";
 
	/**
	 * Conjur_Kind.
	 */
	public static final String CONJUR_KIND = "variable";
	
    
	/**
	 * Custom property file name.
	 */
	public static final String CONJUR_PROPERTIES = "/conjur.properties";
	
   
	/**
	 * Environment variable.  
	 */
	public static final String CONJUR_ACCOUNT = System.getenv("CONJUR_ACCOUNT");
	

	/**
	 * Error Message.
	 */
	public static final String CONJUR_APIKEY_ERROR="Please provide Conjur Authn Token file or else api Key in environment Variable";

	
	/**
	 * Not Found message.
	 */
	public static final String NOT_FOUND = "NotFound";
	
	public static final String CODE_200_CONNECTION = "Connection with Conjur is successful";
	
	public static final String CODE_200_DATA_FOUND = "The secret values was retrieved successfully.";
	public static final String CODE_401 ="The request lacks valid authentication credentials.";
	public static final String CODE_403 ="The authenticated user lacks the necessary privilege.";
	public static final String CODE_404_SINGLE_RETRIEVAL ="The variable does not exist, or it does not have any secret values.";
	public static final String CODE_404_BATCH_RETRIEVAL ="At least one variable does not exist, or at least one variable does not have any secret values.";
	public static final String CODE_422 ="A request parameter was missing or invalid.";
	

	


}
