/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodePromptOptions

/**
 * Use-case interface for verifying the user using device passcode authenticator.
 *
 * **IMPORTANT**: Prior to use this use-case one of the following use-cases must be executed:
 * - [AuthCloudApiRegistrationUseCase]
 * - [InBandAuthenticationUseCase]
 * - [InBandRegistrationUseCase]
 * - [ProcessOutOfBandPayloadUseCase]
 *
 * This use-case must be executed after a [ch.nevis.exampleapp.coroutines.domain.model.response.VerifyDevicePasscodeResponse]
 * is received during an authentication or registration operation.
 */
interface VerifyDevicePasscodeUseCase {

    /**
     * Executes the use-case.
     *
     * @param devicePasscodePromptOptions The device passcode prompt options that is used for displaying the
     * dialog that asks the user to verify her-/himself.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(devicePasscodePromptOptions: DevicePasscodePromptOptions): Response
}