/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.DeviceInformationResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Default implementation of [GetDeviceInformationUseCase] interface that queries the stored
 * [ch.nevis.mobile.sdk.api.localdata.DeviceInformation] from the client and returns it as a
 * [DeviceInformationResponse]. If there isn't any stored device information in the client an
 * [ErrorResponse] will be returned.
 */
class GetDeviceInformationUseCaseImpl(
    /**
     * An instance of a [ClientProvider] implementation.
     */
    private val clientProvider: ClientProvider
) : GetDeviceInformationUseCase {

    //region GetDeviceInformationUseCase
    /**
     * Executes the use-case.
     *
     * @return A [DeviceInformationResponse] or an [ErrorResponse] If there isn't any stored
     * device information in the client.
     */
    override suspend fun execute(): Response {
        val client = clientProvider.get()
        return if (client != null) {
            val deviceInformation = client.localData().deviceInformation().orElse(null)
            if (deviceInformation != null) {
                DeviceInformationResponse(deviceInformation)
            } else {
                ErrorResponse(BusinessException.deviceInformationNotFound())
            }
        } else {
            ErrorResponse(BusinessException.clientNotInitialized())
        }
    }
    //endregion
}