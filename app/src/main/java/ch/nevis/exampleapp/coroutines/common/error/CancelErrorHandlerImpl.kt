/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.common.error

import androidx.navigation.NavController
import ch.nevis.exampleapp.coroutines.NavigationGraphDirections
import ch.nevis.exampleapp.coroutines.domain.model.error.MobileAuthenticationClientException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.ui.result.parameter.ResultNavigationParameter
import ch.nevis.mobile.sdk.api.operation.FidoErrorCode
import ch.nevis.mobile.sdk.api.operation.OperationError
import timber.log.Timber

/**
 * Implementation of [ErrorHandler] interface for cases the user cancels an operation.
 */
class CancelErrorHandlerImpl(
    private val navController: NavController
) : ErrorHandler {

    //region ErrorHandler
    override fun handle(error: Throwable): Boolean {
        Timber.w(error)

        if (isUserCancelledError(error)) {
            val navigationParameter =
                ResultNavigationParameter.forCancelledOperation(getOperation(error))
            val action = NavigationGraphDirections.actionGlobalResultFragment(navigationParameter)
            navController.navigate(action)
            return true
        }
        return false
    }
    //endregion

    //region Private Interface
    private fun isUserCancelledError(error: Throwable): Boolean {
        if (error is MobileAuthenticationClientException) {
            val fidoErrorCode = when (error.error) {
                is OperationError -> error.error.errorCode()
                is ch.nevis.mobile.sdk.api.operation.authcloudapi.AuthCloudApiError.OperationError -> error.error.errorCode()
                else -> null
            }

            if (fidoErrorCode == FidoErrorCode.USER_CANCELED) {
                return true
            }
        }
        return false
    }

    private fun getOperation(error: Throwable): Operation? {
        return if (error is MobileAuthenticationClientException) {
            error.operation
        } else {
            null
        }
    }
    //endregion
}