/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.operation

import ch.nevis.exampleapp.coroutines.R

/**
 * Enumeration of a available operations.
 */
enum class Operation(
    /**
     * The resource identifier used for the description.
     */
    val resId: Int
) {

    /**
     * Client initialization.
     */
    INIT_CLIENT(R.string.operation_init_client),

    /**
     * Registration.
     */
    REGISTRATION(R.string.operation_registration),

    /**
     * Authentication.
     */
    AUTHENTICATION(R.string.operation_authentication),

    /**
     * Change device information.
     */
    CHANGE_DEVICE_INFORMATION(R.string.operation_change_device_information),

    /**
     * Change PIN.
     */
    CHANGE_PIN(R.string.operation_change_pin),

    /**
     * Change Password.
     */
    CHANGE_PASSWORD(R.string.operation_change_password),

    /**
     * Out-of-band authentication.
     */
    OUT_OF_BAND_AUTHENTICATION(R.string.operation_authentication),

    /**
     * Out-of-band registration.
     */
    OUT_OF_BAND_REGISTRATION(R.string.operation_registration),

    /**
     * De-registration.
     */
    DEREGISTRATION(R.string.operation_deregistration),

    /**
     * Decode out-of-band payload.
     */
    DECODE_OUT_OF_BAND_PAYLOAD(R.string.operation_decode_oob_payload),

    /**
     * Process out-of-band payload.
     */
    PROCESS_OUT_OF_BAND_PAYLOAD(R.string.operation_process_oob_payload),

    /**
     * Local Data.
     */
    LOCAL_DATA(R.string.operation_local_data)

}
