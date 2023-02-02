/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricPromptOptions

/**
 * Use-case interface for verifying the user using biometric authenticator.
 *
 * **IMPORTANT**: Prior to use this use-case one of the following use-cases must be executed:
 * - [AuthCloudApiRegistrationUseCase]
 * - [InBandAuthenticationUseCase]
 * - [InBandRegistrationUseCase]
 * - [ProcessOutOfBandPayloadUseCase]
 *
 * This use-case must be executed after a [ch.nevis.exampleapp.domain.model.response.VerifyBiometricResponse]
 * is received during an authentication or registration operation.
 */
interface VerifyBiometricUseCase {

    /**
     * Executes the use-case.
     *
     * @param biometricPromptOptions The biometric prompt options that is used for displaying the
     * dialog that asks the user to verify her-/himself.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(biometricPromptOptions: BiometricPromptOptions): Response
}