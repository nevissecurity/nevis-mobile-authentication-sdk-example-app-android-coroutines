/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.data.repository

import ch.nevis.exampleapp.coroutines.data.cache.Cache
import ch.nevis.exampleapp.coroutines.domain.model.state.OperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository

/**
 * Default implementation of [OperationStateRepository] interface.
 *
 * @constructor Creates a new instance.
 * @param cache The state cache for operations.
 */
class OperationStateRepositoryImpl<T : OperationState>(
    private val cache: Cache<T>
) : OperationStateRepository<T> {

    //region OperationStateRepository
    override fun save(state: T) {
        cache.save(state)
    }

    override fun get(): T? {
        return cache.get()
    }

    override fun reset() {
        cache.reset()
    }
    //endregion
}
