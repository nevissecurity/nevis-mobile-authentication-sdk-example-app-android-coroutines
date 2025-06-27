/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2025. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.devicecapabilities.FidoUafAttestationInformation

/**
 * A [Response] class that holds the requested [FidoUafAttestationInformation].
 *
 * @constructor Creates a new instance.
 * @param onlySurrogateBasicSupported Indicates if only surrogate basic attestation is supported.
 * @param onlyDefaultMode Indicates if only default mode is supported.
 * @param strictMode Indicates if strict mode is supported.
 */
sealed class FidoUafAttestationInformationResponse(
    /**
     * Indicates if only surrogate basic attestation is supported.
     */
    val onlySurrogateBasicSupported: Boolean,
    /**
     * Indicates if only default mode is supported.
     */
    val onlyDefaultMode: Boolean,
    /**
     * Indicates if strict mode is supported.
     */
    val strictMode: Boolean
) : Response {
    /**
     * Represents the case when only surrogate basic attestation is supported by the device.
     */
    class OnlySurrogateBasicSupported : FidoUafAttestationInformationResponse(
        onlySurrogateBasicSupported = true,
        onlyDefaultMode = false,
        strictMode = false
    )

    /**
     * Represents the case when both surrogate basic and default attestation modes are supported by the device.
     */
    class OnlyDefaultMode : FidoUafAttestationInformationResponse(
        onlySurrogateBasicSupported = true,
        onlyDefaultMode = true,
        strictMode = false
    )

    /**
     * Represents the case when surrogate basic, default, and strict attestation modes are all supported by the device.
     */
    class StrictMode : FidoUafAttestationInformationResponse(
        onlySurrogateBasicSupported = true,
        onlyDefaultMode = true,
        strictMode = true
    )
}
