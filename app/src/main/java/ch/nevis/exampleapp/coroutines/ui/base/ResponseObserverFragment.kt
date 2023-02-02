/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.base

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.nevis.exampleapp.coroutines.NavigationGraphDirections
import ch.nevis.exampleapp.coroutines.common.error.ErrorHandlerChain
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.*
import ch.nevis.exampleapp.coroutines.ui.pin.model.PinViewMode
import ch.nevis.exampleapp.coroutines.ui.pin.parameter.PinNavigationParameter
import ch.nevis.exampleapp.coroutines.ui.result.parameter.ResultNavigationParameter
import ch.nevis.exampleapp.coroutines.ui.selectAccount.parameter.SelectAccountNavigationParameter
import ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.parameter.SelectAuthenticatorNavigationParameter
import ch.nevis.exampleapp.coroutines.ui.transactionConfirmation.parameter.TransactionConfirmationNavigationParameter
import ch.nevis.exampleapp.coroutines.ui.verifyBiometric.model.VerifyBiometricViewMode
import ch.nevis.exampleapp.coroutines.ui.verifyBiometric.parameter.VerifyBiometricNavigationParameter
import javax.inject.Inject

/**
 * Base, abstract fragment implementation for fragments those process [Response] objects sent by view model
 * or use-case layer. It covers handling almost all known [Response] objects, it is strongly suggest to implement
 * this class when creating a new view that may start an operation or represent an operation step.
 */
abstract class ResponseObserverFragment : Fragment() {

    /**
     * The abstract declaration of a view model. The implementations have to provide their own view model.
     */
    protected abstract val viewModel: OperationViewModel

    /**
     * An instance of a [ErrorHandlerChain] implementation. Received errors will be passed to this error
     * handler chain instance.
     */
    @Inject
    protected lateinit var errorHandlerChain: ErrorHandlerChain

    //region Fragment
    override fun onResume() {
        super.onResume()

        viewModel.responseLiveData.observe(viewLifecycleOwner) {
            processResponse(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.responseLiveData.removeObservers(viewLifecycleOwner)
    }
    //endregion

    //region Protected Interface
    /**
     * Processes a [Response] object.
     *
     * @param response The [Response] object to process.
     */
    protected open fun processResponse(response: Response) {
        val navController = findNavController()

        when (response) {
            is SelectAuthenticatorResponse -> {
                val action = NavigationGraphDirections.actionGlobalSelectAuthenticatorFragment(
                    SelectAuthenticatorNavigationParameter(response.authenticators)
                )
                navController.navigate(action)
            }
            is SelectAccountResponse -> {
                response.transactionConfirmationData?.also {
                    val parameter = TransactionConfirmationNavigationParameter(
                        response.operation,
                        response.accounts,
                        it.decodeToString()
                    )
                    val action =
                        NavigationGraphDirections.actionGlobalTransactionConfirmationFragment(
                            parameter
                        )
                    navController.navigate(action)
                } ?: run {
                    val parameter = SelectAccountNavigationParameter(
                        response.operation,
                        response.accounts
                    )
                    val action =
                        NavigationGraphDirections.actionGlobalSelectAccountFragment(parameter)
                    navController.navigate(action)
                }
            }
            is EnrollPinResponse -> {
                val navigationParameter = PinNavigationParameter(
                    PinViewMode.ENROLL_PIN,
                    lastRecoverableError = response.lastRecoverableError
                )
                val action = NavigationGraphDirections.actionGlobalPinFragment(navigationParameter)
                navController.navigate(action)
            }
            is VerifyFingerprintResponse -> {
                val action = NavigationGraphDirections.actionGlobalBiometricFragment(
                    VerifyBiometricNavigationParameter(VerifyBiometricViewMode.FINGERPRINT)
                )
                navController.navigate(action)
            }
            is VerifyBiometricResponse -> {
                val action = NavigationGraphDirections.actionGlobalBiometricFragment(
                    VerifyBiometricNavigationParameter(VerifyBiometricViewMode.BIOMETRIC)
                )
                navController.navigate(action)
            }
            is VerifyPinResponse -> {
                val navigationParameter = PinNavigationParameter(
                    PinViewMode.VERIFY_PIN,
                    response.pinAuthenticatorProtectionStatus,
                    response.lastRecoverableError
                )
                val action = NavigationGraphDirections.actionGlobalPinFragment(navigationParameter)
                navController.navigate(action)
            }
            is CompletedResponse -> {
                viewModel.finishOperation()
                val navigationParameter =
                    ResultNavigationParameter.forSuccessfulOperation(response.operation)
                val action =
                    NavigationGraphDirections.actionGlobalResultFragment(navigationParameter)
                navController.navigate(action)
            }
            is AuthenticationCompletedResponse -> {
                viewModel.finishOperation()
                when (response.forOperation) {
                    Operation.DEREGISTRATION -> {
                        viewModel.deregister(
                            response.username,
                            response.authorizationProvider
                        )
                    }
                    else -> {
                        val navigationParameter =
                            ResultNavigationParameter.forSuccessfulOperation(Operation.AUTHENTICATION)
                        val action =
                            NavigationGraphDirections.actionGlobalResultFragment(navigationParameter)
                        navController.navigate(action)
                    }
                }
            }
            is CancelledResponse -> {
                val navigationParameter =
                    ResultNavigationParameter.forCancelledOperation(response.operation)
                val action =
                    NavigationGraphDirections.actionGlobalResultFragment(navigationParameter)
                navController.navigate(action)
            }
            is ChangePinResponse -> {
                val navigationParameter = PinNavigationParameter(
                    PinViewMode.CHANGE_PIN,
                    response.pinAuthenticatorProtectionStatus,
                    response.lastRecoverableError
                )
                val action = NavigationGraphDirections.actionGlobalPinFragment(navigationParameter)
                navController.navigate(action)
            }
            is ErrorResponse -> {
                viewModel.finishOperation()
                errorHandlerChain.handle(response.cause)
            }
            else -> {
                errorHandlerChain.handle(BusinessException.invalidState())
            }
        }
    }
    //endregion
}