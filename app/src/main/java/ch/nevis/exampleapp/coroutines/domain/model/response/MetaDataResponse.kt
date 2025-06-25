/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2025. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

/**
 * A [Response] class that holds the meta data of Nevis Mobile Authentication SDK.
 *
 * @constructor Creates a new instance.
 * @param sdkVersion The version of Nevis Mobile Authentication SDK.
 * @param facetId The application facet identifier.
 * @param certificateFingerprint The certificate fingerprint.
 */
data class MetaDataResponse(
    /**
     * The version of Nevis Mobile Authentication SDK.
     */
    val sdkVersion: String,
    /**
     * The application facet identifier.
     */
    val facetId: String,
    /**
     * The certificate fingerprint.
     */
    val certificateFingerprint: String
) : Response
