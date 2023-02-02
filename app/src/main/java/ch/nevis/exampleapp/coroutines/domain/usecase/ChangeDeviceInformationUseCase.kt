/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for starting a change device information operation.
 */
interface ChangeDeviceInformationUseCase {

    /**
     * Executes the use-case.
     *
     * @param name The new device information name.
     * @param fcmRegistrationToken The FCM registration token if it is known, otherwise null.
     * @param disablePushNotifications A flag that tells push notifications have to be disabled or not.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(
        name: String,
        fcmRegistrationToken: String? = null,
        disablePushNotifications: Boolean = false
    ): Response
}