/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.credential.parameter

import ch.nevis.exampleapp.coroutines.ui.credential.model.CredentialViewMode
import ch.nevis.mobile.sdk.api.operation.RecoverableError
import ch.nevis.mobile.sdk.api.operation.pin.PinAuthenticatorProtectionStatus
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter of the Credential view in case of PIN authenticator.
 *
 * @constructor Creates a new instance.
 * @param credentialViewMode The mode, the Credential view intend to be used/initialized.
 * @param lastRecoverableError The last recoverable error. It exists only if there was already a failed
 *      Password operation attempt.
 * @param pinAuthenticatorProtectionStatus Status object of the PIN authenticator.
 */
@Parcelize
data class PinNavigationParameter(
    override val credentialViewMode: CredentialViewMode,
    @IgnoredOnParcel
    override val lastRecoverableError: RecoverableError? = null,

    /**
     * Status object of the PIN authenticator.
     */
    @IgnoredOnParcel
    val pinAuthenticatorProtectionStatus: PinAuthenticatorProtectionStatus? = null
) : CredentialNavigationParameter()
