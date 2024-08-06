/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.repository

import ch.nevis.exampleapp.coroutines.domain.model.state.OperationState

/**
 * Interface declaration of operation state repository that can store and give back a [OperationState] instance.
 */
interface OperationStateRepository<T : OperationState> {

    /**
     * Saves a [OperationState] instance into the repository.
     *
     * @param state The [OperationState] instance to be saved.
     */
    fun save(state: T)

    /**
     * Gets the [OperationState] instance stored by the repository.
     *
     * @return The saved [OperationState] instance or null if there isn't any stored instance.
     */
    fun get(): T?

    /**
     * Resets the repository, removes the stored [OperationState] instance from the repository.
     */
    fun reset()
}
