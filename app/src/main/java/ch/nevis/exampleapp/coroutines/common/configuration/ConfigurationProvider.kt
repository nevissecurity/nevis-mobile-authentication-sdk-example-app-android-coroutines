/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.common.configuration

import ch.nevis.mobile.sdk.api.Configuration

/**
 * Abstract class declaration of a configuration provider that provides information about
 * the configured environment and the related [Configuration] object as well.
 */
abstract class ConfigurationProvider {

    //region Properties
    /**
     * The selected/configured [Environment] value.
     */
    abstract val environment: Environment

    /**
     * The [Configuration] object related to the environment.
     */
    abstract val configuration: Configuration

    /**
     * The list of allowed authenticators.
     */
    abstract val authenticatorAllowlist: List<String>
    //endregion
}
