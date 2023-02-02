/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.localdata.DeviceInformation

/**
 * A [Response] class that holds the requested [DeviceInformation] object.
 */
data class DeviceInformationResponse(
    /**
     * The requested [DeviceInformation] object.
     */
    val deviceInformation: DeviceInformation
) : Response
