/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

/**
 * Use-case for finishing any previously started operation.
 *
 * When an operation successfully ends or it ends with an error the operation has to be finished that
 * means all caches, states that were used during the operation have to be cleared.
 */
interface FinishOperationUseCase {

    /**
     * Executes the use-case.
     */
    suspend fun execute()
}