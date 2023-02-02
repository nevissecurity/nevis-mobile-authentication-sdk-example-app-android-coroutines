/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.client

import ch.nevis.mobile.sdk.api.MobileAuthenticationClient

/**
 * Default implementation of [ClientProvider] interface.
 */
class ClientProviderImpl : ClientProvider {

    //region Properties
    private var client: MobileAuthenticationClient? = null
    //endregion

    //region ClientProvider
    override fun save(client: MobileAuthenticationClient) {
        this.client = client
    }

    override fun get(): MobileAuthenticationClient? {
        return client
    }

    override fun reset() {
        client = null
    }
    //endregion
}