/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2025. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import android.content.Context
import ch.nevis.exampleapp.coroutines.domain.model.response.MetaDataResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.metadata.MetaData
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Default implementation of [MetaDataUseCase] interface that queries the meta data of Nevis Mobile
 * Authentication SDK and returns them in a [MetaDataResponse].
 */
class MetaDataUseCaseImpl(
    @ApplicationContext
    private val context: Context,
) : MetaDataUseCase {

    //region MetaDataUseCase
    override suspend fun execute(): Response {
        return MetaDataResponse(
            sdkVersion = MetaData.mobileAuthenticationVersion().toString(),
            facetId = MetaData.applicationFacetId(context),
            certificateFingerprint = MetaData.signingCertificateSha256(context)
        )
    }
    //endregion
}
