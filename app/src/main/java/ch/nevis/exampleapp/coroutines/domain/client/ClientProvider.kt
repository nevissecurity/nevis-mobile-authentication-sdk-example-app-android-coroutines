/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.client

import ch.nevis.mobile.sdk.api.MobileAuthenticationClient

/**
 * Interface declaration of client provider that can store and provide a [MobileAuthenticationClient] instance.
 */
interface ClientProvider {

    /**
     * Saves a [MobileAuthenticationClient] instance into the provider.
     *
     * @param client The [MobileAuthenticationClient] instance to be saved.
     */
    fun save(client: MobileAuthenticationClient)

    /**
     * Gets the [MobileAuthenticationClient] instance stored by the provider.
     *
     * @return The saved [MobileAuthenticationClient] instance or null if there isn't any stored instance.
     */
    fun get(): MobileAuthenticationClient?

    /**
     * Resets the provider, removes the stored [MobileAuthenticationClient] instance from the provider.
     */
    fun reset()
}
