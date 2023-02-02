/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.repository.LoginRepository
import timber.log.Timber
import java.net.PasswordAuthentication

/**
 * Default implementation of [LoginUseCase] interface.
 */
class LoginUseCaseImpl(
    /**
     * An instance of a [LoginRepository] implementation that is used for the login process.
     */
    private val loginRepository: LoginRepository
) : LoginUseCase {

    //region LoginUseCase
    override suspend fun execute(passwordAuthentication: PasswordAuthentication): Response {
        return try {
            return loginRepository.execute(passwordAuthentication)
        } catch (exception: Exception) {
            // In case of legacy login we handle all errors as login failed, but the original exception is logged.
            Timber.e(exception)
            ErrorResponse(BusinessException.loginFailed())
        }
    }
    //endregion
}