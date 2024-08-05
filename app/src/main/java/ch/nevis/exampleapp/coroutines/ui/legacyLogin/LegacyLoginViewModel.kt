/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.legacyLogin

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.usecase.InBandRegistrationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.LoginUseCase
import ch.nevis.exampleapp.coroutines.ui.base.OperationViewModel
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider.CookieAuthorizationProvider
import ch.nevis.mobile.sdk.api.authorization.Cookie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.PasswordAuthentication
import javax.inject.Inject

/**
 * View model implementation of Legacy Login view.
 *
 * @constructor Creates a new instance.
 * @param loginUseCase An instance of a [LoginUseCase] implementation.
 * @param inBandRegistrationUseCase An instance of a [InBandRegistrationUseCase] implementation.
 */
@HiltViewModel
class LegacyLoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val inBandRegistrationUseCase: InBandRegistrationUseCase
) : OperationViewModel() {

    //region Public Interface
    /**
     * Starts legacy login process with given username/password pair.
     *
     * @param passwordAuthentication The [PasswordAuthentication] object that holds the username and
     * the password be used to log in.
     */
    fun legacyLogin(passwordAuthentication: PasswordAuthentication) {
        viewModelScope.launch(errorHandler) {
            val response = loginUseCase.execute(passwordAuthentication)
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Starts in-band registration operation based on data/information received during the legacy
     * login process.
     *
     * @param extId The external identifier of the user to be registered.
     * @param cookies HTTP session cookies to be used for the in-base registration end-point.
     */
    fun register(extId: String, cookies: List<Cookie>) {
        viewModelScope.launch(errorHandler) {
            val authorizationProvider = CookieAuthorizationProvider.create(cookies)
            val response = inBandRegistrationUseCase.execute(extId, authorizationProvider)
            mutableResponseLiveData.postValue(response)
        }
    }
    //endregion
}
