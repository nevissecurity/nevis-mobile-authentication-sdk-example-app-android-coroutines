/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.common.configuration

import ch.nevis.mobile.sdk.api.Configuration

/**
 * Default implementation of [ConfigurationProvider] abstract class.
 */
class ConfigurationProviderImpl(
    override val environment: Environment,
    override val configuration: Configuration
) : ConfigurationProvider()