/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.validation

import ch.nevis.mobile.sdk.api.localdata.Authenticator
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionContext

/**
 * Interface declaration for validating list of authenticators for different operations.
 */
interface AuthenticatorValidator {
    /**
     * Validates authenticators for registration operation.
     *
     * @param context The context holding the authenticators to validate.
     * @param authenticatorAllowlist List of allowlisted authenticators.
     * @return List of allowed authenticators.
     */
    fun validateForRegistration(
        context: AuthenticatorSelectionContext,
        authenticatorAllowlist: List<String>
    ): Set<Authenticator>

    /**
     * Validates authenticators for authentication operation.
     *
     * @param context The context holding the authenticators to validate.
     * @param authenticatorAllowlist List of allowlisted authenticators.
     * @return List of allowed authenticators.
     */
    fun validateForAuthentication(
        context: AuthenticatorSelectionContext,
        authenticatorAllowlist: List<String>
    ): Set<Authenticator>
}
