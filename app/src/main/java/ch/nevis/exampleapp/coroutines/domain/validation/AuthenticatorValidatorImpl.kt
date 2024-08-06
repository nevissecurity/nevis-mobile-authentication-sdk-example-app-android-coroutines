/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.validation

import ch.nevis.mobile.sdk.api.localdata.Authenticator
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionContext

/**
 * Default implementation of [AuthenticatorValidator] interface.
 *
 * @constructor Creates a new instance.
 */
class AuthenticatorValidatorImpl : AuthenticatorValidator {
    //region AuthenticatorValidator
    override fun validateForRegistration(
        context: AuthenticatorSelectionContext,
        authenticatorAllowlist: List<String>
    ): Set<Authenticator> {
        return context.authenticators()
            .filter { authenticatorAllowlist.contains(it.aaid()) }
            .filter {
                // Do not display:
                //  - policy non-compliant authenticators (this includes already registered authenticators)
                //  - not hardware supported authenticators.
                //  - not OS supported authenticators.
                //  - prefer Biometrics authenticator on Android
                it.isSupportedByHardware &&
                        it.isSupportedByOs &&
                        context.isPolicyCompliant(it.aaid()) &&
                        filterFingerprintIfNecessary(context, it)
            }.toSet()
    }

    override fun validateForAuthentication(
        context: AuthenticatorSelectionContext,
        authenticatorAllowlist: List<String>
    ): Set<Authenticator> {
        return context.authenticators()
            .filter { authenticatorAllowlist.contains(it.aaid()) }
            .filter {
                // Do not display:
                //  - non-registered authenticators
                //  - not hardware supported authenticators
                it.registration().isRegistered(context.account().username()) &&
                        it.isSupportedByHardware
            }.toSet()
    }
    //endregion

    //region Private Interface
    private fun filterFingerprintIfNecessary(
        context: AuthenticatorSelectionContext,
        authenticator: Authenticator
    ): Boolean {
        if (authenticator.aaid() != Authenticator.FINGERPRINT_AUTHENTICATOR_AAID) {
            return true
        }

        var isBiometricsRegistered = false
        var canRegisterBiometrics = false
        var canRegisterFingerprint = false
        context.authenticators().forEach {
            if (it.aaid() == Authenticator.BIOMETRIC_AUTHENTICATOR_AAID &&
                it.registration().isRegistered(context.account().username())
            ) {
                isBiometricsRegistered = true
            }

            if (it.aaid() == Authenticator.BIOMETRIC_AUTHENTICATOR_AAID &&
                context.isPolicyCompliant(it.aaid()) &&
                it.isSupportedByHardware
            ) {
                canRegisterBiometrics = true
            }

            if (it.aaid() == Authenticator.FINGERPRINT_AUTHENTICATOR_AAID &&
                context.isPolicyCompliant(it.aaid()) &&
                it.isSupportedByHardware
            ) {
                canRegisterFingerprint = true
            }
        }

        // If biometric can be registered (or is already registered), or if we
        // cannot register fingerprint, do not propose to register fingerprint
        // (we favor biometric over fingerprint).
        return !isBiometricsRegistered &&
                !canRegisterBiometrics &&
                canRegisterFingerprint
    }
    //endregion
}
