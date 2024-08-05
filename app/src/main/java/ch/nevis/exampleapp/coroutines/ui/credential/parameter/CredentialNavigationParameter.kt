/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.credential.parameter

import android.os.Parcelable
import ch.nevis.exampleapp.coroutines.ui.credential.model.CredentialViewMode
import ch.nevis.mobile.sdk.api.operation.RecoverableError

/**
 * Interface for Credential view navigation parameter.
 */
abstract class CredentialNavigationParameter : Parcelable {
    //region Properties
    /**
     * The mode, the Credential view intend to be used/initialized.
     */
    abstract val credentialViewMode: CredentialViewMode

    /**
     * The last recoverable error. It exists only if there was already a failed PIN/Password operation
     * attempt.
     */
    abstract val lastRecoverableError: RecoverableError?
    //endregion
}
