/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyDevicePasscodeResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.domain.util.titleResId
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodeUserVerificationContext
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodeUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodeUserVerifier
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [DevicePasscodeUserVerifier] interface. It stores the device passcode verification
 * step context into its state and resumes the cancellableContinuation found in state with [VerifyDevicePasscodeResponse]
 * indicating that the running operation waiting for a device passcode verification.
 *
 * @constructor Creates a new instance.
 * @param stateRepository The state repository that stores the state of the running operation.
 */
class DevicePasscodeUserVerifierImpl(
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : DevicePasscodeUserVerifier {

    //region DevicePasscodeUserVerifier
    /** @suppress */
    override fun verifyDevicePasscode(
        devicePasscodeUserVerificationContext: DevicePasscodeUserVerificationContext,
        devicePasscodeUserVerificationHandler: DevicePasscodeUserVerificationHandler
    ) {
        Timber.asTree().sdk("Please start device passcode user verification.")

        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.devicePasscodeUserVerificationHandler = devicePasscodeUserVerificationHandler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            VerifyDevicePasscodeResponse(
                devicePasscodeUserVerificationContext.authenticator().titleResId()
            )
        )
    }

    /** @suppress */
    override fun onValidCredentialsProvided() {
        Timber.asTree()
            .sdk("Valid credentials provided during device passcode verification.")
    }
    //endregion
}
