/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2025. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.error.MobileAuthenticationClientException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.FidoUafAttestationInformationResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.devicecapabilities.FidoUafAttestationInformation.OnlyDefaultMode
import ch.nevis.mobile.sdk.api.devicecapabilities.FidoUafAttestationInformation.OnlySurrogateBasicSupported
import ch.nevis.mobile.sdk.api.devicecapabilities.FidoUafAttestationInformation.StrictMode
import ch.nevis.mobile.sdk.api.devicecapabilities.FidoUafAttestationInformation.StrictStrongBoxMode
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

/**
 * Default implementation of [GetFidoUafAttestationInformationUseCase] interface that queries the
 * FIDO UAF attestation information from the client and returns them in a [FidoUafAttestationInformationResponse].
 *
 * @constructor Creates a new instance.
 * @param clientProvider An instance of [ClientProvider] interface implementation.
 */
class GetFidoUafAttestationInformationUseCaseImpl(private val clientProvider: ClientProvider) : GetFidoUafAttestationInformationUseCase {

    //region GetFidoUafAttestationInformationUseCase
    override suspend fun execute(): Response = suspendCancellableCoroutine { cancellableContinuation ->
        val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
        client.deviceCapabilities().fidoUafAttestationInformationGetter()
            .onSuccess { information ->
                when (information) {
                    is OnlySurrogateBasicSupported -> {
                        Timber.asTree()
                            .sdk("Only surrogate basic attestation supported.")
                        if (information.cause() != null) {
                            Timber.asTree().sdk("Cause: ${information.cause()}")
                        }
                        cancellableContinuation.resume(FidoUafAttestationInformationResponse.OnlySurrogateBasicSupported())
                    }
                    is OnlyDefaultMode -> {
                        Timber.asTree().sdk("Full basic default attestation mode supported.")
                        if (information.cause() != null) {
                            Timber.asTree().sdk("Cause: ${information.cause()}")
                        }
                        cancellableContinuation.resume(FidoUafAttestationInformationResponse.OnlyDefaultMode())
                    }
                    is StrictMode -> {
                        Timber.asTree().sdk("Full basic strict attestation mode supported.")
                        cancellableContinuation.resume(FidoUafAttestationInformationResponse.StrictMode())
                    }
                    is StrictStrongBoxMode -> {
                        Timber.asTree().sdk("Full basic strict-strongbox attestation mode supported.")
                        cancellableContinuation.resume(FidoUafAttestationInformationResponse.StrictStrongBoxMode())
                    }
                    else -> throw IllegalStateException("Unsupported attestation information type.")
                }
            }
            .onError {
                cancellableContinuation.resume(
                    ErrorResponse(
                        MobileAuthenticationClientException(
                            Operation.GET_FIDO_UAF_ATTESTATION_INFORMATION,
                            it
                        )
                    )
                )
            }
            .execute()
    }
    //endregion
}
