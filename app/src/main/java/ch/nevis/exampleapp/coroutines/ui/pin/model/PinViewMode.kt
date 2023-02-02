/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.pin.model

/**
 * Enumeration of available PIN view modes.
 */
enum class PinViewMode {

    /**
     * Change PIN mode
     */
    CHANGE_PIN,

    /**
     * Enroll PIN mode
     */
    ENROLL_PIN,

    /**
     * Verify PIN mode
     */
    VERIFY_PIN
}