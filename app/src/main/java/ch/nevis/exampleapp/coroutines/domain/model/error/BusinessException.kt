/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.error

import ch.nevis.exampleapp.coroutines.R

/**
 * Enumeration of business exception types.
 */
enum class BusinessExceptionType(
    val resId: Int
) {

    /**
     * Registered accounts not found
     */
    ACCOUNTS_NOT_FOUND(R.string.business_error_type_accounts_not_found),

    /**
     * Client not initialized
     */
    CLIENT_NOT_INITIALIZED(R.string.business_error_type_client_not_initialized),

    /**
     * Device information not found
     */
    DEVICE_INFORMATION_NOT_FOUND(R.string.business_error_type_device_information_not_found),

    /**
     * Invalid state
     */
    INVALID_STATE(R.string.business_error_type_invalid_state),

    /**
     * Legacy login failed
     */
    LOGIN_FAILED(R.string.business_error_type_login_failed)
}

/**
 * A sub-class of [Exception] that represents a business exception.
 */
class BusinessException private constructor(
    /**
     * Business exception type.
     */
    val type: BusinessExceptionType
) : Exception() {
    companion object {

        /**
         * Helper static method to initialize a [BusinessException] with type [BusinessExceptionType.ACCOUNTS_NOT_FOUND].
         */
        fun accountsNotFound() = BusinessException(BusinessExceptionType.ACCOUNTS_NOT_FOUND)

        /**
         * Helper static method to initialize a [BusinessException] with type [BusinessExceptionType.CLIENT_NOT_INITIALIZED].
         */
        fun clientNotInitialized() = BusinessException(BusinessExceptionType.CLIENT_NOT_INITIALIZED)

        /**
         * Helper static method to initialize a [BusinessException] with type [BusinessExceptionType.DEVICE_INFORMATION_NOT_FOUND].
         */
        fun deviceInformationNotFound() =
            BusinessException(BusinessExceptionType.DEVICE_INFORMATION_NOT_FOUND)

        /**
         * Helper static method to initialize a [BusinessException] with type [BusinessExceptionType.INVALID_STATE].
         */
        fun invalidState() = BusinessException(BusinessExceptionType.INVALID_STATE)

        /**
         * Helper static method to initialize a [BusinessException] with type [BusinessExceptionType.LOGIN_FAILED].
         */
        fun loginFailed() = BusinessException(BusinessExceptionType.LOGIN_FAILED)
    }
}