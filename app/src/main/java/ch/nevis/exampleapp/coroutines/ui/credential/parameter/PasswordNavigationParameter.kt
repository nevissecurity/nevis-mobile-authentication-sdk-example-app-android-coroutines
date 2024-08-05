/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.credential.parameter

import ch.nevis.exampleapp.coroutines.ui.credential.model.CredentialViewMode
import ch.nevis.mobile.sdk.api.operation.RecoverableError
import ch.nevis.mobile.sdk.api.operation.password.PasswordAuthenticatorProtectionStatus
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter of the Credential view in case of Password authenticator.
 */
@Parcelize
data class PasswordNavigationParameter(
    /**
     * The mode, the Credential view intend to be used/initialized.
     */
    override val credentialViewMode: CredentialViewMode,

    /**
     * The last recoverable error. It exists only if there was already a failed Password operation attempt.
     */
    @IgnoredOnParcel
    override val lastRecoverableError: RecoverableError? = null,

    /**
     * Status object of the Password authenticator.
     */
    @IgnoredOnParcel
    val passwordAuthenticatorProtectionStatus: PasswordAuthenticatorProtectionStatus? = null
) : CredentialNavigationParameter()
