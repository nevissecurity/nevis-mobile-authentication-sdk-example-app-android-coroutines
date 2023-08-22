/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.verifyBiometric.model

/**
 * Enumeration of available Verify Biometric view modes.
 */
enum class VerifyBiometricViewMode {

    /**
     * Fingerprint verification.
     */
    FINGERPRINT,

    /**
     * Biometric verification that could be fingerprint or face ID verification as well.
     */
    BIOMETRIC,

    /**
     * Device passcode verification.
     */
    DEVICE_PASSCODE
}