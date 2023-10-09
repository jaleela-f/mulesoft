package com.cyberark.conjur.error;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

public enum ConjurErrorTypes implements ErrorTypeDefinition<ConjurErrorTypes> {
	CODE_200, CODE_401, CODE_403, CODE_404, CODE_422

}
