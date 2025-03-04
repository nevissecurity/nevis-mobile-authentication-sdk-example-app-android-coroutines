/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.common.error

import android.content.Context
import androidx.navigation.NavController
import ch.nevis.exampleapp.coroutines.NavigationGraphDirections
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.error.MobileAuthenticationClientException
import ch.nevis.exampleapp.coroutines.ui.error.parameter.ErrorNavigationParameter
import ch.nevis.mobile.sdk.api.MobileAuthenticationClientError
import ch.nevis.mobile.sdk.api.operation.OperationError
import timber.log.Timber

/**
 * Default implementation of [ErrorHandler] interface.
 *
 * @constructor Creates a new instance.
 * @param context An Android [Context] object for [String] resource resolving.
 * @param navController The navigation controller.
 */
class DefaultErrorHandlerImpl(
    private val context: Context,
    private val navController: NavController
) : ErrorHandler {

    //region ErrorHandler
    override fun handle(error: Throwable): Boolean {
        Timber.e(error)
        val errorMessage = when (error) {
            is MobileAuthenticationClientException -> {
                getErrorMessage(error.error)
            }
            is BusinessException -> {
                context.getString(error.type.resId)
            }
            else -> getErrorMessage(error)
        }

        val action = NavigationGraphDirections.actionGlobalErrorFragment(
            ErrorNavigationParameter(errorMessage)
        )
        navController.navigate(action)

        return true
    }
    //endregion

    //region Private Interface
    /**
     * Gets/composes an error message based on the received error response.
     *
     * @param error The received client error.
     * @return The error message.
     */
    private fun getErrorMessage(error: MobileAuthenticationClientError): String {
        // As this is an example app, we are directly showing the technical error occurring.
        // Be aware that this is not to be considered best practice. Your own production app should
        // handle the errors in a more appropriate manner  such as providing translations for all your
        // supported languages as well as simplifying the error message presented to the end-user in
        // a way non-technical adverse people can understand and act upon them.
        return when (error) {
            is OperationError -> error.errorCode().description()
            is ch.nevis.mobile.sdk.api.operation.authcloudapi.AuthCloudApiError.OperationError -> error.errorCode()
                .description()
            else -> error.description()
        }
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return throwable.message ?: throwable.stackTraceToString()
    }
    //endregion
}
