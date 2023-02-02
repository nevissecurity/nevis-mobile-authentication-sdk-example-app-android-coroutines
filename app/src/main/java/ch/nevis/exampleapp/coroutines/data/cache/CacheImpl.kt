/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.data.cache

/**
 * Default implementation of [Cache] interface.
 */
class CacheImpl<T> : Cache<T> {

    //region Properties
    private var item: T? = null
    //endregion

    //region Cache
    override fun save(item: T) {
        this.item = item
    }

    override fun get(): T? {
        return item
    }

    override fun reset() {
        item = null
    }
    //endregion
}