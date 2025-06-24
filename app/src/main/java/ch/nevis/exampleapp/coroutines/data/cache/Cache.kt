/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.data.cache

/**
 * Interface declaration of cache that can store and give back an arbitrary item.
 */
interface Cache<T> {
    /**
     * Saves an item into the cache.
     *
     * @param item The item to be saved.
     */
    fun save(item: T)

    /**
     * Gets the item stored by the cache.
     *
     * @return The saved item or null if there isn't any stored one.
     */
    fun get(): T?

    /**
     * Resets the cache, removes the stored item from the cache.
     */
    fun reset()
}
